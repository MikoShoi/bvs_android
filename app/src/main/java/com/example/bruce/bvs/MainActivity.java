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
import com.example.mikotools.MikoLogger;
import com.example.networkcontroller.HttpConnection;
import com.example.networkcontroller.ResponseListener;
import com.example.viewer3d.Viewer3D;
import com.infoScreens.InfoAnimation;
import com.infoScreens.InfoImage;
import com.instructions.InstructionViewer;
import com.instructions.InstructionViewerListener;

import java.util.Collections;
import java.util.List;

import okhttp3.Response;

public class MainActivity
        extends AppCompatActivity
        implements InstructionViewerListener
                    , DocumentViewerListener
                    , CameraListener
                    , ResponseListener
                    , FragmentManager.OnBackStackChangedListener
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
    fragmentManager.addOnBackStackChangedListener(this);

    configSlidingMenu();

    moveTo(Tab.WELCOME);
    if( isNetworkConnectionAvailable() )
    {
      httpConnection = new HttpConnection(this, this);
      httpConnection.sendGetRequest(serverAddress);
    }
    else
      moveTo(Tab.INTERNER_CONNECTION_UNAVAILABLE);
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

    moveTo( Viewer3D.newInstance(absFilePath) );
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
    moveTo(Tab.INTERNER_CONNECTION_UNAVAILABLE);
  }
  @Override
  public void onBackStackChanged ()
  {
    List<Fragment> fragments = fragmentManager.getFragments();

    if ( fragments == null )
    {
      return;
    }
    else if ( fragments.contains(null) )
    {
      fragments.removeAll( Collections.singleton(null) );
    }

    int indicesCount    = fragments.size()
      , lastFIndex      = indicesCount  - 1
      , prevFIndex      = lastFIndex    - 1;

    if ( indicesCount < 3)
    {
      backStackIndex = 0;
    }
    else if ( !isInfoGraphic(fragments.get(prevFIndex)) )
    {
      backStackIndex = prevFIndex;
    }
    else
    {
      for (int i = lastFIndex; i >= 0; i--)
      {
        if ( !isInfoGraphic(fragments.get(i)) )
          backStackIndex = i;
      }
    }
  }
  private boolean isInfoGraphic(Fragment fragment)
  {
    String  f   = fragment.getClass().getName()
          , ia  = InfoAnimation.class.getName()
          , ii  = InfoImage.class.getName();

    return ( f.equals(ia) || f.equals(ii) );
  }
  @Override
  public void onBackPressed         ()
  {
    if (backStackIndex > 0)
    {
      fragmentManager.popBackStack(backStackIndex, 0);
    }
    else
    {
      closeApp();
    }
  }

  private void moveTo               (Tab tab)
  {
    switch (tab)
    {
      case WELCOME:
        moveTo( InfoImage.newInstance(
                R.drawable.welcome_h
              , R.drawable.welcome_v ) );
        break;
      case INTERNER_CONNECTION_UNAVAILABLE:
        moveTo( InfoImage.newInstance(
                R.drawable.internet_connection_unavailable_h
              , R.drawable.internet_connection_unavailable_v) );
        break;
      case LOADER:
        moveTo( InfoAnimation.newInstance(
                R.raw.loading
              , R.string.loaderDescription) );
        break;
      case INSTRUCTIONS:
        moveTo( new InstructionViewer() );
        break;
      case DOCUMENTS:
        moveTo( new DocumentViewer() );
        break;
      case CAMERA:
        moveTo( new Camera() );
        break;
      default:
        Log.i("MainActivity moveTo: ","Unsupported tab");
    }
  }
  private void moveTo               (Fragment fragment)
  {
    String marker = fragment.getClass().getName();

    fragmentManager
            .beginTransaction()
            .replace(R.id.mainFrame, fragment, marker)
            .addToBackStack(marker)
            .commit();
  }

  private void configSlidingMenu    ()
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

  private NavigationView  menu              = null;
  private DrawerLayout    drawer            = null;
  private HttpConnection  httpConnection    = null;
  private FragmentManager fragmentManager   = null;

  private int backStackIndex = 1;

  private final String    serverAddress     = "http://184ab88a.ngrok.io"
                        , getModelEndpoint  = "/getModel"
                        , addImageEndpoint  = "/addImage";
}
