package com.infoScreens;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bruce.bvs.R;
import com.example.bruce.bvs.databinding.InfoImageBinding;
import com.example.mikotools.MikoLogger;

public class InfoImage extends Fragment
{
  public static InfoImage newInstance (int rIdImage)
  {
    InfoImage fragment = new InfoImage();

    Bundle bundle = new Bundle();
    bundle.putInt(BUNDLE_R_ID_IMAGE, rIdImage);
    fragment.setArguments(bundle);

    return fragment;
  }

  @Override
  public View onCreateView( LayoutInflater  inflater
                          , ViewGroup       container
                          , Bundle          savedInstanceState )
  {
    InfoImageBinding infoImage = DataBindingUtil.inflate( inflater
                                                        , R.layout.info_image
                                                        , container
                                                        , false );
    Bundle bundle = getArguments();
    if (bundle != null)
    {
      Drawable image = ContextCompat
              .getDrawable( getContext()
                          , bundle.getInt(BUNDLE_R_ID_IMAGE) );

      infoImage.image.setImageDrawable(image);
    }
    else
    {
      MikoLogger.log("r id is invalid, did you use newInstance() ?");
    }

    return infoImage.getRoot();
  }

  private final static String BUNDLE_R_ID_IMAGE = "R_ID_IMAGE";
}
