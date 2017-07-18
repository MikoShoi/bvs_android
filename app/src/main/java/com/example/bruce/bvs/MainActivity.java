package com.example.bruce.bvs;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.androidnetworking.error.ANError;
import com.camera.Camera;
import com.camera.CameraListener;
import com.camera.PhotoUploadProgressListener;
import com.documents.DocumentViewer;
import com.documents.DocumentViewerListener;
import com.example.bruce.bvs.databinding.MainBinding;
import com.example.mikotools.AppManager;
import com.example.mikotools.FileReader;
import com.example.mikotools.MikoLogger;
import com.example.viewer3d.Viewer3D;
import com.flowController.FlowController;
import com.httpConnection.HttpConnection;
import com.httpConnection.ResponseListener;
import com.infoScreens.InfoAnimation;
import com.infoScreens.InfoImage;
import com.instructions.InstructionViewer;
import com.instructions.InstructionViewerListener;

import java.util.Map;
import java.util.Set;

import okhttp3.Response;

public class MainActivity
        extends AppCompatActivity
        implements  InstructionViewerListener
                  , ResponseListener
                  , CameraListener
                  , DocumentViewerListener
{
  @Override
  protected void onCreate           (Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    MainBinding mainActivity = DataBindingUtil.setContentView(this, R.layout.main);

    FileReader.getInstance().setResources( getResources() );

    prepareMenu(mainActivity.menu, mainActivity.drawer);
    prepareFlowController();

    if (savedInstanceState == null)
      startNewSession();
  }
  @Override
  protected void onSaveInstanceState(Bundle outState)
  {
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onInstructionsViewed  ()
  {
    moveTo(Tab.CAMERA);
  }
  @Override
  public void onDocumentsViewed     ()
  {
    onBackPressed();
  }
  @Override
  public void onShootingFinished    ()
  {
    doneButtonClicked = true;
  }
  @Override
  public void onCapturePhoto        ()
  {
    uploadRequestsNumber++;
  }
  @Override
  public void onPhotoCaptured       (String absoluteFilePath)
  {
    uploadedFilesInfo.put( absoluteFilePath, Float.valueOf(0) );
    httpConnection.uploadFile(serverAddress + "/addImage", absoluteFilePath);
  }
  @Override
  public void onUploadedFile        (String serverAddress, Response response)
  {
    uploadResponsesNumber++;

    if (doneButtonClicked && uploadRequestsNumber == uploadResponsesNumber)
    {
      moveTo(Tab.LOADER);
      httpConnection.downloadFile(this.serverAddress + "/getModel", "model.off");
    }
  }
  @Override
  public void onDownloadedFile      (String serverAddress, final String absFilePath)
  {
    flowController.moveTo( Viewer3D.newInstance(absFilePath) );
  }
  @Override
  public void onGETResponseReceived (String serverAddress, Response response)
  {
    if ( serverAddress.equals(this.serverAddress) )
    {
      moveTo( AppManager.getInstance().isAppFirstTimeLaunch(this)
              ? Tab.INSTRUCTIONS
              : Tab.CAMERA );
    }
  }
  @Override
  public void onUploadProgressChanged (String filePath, float percentage)
  {
    int sumOfProgresses = 0;

    uploadedFilesInfo.put(filePath, percentage);
    Set<Map.Entry<String, Float>> uploadedFiles = uploadedFilesInfo.entrySet();
    for(Map.Entry<String, Float> uploadedFile : uploadedFiles)
    {
      sumOfProgresses += uploadedFile.getValue().floatValue() * 100.0f;
    }

    int midProgress = sumOfProgresses / uploadedFiles.size();
    photoUploadProgressListener.onPhotoUploadProgressChanged(midProgress);
  }
  @Override
  public void onErrorOccurred       (ANError error)
  {
    moveTo(Tab.INFO_PROBLEM_SERVER);
  }
  @Override
  public void onBackPressed         ()
  {
    if ( flowController.canMoveBack() )
      flowController.moveBack();
    else
      closeApp();
  }
  public void moveTo                (Tab tab)
  {
    switch (tab)
    {
      case WELCOME:
        flowController.moveTo( InfoImage.newInstance(
                R.drawable.welcome ) );
        break;
      case INFO_PROBLEM_OFFLINE:
        flowController.moveTo( InfoImage.newInstance(
                R.drawable.problem_offline ) );
        break;
      case INFO_PROBLEM_SERVER:
        flowController.moveTo( InfoImage.newInstance(
                R.drawable.problem_server ) );
        break;
      case LOADER:
        flowController.moveTo( InfoAnimation.newInstance(
                R.raw.loading
              , R.string.loaderDescription) );
        break;
      case INSTRUCTIONS:
        flowController.moveTo( new InstructionViewer() );
        break;
      case DOCUMENTS:
        flowController.moveTo( new DocumentViewer() );
        break;
      case CAMERA:
        Camera camera = new Camera();
        photoUploadProgressListener = camera;
        flowController.moveTo(camera);
        break;
      default:
        MikoLogger.log("Unsupported tab");
    }
  }

  private void    startNewSession       ()
  {
    if( isOnline() )
    {
      moveTo(Tab.WELCOME);

      httpConnection = new HttpConnection(this, this);
      httpConnection.sendGetRequest(serverAddress);
    }
    else
      moveTo(Tab.INFO_PROBLEM_OFFLINE);
  }
  private void    prepareMenu           (final NavigationView menu, final DrawerLayout drawer)
  {
    OnNavigationItemSelectedListener l = new OnNavigationItemSelectedListener()
    {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item)
      {
        switch (item.getItemId())
        {
          case R.id.nav_item_documents:
            moveTo(Tab.DOCUMENTS);
            break;
          case R.id.nav_item_quit:
            closeApp();
            break;
          default:
            MikoLogger.log("Unknown menu item");
        }

        drawer.closeDrawer(GravityCompat.START);
        item.setCheckable(false);

        return true;
      }
    };

    menu.setNavigationItemSelectedListener(l);

    //TODO: repair. deselect sliding menu item
  }
  private void    prepareFlowController ()
  {
    flowController = new FlowController( getSupportFragmentManager() );
    flowController.addFragmentsToSkip(InfoImage.class.getName()
                                    , InfoAnimation.class.getName() );
  }
  private void    closeApp              ()
  {
    AppManager.getInstance().cleanTempDirContent();

    finish();
    System.exit(0);
  }
  private boolean isOnline              ()
  {
    ConnectivityManager cm  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    boolean isWifiTurnOn    = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()
          , isMobileTurnOn  = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();

    return (isWifiTurnOn || isMobileTurnOn);
  }

  private HttpConnection  httpConnection  = null;
  private FlowController  flowController  = null;
  private PhotoUploadProgressListener photoUploadProgressListener = null;

  private final String serverAddress    = "http://dfe0de0c.ngrok.io";
  private boolean doneButtonClicked     = false;
  private int     uploadRequestsNumber  = 0
                , uploadResponsesNumber = 0;
  private ArrayMap<String, Float> uploadedFilesInfo = new ArrayMap<>(0);
}
