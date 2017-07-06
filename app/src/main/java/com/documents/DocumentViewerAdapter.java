package com.documents;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class DocumentViewerAdapter extends FragmentPagerAdapter
{
  DocumentViewerAdapter(FragmentManager fm)
  {
    super(fm);
  }

  @Override
  public Fragment getItem   (int position)
  {
    return Document.newInstance( position );
  }
  @Override
  public int      getCount  ()
  {
    return 2;
  }
}
