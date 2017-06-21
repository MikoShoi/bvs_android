package com.example.bruce.miko_mk10;

import android.content.Intent;
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

import com.example.bruce.miko_mk10.databinding.ActivityMainBinding;
import com.example.camera.CameraInterface;
import com.example.camera.CameraPage;
import com.example.eventbusmessages.SendFileMessage;
import com.example.eventbusmessages.SendGetModelMessage;
import com.example.firstlaunch.FirstLaunch;
import com.example.firstlaunch.FirstLaunchInterface;
import com.example.menupages.MenuPages;
import com.example.menupages.MenuPagesInterface;
import com.example.networkcontroller.NetworkController;
import com.example.viewer3d.Viewer3D;

import org.greenrobot.eventbus.EventBus;

public class MainActivity
        extends AppCompatActivity
        implements  FirstLaunchInterface
                    ,MenuPagesInterface
                    ,CameraInterface
{
    private FragmentTabHost tabHost;
    private NavigationView  menu;
    private DrawerLayout    drawer;

    private EventBus        eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventBus = EventBus.getDefault();
        initNetworkConnection();

        prepareUiElements();
        prepareSlidingMenu();
        prepareTabHost();

        if( new AppConfigManager(this).isAppFirstTimeLaunch() )
            setCurrentTab(TAB.FIRST_LAUNCH);
        else
            setCurrentTab(TAB.VIEWER_3D);
    }

    @Override
    public void firstLaunchCompleted()
    {
        setCurrentTab(TAB.VIEWER_3D);
    }
    @Override
    public void menuPagesCompleted()
    {
        Log.i("MenuPagesCompleted","Mam");
        tabHost.setCurrentTab( tabHost.getCurrentTab() - 1 );
    }
    @Override
    public void cameraDoneEventHandle()
    {
        eventBus.postSticky( new SendGetModelMessage() );
        Log.i(  "\n\n------ MainActivity" ,"- wysyłam request GET");
    }

    @Override
    public void cameraCaptureEventHandle(String absoluteFilePath)
    {
    //--tell networkController service to send captured image to server
        eventBus.postSticky( new SendFileMessage(absoluteFilePath) );
    }

    //--init
    private void prepareUiElements()
    {
        ActivityMainBinding mainActivity =
                DataBindingUtil.setContentView(this, R.layout.activity_main);

        tabHost = mainActivity.tabhost;
        menu    = mainActivity.menu;
        drawer  = mainActivity.drawer;
    }
    private void initNetworkConnection()
    {
        Intent i = new Intent(this, NetworkController.class);
        i.putExtra(NetworkController.intentParamServerAddress,  "http://8c73fdbd.ngrok.io");
        i.putExtra(NetworkController.intentParamGetEndpoint,    "/getModel");
        i.putExtra(NetworkController.intentParamPostEndpoint,   "/addImage");
        startService(i);
    }

    private void prepareTabHost()
    {
        tabHost.setup(this, getSupportFragmentManager(), R.id.tabhost);
        tabHost.getTabWidget().setVisibility(View.GONE);

        tabHost.addTab(tabHost.newTabSpec("FirstLaunch").setIndicator("fl")
                ,FirstLaunch.class, null);
        tabHost.addTab(tabHost.newTabSpec("CameraPage").setIndicator("cp")
                ,CameraPage.class, null);
        tabHost.addTab(tabHost.newTabSpec("MenuPages").setIndicator("mp")
                ,MenuPages.class, null);
        tabHost.addTab(tabHost.newTabSpec("Viewer3D").setIndicator("v3D")
                ,Viewer3D.class, null);
    }
    private void setCurrentTab(TAB TAB)
    {
        int pageId;

        switch (TAB)
        {
            case FIRST_LAUNCH:
                pageId = 0;
                break;
            case CAMERA:
                pageId = 1;
                break;
            case DOCUMENTS:
                pageId = 2;
                break;
            case VIEWER_3D:
                pageId = 3;
                break;
            default:
                pageId = 0;
                break;
        }
        tabHost.setCurrentTab( pageId );
    }

    private void prepareSlidingMenu()
    {
        menu.setNavigationItemSelectedListener( getMenuItemSelectedListener() );
    }
    private OnNavigationItemSelectedListener getMenuItemSelectedListener()
    {
        return new OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                final int docsItemId = R.id.nav_item_documentation;
                final int quitItemId = R.id.nav_item_quit;

                switch (item.getItemId())
                {
                    case docsItemId:
                        Log.i("-","documentation");
                        setCurrentTab(TAB.DOCUMENTS);
                        break;
                    case quitItemId:
                        Log.i("-","quit");
                        finish();
                        System.exit(0);
                        break;
                    default:
                        Log.i("-","nie wiem co to");
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        };
    }

}
