package com.example.viewer3d;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mikotools.MikoLogger;
import com.example.viewer3d.databinding.Viewer3dBinding;
import com.model.Model;

public class Viewer3D
        extends Fragment
{
  public static Viewer3D newInstance (String modelFilePath)
  {
    Viewer3D fragment = new Viewer3D();

    Bundle bundle = new Bundle();
    bundle.putString(BUNDLE_PARAM_MODEL_FILE_PATH, modelFilePath);
    fragment.setArguments(bundle);

    return fragment;
  }

  @Override
  public View onCreateView( LayoutInflater inflater
                          , ViewGroup      container
                          , Bundle         savedInstanceState)
  {
    Viewer3dBinding viewer3d = DataBindingUtil.inflate( inflater
                                                      , R.layout.viewer_3d
                                                      , container
                                                      , false);
    Bundle bundle = getArguments();
    if (bundle != null)
    {
      Model model = new Model( bundle.getString(BUNDLE_PARAM_MODEL_FILE_PATH) );
      viewer3d.previewSurface.show(model);
    }
    else
    {
      MikoLogger.log("bundle params are invalid. Did you use newInstance() ?");
    }

    return viewer3d.getRoot();
  }

  private static final String BUNDLE_PARAM_MODEL_FILE_PATH = "MODEL_FILE_PATH";
}

//-- TODO: Without parent layout margin, sliding menu is hidden.