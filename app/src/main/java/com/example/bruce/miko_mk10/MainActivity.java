package com.example.bruce.miko_mk10;

import android.databinding.DataBindingUtil;
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
import com.example.bruce.miko_mk10.databinding.MainBinding;
import com.example.camera.Camera;
import com.example.camera.CameraListener;
import com.example.mikotools.AppManager;
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

    MainBinding mainActivity = DataBindingUtil.setContentView(this, R.layout.main);

    menu            = mainActivity.menu;
    drawer          = mainActivity.drawer;
    fragmentManager = getSupportFragmentManager();

    configSlidingMenu();

    httpConnection = new HttpConnection(this, this);
    httpConnection.sendGetRequest(serverAddress);

    moveTo(InfoImage.newInstance(R.drawable.welcome), "welcome", false);
  }

  @Override
  public void onInstructionsViewed  ()
  {
    moveTo(new Camera(), "camera", true);
  }
  @Override
  public void onDocumentsViewed     ()
  {
    onBackPressed();
  }

//-- camera
  @Override
  public void onShootingFinished    ()
  {
    moveTo( InfoAnimation.newInstance( R.raw.loading
                                    , R.string.loaderDescription)
          , "loader"
          , false );

    httpConnection.downloadFile(serverAddress + getModelEndpoint, "model.off");
  }
  @Override
  public void onPhotoCaptured       (String absoluteFilePath)
  {
    httpConnection.uploadFile(serverAddress + addImageEndpoint, absoluteFilePath);
  }

//-- http connection
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
      boolean firstLaunch = new AppManager().isAppFirstTimeLaunch( getApplicationContext() );

      if (firstLaunch)
      {
        moveTo(new InstructionViewer(), "instructions", true);
      }
      else
      {
        moveTo(new Camera(), "camera", true);
      }
    }
  }
  @Override
  public void onErrorOccurred         (ANError       error)
  {
    moveTo(InfoImage.newInstance(R.drawable.no_connection), "noConnection", false);
  }

  @Override
  public void onBackPressed           ()
  {
    for(int entry = 0; entry < fragmentManager.getBackStackEntryCount(); entry++)
    {
      Log.i("Back stack", "Found fragment: " + fragmentManager.getBackStackEntryAt(entry).getName());
    }

    if ( fragmentManager.getBackStackEntryCount() > 0 )
    {
      fragmentManager.popBackStack();
    }
    else
    {
      super.onBackPressed();
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
        final int docsItemId = R.id.nav_item_documents
                , quitItemId = R.id.nav_item_quit
                , itemId     = item.getItemId();

         if ( itemId == docsItemId )
           moveTo( new DocumentViewer(), "documents", true);
        else if ( itemId == quitItemId )
          closeApp();
        else
          throw new MikoError(this
                            , "onNavigationItemSelected"
                            , "Unknown menu item");

        drawer.closeDrawer(GravityCompat.START);
        item.setChecked(false);

        //TODO: repair. deselect sliding menu item

        return true;
      }
    };

    menu.setNavigationItemSelectedListener(l);
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
