package com.infoScreens;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bruce.bvs.R;
import com.example.bruce.bvs.databinding.InfoImageBinding;

public class InfoImage extends Fragment
{
  public InfoImage ()
  {
    // Required empty public constructor
  }

  public static InfoImage newInstance (int rIdImage)
  {
    InfoImage fragment = new InfoImage();
    Bundle args = new Bundle();
    args.putInt(BUNDLE_PARAM_R_ID_IMAGE, rIdImage);
    fragment.setArguments(args);

    return fragment;
  }

  @Override
  public void onCreate    ( Bundle savedInstanceState )
  {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView( LayoutInflater  inflater
                          , ViewGroup       container
                          , Bundle          savedInstanceState )
  {
    InfoImageBinding infoImage = DataBindingUtil.inflate( inflater
                                                        , R.layout.info_image
                                                        , container
                                                        , false);

    if (getArguments() != null)
    {
      int rIdImage = getArguments().getInt(BUNDLE_PARAM_R_ID_IMAGE);

      Drawable image = ContextCompat.getDrawable(getContext(), rIdImage);
      infoImage.image.setImageDrawable(image);
    }
    else
      Log.i( "InfoImage: "
            ," r id image is invalid, did you use newInstance()?");

    return infoImage.getRoot();
  }

  private final static String BUNDLE_PARAM_R_ID_IMAGE = "R_ID_IMAGE";
}
