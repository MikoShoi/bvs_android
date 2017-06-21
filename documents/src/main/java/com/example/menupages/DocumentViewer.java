package com.example.menupages;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.menupages.databinding.MenuPagesBinding;

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
        MenuPagesBinding layout = DataBindingUtil.inflate( inflater
                ,R.layout.menu_pages
                ,container
                ,false);

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

        if (context instanceof DocumentVIewerInterface)
            parentObject = (DocumentVIewerInterface) context;
        else
            throw new Error("\n\n-------\tError source:\tDocumentViewer:onAttach");
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
        parentObject.menuPagesCompleted();
    }

    private FloatingActionButton    closeFab;
    private ViewPager               viewPager;
    private DocumentVIewerInterface parentObject;
}
