package com.infoScreens;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.bruce.bvs.R;
import com.example.bruce.bvs.databinding.InfoImageBinding;
import com.example.mikotools.MikoLogger;

public class InfoImage extends Fragment
{
  public static InfoImage newInstance (int rIdHImage, int rIdVImage)
  {
    InfoImage fragment = new InfoImage();

    Bundle bundle = new Bundle();
    bundle.putInt(BUNDLE_R_ID_H_IMG, rIdHImage);
    bundle.putInt(BUNDLE_R_ID_V_IMG, rIdVImage);
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
    imageView = infoImage.image;

    Bundle bundle = getArguments();
    if (bundle != null)
    {
      rIdHImage = bundle.getInt(BUNDLE_R_ID_H_IMG);
      rIdVImage = bundle.getInt(BUNDLE_R_ID_V_IMG);

      setSuitableImage();
    }
    else
    {
      MikoLogger.log("r id is invalid, did you use newInstance() ?");
    }

    return infoImage.getRoot();
  }

  @Override
  public void onConfigurationChanged (Configuration newConfig)
  {
    super.onConfigurationChanged(newConfig);

    setSuitableImage();
  }

  private void setSuitableImage()
  {
    int rotation = getActivity()
                    .getWindowManager()
                    .getDefaultDisplay()
                    .getRotation();

    int rIdImage;
    if ( rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180)
    {
      rIdImage = rIdVImage;
    }
    else
    {
      rIdImage = rIdHImage;
    }

    Drawable image = ContextCompat.getDrawable(getContext(), rIdImage);
    imageView.setImageDrawable(image);
  }

  private int rIdHImage, rIdVImage;
  private ImageView imageView;

  private final static String BUNDLE_R_ID_H_IMG = "R_ID_H_IMAGE"
                            , BUNDLE_R_ID_V_IMG = "R_ID_V_IMAGE";
}
