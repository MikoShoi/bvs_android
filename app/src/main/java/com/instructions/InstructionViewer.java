package com.instructions;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.bruce.bvs.R;
import com.example.bruce.bvs.databinding.InstructionViewerBinding;

public class InstructionViewer extends Fragment
{
  @Override
  public View onCreateView  ( LayoutInflater inflater
                            , ViewGroup container
                            , Bundle savedInstanceState )
  {
    InstructionViewerBinding layout
            = DataBindingUtil.inflate( inflater
                                      , R.layout.instruction_viewer
                                      , container
                                      , false);

    setAppearance(layout.viewPager, layout.prevButton);

    return layout.getRoot();
  }
  @Override
  public void onAttach      (Context context)
  {
    super.onAttach(context);

    if (context instanceof InstructionViewerListener)
      listener = (InstructionViewerListener) context;
    else
      throw new RuntimeException( "parent object must implement " +
                                  "InstructionViewerListener interface" );
  }
  @Override
  public void onDetach      ()
  {
      super.onDetach();
      listener = null;
  }

  private void setAppearance (ViewPager viewPager, final ImageButton prevButton)
  {
    prevButton.setVisibility(View.INVISIBLE);

    ViewPager.OnPageChangeListener l = new ViewPager.OnPageChangeListener()
    {
      @Override
      public void onPageScrolled          ( int   position
                                          , float positionOffset
                                          , int   positionOffsetPixels)
      {
        if (CURRENT_PAGE_INDEX == 2 && positionOffsetPixels == 0)
          listener.onInstructionsViewed();

        CURRENT_PAGE_INDEX = position;
      }
      @Override
      public void onPageSelected          (int position)
      {
        prevButton.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
      }
      @Override
      public void onPageScrollStateChanged(int state)
      {
      }
    };

    InstructionViewerAdapter adapter
            = new InstructionViewerAdapter( getChildFragmentManager() );
    viewPager.setAdapter(adapter);
    viewPager.addOnPageChangeListener(l);
  }

  private InstructionViewerListener listener;
  private int CURRENT_PAGE_INDEX = 0;
}