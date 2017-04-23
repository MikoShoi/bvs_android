package com.example.firstlaunch;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.database.DatabaseUtilsCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.firstlaunch.databinding.FirstLaunchBinding;
import com.example.firstlaunch.databinding.FirstLaunchPageBinding;

import java.util.concurrent.PriorityBlockingQueue;

public class FirstLaunchPage extends Fragment
{
    private Activity mainActivity;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public FirstLaunchPage()
    {}
    public static FirstLaunchPage newInstance(int sectionNumber)
    {
        FirstLaunchPage fragment = new FirstLaunchPage();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater
            ,ViewGroup container
            ,Bundle savedInstanceState)
    {
        //--inflate
        FirstLaunchPageBinding firstLaunchPage =
                DataBindingUtil.inflate(inflater, R.layout.first_launch_page, container, false);

        //--get number of page, which you will customize
        int pageNumber = this.getArguments().getInt(ARG_SECTION_NUMBER);

        //--set for this page title
        firstLaunchPage.textViewTitle.setText( getTitle(pageNumber) );

        //--, description
        firstLaunchPage.textViewDescription.setText( getDescription(pageNumber) );

        //--, background color
        firstLaunchPage.layout.setBackgroundColor( getColor(pageNumber) );

        //--and icon
        firstLaunchPage.imageViewIcon.setImageDrawable( getIcon(pageNumber) );

        return firstLaunchPage.getRoot();
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        mainActivity = getActivity();
    }
    @Override
    public void onDetach()
    {
        super.onDetach();

        mainActivity = null;
    }


    private String      getDescription  (int pageNumber)
    {
        int descriptionId;

        switch (pageNumber)
        {
            case 0:
                descriptionId = R.string.welcome_description;
                break;
            case 1:
                descriptionId = R.string.help_description;
                break;
            case 2:
                descriptionId = R.string.capture_images_description;
                break;
            case 3:
                descriptionId = R.string.enjoy_description;
                break;
            default:
                descriptionId = R.string.default_description;
                break;
        }

        return getResources().getString(descriptionId);
    }
    private String      getTitle        (int pageNumber)
    {
        int titleId;

        switch (pageNumber)
        {
            case 0:
                titleId = R.string.welcome_title;
                break;
            case 1:
                titleId = R.string.help_title;
                break;
            case 2:
                titleId = R.string.capture_images_title;
                break;
            case 3:
                titleId = R.string.enjoy_title;
                break;
            default:
                titleId = R.string.default_title;
                break;
        }

        return getResources().getString(titleId);
    }
    private int         getColor        (int pageNumber)
    {
        //--color variable
        int colorId;

        //--assign get proper color for each page
        switch (pageNumber)
        {
            case 0:
                colorId = R.color.bg_screen1;
                break;
            case 1:
                colorId = R.color.bg_screen2;
                break;
            case 2:
                colorId = R.color.bg_screen3;
                break;
            case 3:
                colorId = R.color.bg_screen4;
                break;
            default:
                colorId = R.color.default_background;
                break;
        }

        //--return this color
        return ContextCompat.getColor(mainActivity, colorId);
    }
    private Drawable    getIcon         (int pageNumber)
    {
        int iconId;

        switch (pageNumber)
        {
            case 0:
                iconId = R.drawable.happy;
                break;
            case 1:
                iconId = R.drawable.info_squared;
                break;
            case 2:
                iconId = R.drawable.camera;
                break;
            case 3:
                iconId = R.drawable.mesh;
                break;
            default:
                iconId = R.drawable.default_icon;
                break;
        }

        return ContextCompat.getDrawable(mainActivity, iconId);
    }
}
