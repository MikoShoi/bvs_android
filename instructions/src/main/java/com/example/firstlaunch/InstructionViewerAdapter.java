package com.example.firstlaunch;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class InstructionViewerAdapter extends FragmentPagerAdapter
{
    public InstructionViewerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        return Instruction.newInstance( position );
    }

    @Override
    public int getCount()
    {
        return 4;
    }
}
