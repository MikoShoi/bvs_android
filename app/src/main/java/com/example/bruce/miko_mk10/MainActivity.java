package com.example.bruce.miko_mk10;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.androidnetworking.error.ANError;
import com.example.bruce.miko_mk10.databinding.MainBinding;
import com.example.camera.Camera;
import com.example.camera.CameraListener;
import com.example.firstlaunch.InstructionViewer;
import com.example.firstlaunch.InstructionViewerListener;
import com.example.handytools.AppManager;
import com.example.handytools.MikoError;
import com.example.menupages.DocumentViewer;
import com.example.menupages.DocumentViewerInterface;
import com.example.networkcontroller.HttpConnection;
import com.example.networkcontroller.ResponseListener;
import com.example.viewer3d.Viewer3D;
import com.infoScreens.NoConnectionScreen;
import com.infoScreens.PreloaderScreen;
import com.infoScreens.WelcomeScreen;

import java.util.List;

import okhttp3.Response;

public class MainActivity
        extends AppCompatActivity
        implements InstructionViewerListener
                    , DocumentViewerInterface
                    , CameraListener
                    , ResponseListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initUiElements();
        configSlidingMenu();
        configTabHost();

        httpConnection = new HttpConnection(getApplicationContext(), this);
        httpConnection.sendGetRequest(serverAddress);
    }

    @Override
    public void onInstructionCompleted ()
    {
        setCurrentTab(TAB.CAMERA);
    }
    @Override
    public void onDocumentViewerCompletedHandle     ()
    {
        onBackPressed();
    }

//-- camera
    @Override
    public void onShootingFinished      ()
    {
        setCurrentTab(TAB.PRELOADER);
        httpConnection.downloadFile(serverAddress + getModelEndpoint, "model.off");
    }
    @Override
    public void onPhotoCaptured         (String absoluteFilePath)
    {
        httpConnection.uploadFile(  serverAddress + addImageEndpoint
                                    , absoluteFilePath);
    }

//-- http connection
    @Override
    public void onUploadedFile          (String serverAddress, Response response)
    {
        Log.i("MainActivity","onUploadedFile");
    }
    @Override
    public void onDownloadedFile        (String serverAddress, final String absFilePath)
    {
        Bundle arg = new Bundle();
        arg.putInt   ("rIdVertShader", R.raw.vertex_shader);
        arg.putInt   ("rIdFragShader", R.raw.fragment_shader);
        arg.putString("modelFilePath", absFilePath);

        tabHost.addTab( tabHost.newTabSpec("MyViewer").setIndicator("MyViewer")
                        , Viewer3D.class
                        , arg);

        tabHost.setCurrentTabByTag("MyViewer");
    }
    @Override
    public void onGetResponseReceived   (String serverAddress, Response response)
    {
        if ( serverAddress.equals(this.serverAddress) )
        {
            boolean firstLaunch = new AppManager().isAppFirstTimeLaunch( getApplicationContext() );

            setCurrentTab( firstLaunch ? TAB.INSTRUCTIONS : TAB.INSTRUCTIONS );
        }
    }
    @Override
    public void onErrorOccurred         (ANError       error)
    {
        Log.i("\t\tMainActivity: ","connection error");

        setCurrentTab(TAB.NO_CONNECTION_SCREEN);
    }

    @Override
    public void onBackPressed           ()
    {
//        super.onBackPressed();

        setCurrentTab(previousTab);
    }

    private void initUiElements     ()
    {
        MainBinding mainActivity =
                DataBindingUtil.setContentView(this, R.layout.main);

        tabHost = mainActivity.tabhost;
        menu    = mainActivity.menu;
        drawer  = mainActivity.drawer;
    }
    private void configTabHost      ()
    {
        tabHost.setup(this, getSupportFragmentManager(), R.id.tabhost);
        tabHost.getTabWidget().setVisibility(View.GONE);

        tabHost.addTab(tabHost.newTabSpec("InstructionViewer").setIndicator("fl")
                ,InstructionViewer.class, null);
        tabHost.addTab(tabHost.newTabSpec("MvpController").setIndicator("cp")
                ,Camera.class, null);
        tabHost.addTab(tabHost.newTabSpec("DocumentViewer").setIndicator("mp")
                ,DocumentViewer.class, null);
        tabHost.addTab(tabHost.newTabSpec("PreloaderScreen").setIndicator("pl")
                ,PreloaderScreen.class, null);
        tabHost.addTab(tabHost.newTabSpec("WelcomeScreen").setIndicator("ws")
                ,WelcomeScreen.class, null);
        tabHost.addTab(tabHost.newTabSpec("NoConnectionScreen").setIndicator("ncs")
                , NoConnectionScreen.class, null);

        setCurrentTab(TAB.WELCOME_SCREEN);
    }
    private void setCurrentTab      (TAB tab)
    {
        switch (currentTab)
        {
            case CAMERA:
            case VIEWER_3D:
            case INSTRUCTIONS:
                previousTab = currentTab;
                break;

            case DOCUMENTS:
            case PRELOADER:
            case WELCOME_SCREEN:
            case NO_CONNECTION_SCREEN:
                Log.i("setCurrentTab: ","previous tab unchanged");
                break;
        }

        currentTab = tab;
        tabHost.setCurrentTab( tab.index() );
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
                         setCurrentTab(TAB.DOCUMENTS);
                else if ( itemId == quitItemId )
                    closeApp();
                else
                    throw new MikoError ( this
                            , "onNavigationItemSelected"
                            , "Unknown menu item" );

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

    private FragmentTabHost tabHost;
    private NavigationView  menu;
    private DrawerLayout    drawer;
    private HttpConnection  httpConnection;
    private List<TAB>       tabStack;

    private         TAB     currentTab          = TAB.WELCOME_SCREEN
                            , previousTab       = TAB.WELCOME_SCREEN;
    private final   String  serverAddress       = "http://7fc4fc31.ngrok.io"
                            , getModelEndpoint  = "/getModel"
                            , addImageEndpoint  = "/addImage";
}
