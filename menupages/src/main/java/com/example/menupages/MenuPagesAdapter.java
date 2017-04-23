package com.example.menupages;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MenuPagesAdapter extends FragmentPagerAdapter
{
    public MenuPagesAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        return MenuPage.newInstance( position );
    }

    @Override
    public int getCount()
    {
        return 2;
    }
}
