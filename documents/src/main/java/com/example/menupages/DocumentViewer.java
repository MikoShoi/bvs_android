package com.example.menupages;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.handytools.MikoError;
import com.example.menupages.databinding.DocumentViewerBinding;

public class DocumentViewer extends Fragment
{
    public DocumentViewer()
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
        DocumentViewerBinding layout = DataBindingUtil.inflate( inflater
                , R.layout.document_viewer
                , container
                , false );

        closeFab  = layout.fab;
        viewPager = layout.viewPager;

        setViewPager();
        setFabOnClickListener();

        return layout.getRoot();
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if (context instanceof AppCompatActivity)
        {
            Log.i("DocumentViewer ","onAttach a");
            AppCompatActivity activity = (AppCompatActivity)context;
        }
        else
        {
            Log.i("DocumentViewer ","onAttach b");
        }

        if (context instanceof DocumentViewerInterface)
            parentObject = (DocumentViewerInterface) context;
        else
            throw new MikoError(this, "onAttach", "parent object does not implement needed interface");
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
        parentObject = null;
    }

    private void setViewPager           ()
    {
        DocumentViewerAdapter adapter = new DocumentViewerAdapter( getChildFragmentManager() );
        viewPager.setAdapter(adapter);
    }
    private void setFabOnClickListener  ()
    {
        closeFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                closeActivity();
            }
        });
    }
    private void closeActivity          ()
    {
        parentObject.onDocumentViewerCompletedHandle();
    }

    private FloatingActionButton    closeFab;
    private ViewPager               viewPager;
    private DocumentViewerInterface parentObject;
}
