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
import android.view.MenuItem;
import android.view.View;

import com.example.bruce.miko_mk10.databinding.MainBinding;
import com.example.camera.Camera;
import com.example.camera.CameraInterface;
import com.example.firstlaunch.InstructionViewerInterface;
import com.example.handytools.MikoError;
import com.example.handytools.Preloader;
import com.example.menupages.DocumentViewer;
import com.example.networkcontroller.FileDownloadedMessage;
import com.example.networkcontroller.ServerConnection;
import com.example.networkcontroller.UploadFileMessage;
import com.example.networkcontroller.DownloadFileMessage;
import com.example.firstlaunch.InstructionViewer;
import com.example.handytools.AppConfigManager;
import com.example.menupages.DocumentViewerInterface;
import com.example.viewer3d.Viewer3D;
import com.model.AddModelToRenderMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity
        extends AppCompatActivity
        implements InstructionViewerInterface
                    ,DocumentViewerInterface
                    ,CameraInterface
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        eventBus = EventBus.getDefault();
        eventBus.register(this);
        initNetworkConnection();

        prepareUiElements();
        prepareSlidingMenu();
        prepareTabHost();

        if( new AppConfigManager(this).isAppFirstTimeLaunch() )
            setCurrentTab(TAB.FIRST_LAUNCH);
        else
            setCurrentTab(TAB.CAMERA);
    }

    @Override
    public void onInstructionViewerCompletedHandle()
    {
        setCurrentTab(TAB.CAMERA);
    }
    @Override
    public void onDocumentViewerCompletedHandle()
    {
        tabHost.setCurrentTab(previousTabIndex);
    }
    @Override
    public void onShootingFinishedHandle()
    {
        setCurrentTab(TAB.PRELOADER);

        eventBus.postSticky( new DownloadFileMessage() );
    }
    @Override
    public void onCapturedHandle(String absoluteFilePath)
    {
        eventBus.postSticky( new UploadFileMessage(absoluteFilePath) );
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FileDownloadedMessage event)
    {
        AddModelToRenderMessage msg;
        msg = new AddModelToRenderMessage   ( R.raw.vertex_shader
                                            , R.raw.fragment_shader
//                                            , event.getAbsFilePath()
                                            , "/storage/emulated/0/Download/coordinates.off"
                                            , "uniqueName" );
        eventBus.postSticky(msg);
        setCurrentTab(TAB.VIEWER_3D);
    }

    private void prepareUiElements()
    {
        MainBinding mainActivity =
                DataBindingUtil.setContentView(this, R.layout.main);

        tabHost = mainActivity.tabhost;
        menu    = mainActivity.menu;
        drawer  = mainActivity.drawer;
    }
    private void initNetworkConnection()
    {
        Intent i = new Intent(this, ServerConnection.class);

        //check connection

        startService(i);
    }
    private void prepareTabHost()
    {
        tabHost.setup(this, getSupportFragmentManager(), R.id.tabhost);
        tabHost.getTabWidget().setVisibility(View.GONE);

        tabHost.addTab(tabHost.newTabSpec("InstructionViewer").setIndicator("fl")
                ,InstructionViewer.class, null);
        tabHost.addTab(tabHost.newTabSpec("Camera").setIndicator("cp")
                ,Camera.class, null);
        tabHost.addTab(tabHost.newTabSpec("DocumentViewer").setIndicator("mp")
                ,DocumentViewer.class, null);
        tabHost.addTab(tabHost.newTabSpec("Viewer3D").setIndicator("v3D")
                ,Viewer3D.class, null);
        tabHost.addTab(tabHost.newTabSpec("Preloader").setIndicator("pl")
                ,Preloader.class, null);
    }
    private void setCurrentTab(TAB tab)
    {
        previousTabIndex = previousTabIndex == -1 ? tab.index() : tabHost.getCurrentTab();

        tabHost.setCurrentTab( tab.index() );
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
                final int docsItemId = R.id.nav_item_documents;
                final int quitItemId = R.id.nav_item_quit;

                switch (item.getItemId())
                {
                    case docsItemId:
                        setCurrentTab(TAB.DOCUMENTS);
                        break;
                    case quitItemId:
                        finish();
                        System.exit(0);
                        break;
                    default:
                        throw new MikoError(this
                                            , "onNavigationItemSelected"
                                            , "Unknown menu item" );
                }
                drawer.closeDrawer(GravityCompat.START);
                item.setChecked(false);

                //TODO: repair. deselect sliding menu item

                return true;
            }
        };
    }

    private FragmentTabHost tabHost;
    private NavigationView  menu;
    private DrawerLayout    drawer;
    private EventBus        eventBus;

    int previousTabIndex = -1;
}
