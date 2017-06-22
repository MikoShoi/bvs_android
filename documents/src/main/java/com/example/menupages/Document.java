package com.example.menupages;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.menupages.databinding.DocumentBinding;

public class Document extends Fragment
{
    public Document()
    {}
    public static Document newInstance(int sectionNumber)
    {
        Document fragment = new Document();
        Bundle   args     = new Bundle();
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
        DocumentBinding document =
                DataBindingUtil.inflate(inflater, R.layout.document, container, false);

        //--get number of page, which you will customize
        int pageNumber = this.getArguments().getInt(ARG_SECTION_NUMBER);

        //--set for this page title
        document.textViewHeader.setText( getTitle(pageNumber) );

        //--, description
        document.textViewDescription.setMovementMethod( new ScrollingMovementMethod() );
        document.textViewDescription.setText( Html.fromHtml( getDescription(pageNumber) ) );

        return document.getRoot();
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

    private Activity mainActivity;
    private static final String ARG_SECTION_NUMBER = "section_number";
}
