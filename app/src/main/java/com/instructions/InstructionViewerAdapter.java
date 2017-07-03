package com.instructions;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class InstructionViewerAdapter extends FragmentPagerAdapter
{
  InstructionViewerAdapter(FragmentManager fm)
  {
    super(fm);
  }

  @Override
  public Fragment getItem   (int position)
  {
    return Instruction.newInstance(position);
  }
  @Override
  public int      getCount  ()
  {
    return 4;
  }
}
