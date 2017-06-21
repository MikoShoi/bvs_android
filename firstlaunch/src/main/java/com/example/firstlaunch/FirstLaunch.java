package com.example.firstlaunch;

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

import com.example.firstlaunch.databinding.FirstLaunchBinding;

public class FirstLaunch extends Fragment
{
    public FirstLaunch()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater
            , ViewGroup container
            , Bundle savedInstanceState)
    {
        FirstLaunchBinding layout = DataBindingUtil.inflate( inflater
                                                            ,R.layout.first_launch
                                                            ,container
                                                            ,false);

        viewPager   = layout.viewPager;
        dotsLayout  = layout.dots;
        nextButton  = layout.buttonNext;
        skipButton  = layout.buttonSkip;

        setViewPager();
        setBottomDots();

        setSkipButtonOnClickListener();
        setNextButtonOnClickListener();

        return layout.getRoot();
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if (context instanceof OnFirstLaunchCompletedHandle)
            parentObject = (OnFirstLaunchCompletedHandle) context;
        else
            //--viewPager must implement OnFirstLaunchCompletedHandle
            throw new Error("\n\n-------\tError source:\tFirstLaunch:onAttach");
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
        parentObject = null;
    }

    private void setViewPager()
    {
        //--counting from zero for 4 pages, first one has number 0 and last one is 3
        //--special counting method for simpler manipulate arrays
        //--LAST and FIRST are in array notation, NUMBER_OF_PAGES is in normal notation
        NUMBER_OF_PAGES     = 4;
        LAST_PAGE_NUMBER    = NUMBER_OF_PAGES - 1;

        FirstLaunchAdapter adapter = new FirstLaunchAdapter( getChildFragmentManager() );
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener( getViewPagerPageChangeListener() );
    }
    private ViewPager.OnPageChangeListener getViewPagerPageChangeListener()
    {
        return new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageSelected          (int position)
            {
                CURRENT_PAGE_NUMBER = position;

                setBottomDots();
                setButtonsAppearanceForPage( CURRENT_PAGE_NUMBER == LAST_PAGE_NUMBER );
            }
            @Override
            public void onPageScrolled          (int arg0, float arg1, int arg2)
            {}
            @Override
            public void onPageScrollStateChanged(int arg0)
            {}
        };
    }

    private void setNextButtonOnClickListener()
    {
        nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (CURRENT_PAGE_NUMBER < LAST_PAGE_NUMBER)
                    goToNextPage();
                else
                    closeActivity();
            }
        });
    }
    private void setSkipButtonOnClickListener()
    {
        skipButton.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                closeActivity();
            }
        });
    }
    private void setButtonsAppearanceForPage(boolean lastPage)
    {
        nextButton.setText      ( lastPage ? R.string.start : R.string.next );
        skipButton.setVisibility( lastPage ? View.GONE : View.VISIBLE);
    }
    private void goToNextPage()
    {
        viewPager.setCurrentItem(CURRENT_PAGE_NUMBER + 1);
    }

    private void setBottomDots()
    {
        TextView dots[] = new TextView[NUMBER_OF_PAGES];
        final int dotsNumber = dots.length;

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

    private void closeActivity()
    {
        parentObject.firstLaunchCompleted();
    }

    private OnFirstLaunchCompletedHandle parentObject;

    private Button               nextButton, skipButton;
    private ViewPager            viewPager;
    private LinearLayout         dotsLayout;

    private int NUMBER_OF_PAGES
                , LAST_PAGE_NUMBER
                , CURRENT_PAGE_NUMBER;
}
