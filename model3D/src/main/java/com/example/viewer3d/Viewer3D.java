package com.example.viewer3d;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.viewer3d.databinding.Viewer3dBinding;

public class Viewer3D
        extends Fragment
{
    public Viewer3D()
    {
        // Required empty public constructor
    }

    public static Viewer3D newInstance()
    {
        Viewer3D fragment = new Viewer3D();
        fragment.setArguments( new Bundle() );

        return fragment;
    }

    @Override
    public void onCreate        (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView    ( LayoutInflater inflater
                                , ViewGroup      container
                                , Bundle         savedInstanceState)
    {
        viewer3d = DataBindingUtil.inflate  ( inflater
                                            , R.layout.viewer_3d
                                            , container
                                            , false );

        //TODO: Repair. Without parent layout margin, sliding menu is hidden.

        return viewer3d.getRoot();
    }
    @Override
    public void onAttach        (Context context)
    {
        super.onAttach(context);
    }
    @Override
    public void onDetach        ()
    {
        super.onDetach();
    }

    private Viewer3dBinding viewer3d;
}
