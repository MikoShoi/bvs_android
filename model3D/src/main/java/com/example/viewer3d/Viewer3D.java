package com.example.viewer3d;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.viewer3d.databinding.Viewer3dBinding;
import com.model.Model;
import com.model.ModelData;
import com.model.ModelLoader;

public class Viewer3D
        extends Fragment
{
    public Viewer3D()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate        (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        if ( getArguments() != null )
        {
            int     rIdVertShader   = b.getInt("rIdVertShader")
                    , rIdFragShader = b.getInt("rIdFragShader");
            String  modelFilePath   = b.getString("modelFilePath");

            ModelLoader ml = new ModelLoader( getContext() );
            ModelData   md = ml.load(rIdVertShader, rIdFragShader, modelFilePath);

            model = new Model(md);
        }
    }
    @Override
    public View onCreateView    ( LayoutInflater inflater
                                , ViewGroup      container
                                , Bundle         savedInstanceState)
    {
        Viewer3dBinding viewer3d
                = DataBindingUtil.inflate  ( inflater
                                            , R.layout.viewer_3d
                                            , container
                                            , false );
        if ( model != null )
        {
            viewer3d.previewSurface.renderEngine.addModel(model);

            //TODO: Without parent layout margin, sliding menu is hidden.
            //TODO: I don't like it. That first thing to improved
        }

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

    private Model model = null;
}
