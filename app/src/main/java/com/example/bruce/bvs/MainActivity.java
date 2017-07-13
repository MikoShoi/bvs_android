package com.example.bruce.bvs;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.androidnetworking.error.ANError;
import com.documents.DocumentViewer;
import com.documents.DocumentViewerListener;
import com.example.bruce.bvs.databinding.MainBinding;
import com.example.camera.Camera;
import com.example.camera.CameraListener;
import com.example.mikotools.AppManager;
import com.example.mikotools.FileReader;
import com.example.mikotools.MikoLogger;
import com.example.networkcontroller.HttpConnection;
import com.example.networkcontroller.ResponseListener;
import com.example.viewer3d.Viewer3D;
import com.flowController.FlowController;
import com.infoScreens.InfoAnimation;
import com.infoScreens.InfoImage;
import com.instructions.InstructionViewer;
import com.instructions.InstructionViewerListener;

import okhttp3.Response;

public class MainActivity
        extends AppCompatActivity
        implements InstructionViewerListener
                  , DocumentViewerListener
                  , CameraListener
                  , ResponseListener
{
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    MainBinding mainActivity = DataBindingUtil.setContentView(this, R.layout.main);

    FileReader.getInstance().setResources( getResources() );
    prepareMenu(mainActivity.menu, mainActivity.drawer);

    flowController = new FlowController( getSupportFragmentManager() );
    flowController.addFragmentsToSkip(InfoImage.class.getName()
                                    , InfoAnimation.class.getName() );

    if( isNetworkConnectionAvailable() )
    {
      moveTo(Tab.WELCOME);

      httpConnection = new HttpConnection(this, this);
      httpConnection.sendGetRequest(serverAddress);
    }
    else
      moveTo(Tab.INTERNET_CONNECTION_UNAVAILABLE);
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
    moveTo(Tab.LOADER);

    httpConnection.downloadFile(serverAddress + getModelEndpoint, "model.off");
  }
  @Override
  public void onPhotoCaptured       (String absoluteFilePath)
  {
    httpConnection.uploadFile(serverAddress + addImageEndpoint, absoluteFilePath);
  }
  @Override
  public void onUploadedFile        (String serverAddress, Response response)
  {
    MikoLogger.log("onUploadedFile");
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
  public void onErrorOccurred       (ANError       error)
  {
    moveTo(Tab.SERVER_CONNECTION_PROBLEM);
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
                R.drawable.welcome_h
              , R.drawable.welcome_v ) );
        break;
      case INTERNET_CONNECTION_UNAVAILABLE:
        flowController.moveTo( InfoImage.newInstance(
                R.drawable.internet_connection_unavailable_h
              , R.drawable.internet_connection_unavailable_v) );
        break;
      case SERVER_CONNECTION_PROBLEM:
        flowController.moveTo( InfoImage.newInstance(
                R.drawable.server_connection_problem_h
              , R.drawable.server_connection_problem_v) );
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
        flowController.moveTo( new Camera() );
        break;
      default:
        MikoLogger.log("Unsupported tab");
    }
  }

  private void prepareMenu          (NavigationView menu, final DrawerLayout drawer)
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
        item.setChecked(false);

        return true;
      }
    };

    menu.setNavigationItemSelectedListener(l);

    //TODO: repair. deselect sliding menu item
  }
  private void closeApp             ()
  {
    AppManager.getInstance().cleanTempDirContent();

    finish();
    System.exit(0);
  }
  private boolean isNetworkConnectionAvailable()
  {
    ConnectivityManager cm  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    boolean isWifiTurnOn    = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()
          , isMobileTurnOn  = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();

    return (isWifiTurnOn || isMobileTurnOn);
  }

  private HttpConnection  httpConnection    = null;
  private FlowController  flowController    = null;

  private final String    serverAddress     = "http://b6230ff8.ngrok.io"
                        , getModelEndpoint  = "/getModel"
                        , addImageEndpoint  = "/addImage";
}
