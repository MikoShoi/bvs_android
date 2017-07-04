package com.example.viewer3d;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

  public static Viewer3D newInstance (String  modelFilePath
                                    , int     rIdVertexShader
                                    , int     rIdFragmentShader)
  {
    Viewer3D fragment = new Viewer3D();

    Bundle args = new Bundle();
    args.putString(BUNDLE_PARAM_MODEL_FILE_PATH,    modelFilePath);
    args.putInt(BUNDLE_PARAM_R_ID_VERTEX_SHADER,    rIdVertexShader);
    args.putInt(BUNDLE_PARAM_R_ID_FRAGMENT_SHADER,  rIdFragmentShader);
    fragment.setArguments(args);

    return fragment;
  }

  @Override
  public void onCreate    (Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
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
    Bundle b = getArguments();
    if (b != null)
    {
      String  modelFilePath = b.getString(BUNDLE_PARAM_MODEL_FILE_PATH);
      int     rIdVertShader = b.getInt(BUNDLE_PARAM_R_ID_VERTEX_SHADER)
            , rIdFragShader = b.getInt(BUNDLE_PARAM_R_ID_FRAGMENT_SHADER);

      ModelLoader ml = new ModelLoader( getActivity() );
      ModelData   md = ml.load(rIdVertShader, rIdFragShader, modelFilePath);

      viewer3d.previewSurface.show( new Model(md) );
    }
    else
      Log.i("Viewer3D:\t"
            ,"bundle params are invalid. Did you use newInstance()?");

      //-- TODO: Without parent layout margin, sliding menu is hidden.
      //-- TODO: Model loading is no suitable. It must be changed

    return viewer3d.getRoot();
  }

  @Override
  public void onAttach    (Context context)
  {
    super.onAttach(context);
  }
  @Override
  public void onDetach    ()
  {
    super.onDetach();
  }

  private static final String BUNDLE_PARAM_MODEL_FILE_PATH      = "MODEL_FILE_PATH"
                            , BUNDLE_PARAM_R_ID_VERTEX_SHADER   = "R_ID_VERTEX_SHADER"
                            , BUNDLE_PARAM_R_ID_FRAGMENT_SHADER = "R_ID_FRAGMENT_SHADER";
}
