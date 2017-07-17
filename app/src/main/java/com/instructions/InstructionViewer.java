package com.instructions;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    viewPager   = layout.viewPager;
    dotsLayout  = layout.dots;
    nextButton  = layout.buttonNext;
    skipButton  = layout.buttonSkip;

    setViewPager();
    setBottomDots();

    prepareSkipButton();
    prepareNextButton();

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

  @Override
  public void onResume ()
  {
    super.onResume();

    System.out.println("\n\t ---- Instruction:onResume\n");
  }

  @Override
  public void onPause ()
  {
    super.onPause();

    System.out.println("\n\t ---- Documents:onPause\n");
  }

  private void setViewPager             ()
  {
    //--counting from zero for 4 pages, first one has number 0 and last one is 3
    //--special counting method for simpler manipulate arrays
    //--LAST and FIRST are in array notation, NUMBER_OF_PAGES is in normal notation
    NUMBER_OF_PAGES   = 4;
    LAST_PAGE_NUMBER  = NUMBER_OF_PAGES - 1;

    InstructionViewerAdapter adapter = new InstructionViewerAdapter( getChildFragmentManager() );
    ViewPager.OnPageChangeListener l = new ViewPager.OnPageChangeListener()
    {
      @Override
      public void onPageScrolled          (int position, float positionOffset, int positionOffsetPixels)
      {
        CURRENT_PAGE_NUMBER = position;

        setBottomDots();
        prepareButtonsAppearance( CURRENT_PAGE_NUMBER == LAST_PAGE_NUMBER );
      }
      @Override
      public void onPageSelected          (int position)
      {

      }
      @Override
      public void onPageScrollStateChanged(int state)
      {

      }
    };

    viewPager.setAdapter(adapter);
    viewPager.addOnPageChangeListener(l);
  }
  private void prepareNextButton        ()
  {
    View.OnClickListener l = new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if (CURRENT_PAGE_NUMBER < LAST_PAGE_NUMBER)
            goToNextPage();
        else
          listener.onInstructionsViewed();
      }
    };

    nextButton.setOnClickListener(l);
  }
  private void prepareSkipButton        ()
  {
    View.OnClickListener l = new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        listener.onInstructionsViewed();
      }
    };

    skipButton.setOnClickListener(l);
  }
  private void prepareButtonsAppearance (boolean lastPage)
  {
    nextButton.setText      ( lastPage ? R.string.start : R.string.next );
    skipButton.setVisibility( lastPage ? View.GONE : View.VISIBLE);
  }
  private void goToNextPage             ()
  {
    viewPager.setCurrentItem(CURRENT_PAGE_NUMBER + 1);
  }
  private void setBottomDots            ()
  {
    TextView dots[] = new TextView[NUMBER_OF_PAGES];
    int dotsNumber = dots.length;

    int[] colorsActive   = getResources().getIntArray(R.array.array_dot_active)
        , colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

    dotsLayout.removeAllViews();
    for (int i = 0; i < dotsNumber; i++)
    {
        dots[i] = new TextView(getContext());
        dots[i].setText(Html.fromHtml("&#8226;"));
        dots[i].setTextSize(35);
        dots[i].setTextColor(colorsInactive[CURRENT_PAGE_NUMBER]);
        dotsLayout.addView(dots[i]);
    }

    if ( dotsNumber > 0 )
        dots[CURRENT_PAGE_NUMBER].setTextColor(colorsActive[CURRENT_PAGE_NUMBER]);
  }

  private InstructionViewerListener listener;

  private Button        nextButton, skipButton;
  private ViewPager     viewPager;
  private LinearLayout  dotsLayout;

  private int NUMBER_OF_PAGES
            , LAST_PAGE_NUMBER
            , CURRENT_PAGE_NUMBER;
}