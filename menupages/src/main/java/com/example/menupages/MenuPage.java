package com.example.menupages;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.example.menupages.databinding.MenuPageBinding;
import com.example.menupages.databinding.MenuPagesBinding;

public class MenuPage extends Fragment
{
    private Activity mainActivity;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public MenuPage()
    {}
    public static MenuPage newInstance(int sectionNumber)
    {
        MenuPage fragment = new MenuPage();
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
        MenuPageBinding firstLaunchPage =
                DataBindingUtil.inflate(inflater, R.layout.menu_page, container, false);

        //--get number of page, which you will customize
        int pageNumber = this.getArguments().getInt(ARG_SECTION_NUMBER);

        //--set for this page title
        firstLaunchPage.textViewHeader.setText( getTitle(pageNumber) );

        //--, description
        firstLaunchPage.textViewDescription.setMovementMethod( new ScrollingMovementMethod() );
        firstLaunchPage.textViewDescription.setText( Html.fromHtml( getDescription(pageNumber) ) );

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
                descriptionId = R.string.about_description;
                break;
            case 1:
                descriptionId = R.string.privacy_policy_description;
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
                titleId = R.string.about_title;
                break;
            case 1:
                titleId = R.string.privacy_policy_title;
                break;
            default:
                titleId = R.string.default_title;
                break;
        }

        return getResources().getString(titleId);
    }
}
