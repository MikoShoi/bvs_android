package com.example.bruce.bvs;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.androidnetworking.error.ANError;
import com.documents.DocumentViewer;
import com.documents.DocumentViewerListener;
import com.example.bruce.bvs.databinding.MainBinding;
import com.example.camera.Camera;
import com.example.camera.CameraListener;
import com.example.mikotools.AppManager;
import com.example.mikotools.FileReader;
import com.example.mikotools.MikoError;
import com.example.networkcontroller.HttpConnection;
import com.example.networkcontroller.ResponseListener;
import com.example.viewer3d.Viewer3D;
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
    setContentView(R.layout.main);

    FileReader.getInstance().setResources( getResources() );

    MainBinding mainActivity = DataBindingUtil.setContentView(this, R.layout.main);

    menu            = mainActivity.menu;
    drawer          = mainActivity.drawer;
    fragmentManager = getSupportFragmentManager();

    configSlidingMenu();

    moveTo(Tab.WELCOME);
    if( isNetworkConnectionAvailable() )
    {
      httpConnection = new HttpConnection(this, this);
      httpConnection.sendGetRequest(serverAddress);
    }
    else
      moveTo(Tab.NO_CONNECTION);
  }
  private boolean isNetworkConnectionAvailable()
  {
    ConnectivityManager cm  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    boolean isWifiTurnOn    = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()
          , isMobileTurnOn  = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();

    return (isWifiTurnOn || isMobileTurnOn);
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
    Log.i("MainActivity","onUploadedFile");
  }
  @Override
  public void onDownloadedFile      (String serverAddress, final String absFilePath)
  {
    moveTo( Viewer3D.newInstance( absFilePath
                                , R.raw.vertex_shader
                                , R.raw.fragment_shader )
          , "viewer3D"
          , true );
  }
  @Override
  public void onGetResponseReceived (String serverAddress, Response response)
  {
    if ( serverAddress.equals(this.serverAddress) )
    {
      boolean firstLaunch = new AppManager().isAppFirstTimeLaunch(this);

      moveTo(firstLaunch ? Tab.INSTRUCTIONS : Tab.CAMERA);
    }
  }
  @Override
  public void onErrorOccurred         (ANError       error)
  {
    moveTo(Tab.NO_CONNECTION);
  }

  @Override
  public void onBackPressed           ()
  {
    if ( fragmentManager.getBackStackEntryCount() > 0 )
    {
      fragmentManager.popBackStack();
    }
    else
    {
      super.onBackPressed();
    }
  }
  private void moveTo(Tab tab)
  {
    switch (tab)
    {
      case WELCOME:
        moveTo(InfoImage.newInstance(     R.drawable.welcome),        tab.name(), false);
        break;
      case NO_CONNECTION:
        moveTo(InfoImage.newInstance(     R.drawable.no_connection),  tab.name(), false);
        break;
      case LOADER:
        moveTo(InfoAnimation.newInstance( R.raw.loading
                                        , R.string.loaderDescription)
              , tab.name()
              , false);
        break;
      case INSTRUCTIONS:
        moveTo(new InstructionViewer(), tab.name(), true);
        break;
      case DOCUMENTS:
        moveTo(new DocumentViewer(),    tab.name(), true);
        break;
      case CAMERA:
        moveTo(new Camera(),            tab.name(), true);
        break;
        default:
          Log.i("MainActivity moveTo: ","Unsupported tab");
    }
  }
  private void moveTo(Fragment fragment, String tag, boolean saveOnStack)
  {
    FragmentTransaction transaction = fragmentManager
            .beginTransaction()
            .replace( R.id.mainFrame
                    , fragment
                    , tag );

    if (saveOnStack)
    {
      transaction.addToBackStack(tag);
    }

    transaction.commit();
  }
  private void configSlidingMenu  ()
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
            throw new MikoError(this
                              , "onNavigationItemSelected"
                              , "Unknown menu item");
        }

        drawer.closeDrawer(GravityCompat.START);
        item.setChecked(false);

        return true;
      }
    };

    menu.setNavigationItemSelectedListener(l);

    //TODO: repair. deselect sliding menu item
  }
  private void closeApp           ()
  {
    new AppManager().cleanTempDirContent();

    finish();
    System.exit(0);
  }

  private NavigationView  menu            = null;
  private DrawerLayout    drawer          = null;
  private HttpConnection  httpConnection  = null;
  private FragmentManager fragmentManager = null;

  private final String    serverAddress     = "http://3c077028.ngrok.io"
                        , getModelEndpoint  = "/getModel"
                        , addImageEndpoint  = "/addImage";
}
