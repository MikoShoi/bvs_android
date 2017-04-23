package com.example.bruce.miko_mk10;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.bruce.miko_mk10.databinding.ActivityMainBinding;
import com.example.firstlaunch.FirstLaunch;
import com.example.firstlaunch.FirstLaunchInterface;
import com.example.menupages.MenuPages;
import com.example.menupages.MenuPagesInterface;

public class MainActivity
        extends AppCompatActivity
        implements  FirstLaunchInterface
                    ,MenuPagesInterface
{
    FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideAppBar();
        setTabHost();
    }

    @Override
    public void firstLaunchCompleted()
    {
        Log.i("FirstLaunchCompleted","Mam");
        tabHost.setCurrentTab( tabHost.getCurrentTab() + 1 );
    }

    @Override
    public void menuPagesCompleted()
    {
        Log.i("MenuPagesCompleted","Mam");
    }

    private void hideAppBar()
    {
        getSupportActionBar().hide();
    }
    private void setTabHost()
    {
        ActivityMainBinding mainActivity =
                DataBindingUtil.setContentView(this, R.layout.activity_main);

        tabHost = mainActivity.tahHost;

    //--setup tabHost
        tabHost.setup(this, getSupportFragmentManager(), R.layout.tab_host_content);

    //--hide tab bar
        tabHost.getTabWidget().setVisibility(View.GONE);

        //--add tab
        tabHost.addTab(tabHost.newTabSpec("FirstLaunch").setIndicator("fl")
                ,FirstLaunch.class, null);
        tabHost.addTab(tabHost.newTabSpec("MenuPages").setIndicator("mp")
                ,MenuPages.class, null);
    }
}
