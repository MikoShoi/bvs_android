package com.infoScreens;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bruce.bvs.R;
import com.example.bruce.bvs.databinding.InfoAnimationBinding;
import com.example.mikotools.MikoLogger;
import com.felipecsl.gifimageview.library.GifImageView;

import java.io.InputStream;

public class InfoAnimation extends Fragment
{
  public static InfoAnimation newInstance (int rIdGif, int rIdText)
  {
    InfoAnimation fragment = new InfoAnimation();

    Bundle bundle = new Bundle();
    bundle.putInt(BUNDLE_PARAM_R_ID_GIF,  rIdGif);
    bundle.putInt(BUNDLE_PARAM_R_ID_TEXT, rIdText);
    fragment.setArguments(bundle);

    return fragment;
  }

  @Override
  public View onCreateView( LayoutInflater  inflater
                          , ViewGroup       container
                          , Bundle          savedInstanceState )
  {
    InfoAnimationBinding infoAnimation
            = DataBindingUtil.inflate(inflater
                                    , R.layout.info_animation
                                    , container
                                    , false);

    Bundle bundle = getArguments();
    if (bundle != null)
    {
      int rIdGif  = bundle.getInt(BUNDLE_PARAM_R_ID_GIF)
        , rIdText = bundle.getInt(BUNDLE_PARAM_R_ID_TEXT);

      setGif(infoAnimation.gif,   rIdGif);
      setTest(infoAnimation.text, rIdText);
    }
    else
    {
      MikoLogger.log("r id's are invalid, did you use newInstance() ?");
    }

    return infoAnimation.getRoot();
  }

  @Override
  public void onStart ()
  {
    super.onStart();

    if (gif != null)
      gif.startAnimation();
  }
  @Override
  public void onStop  ()
  {
    super.onStop();

    if (gif != null)
      gif.stopAnimation();
  }

  private void setTest(TextView textView, int rIdText)
  {
    textView.setText(rIdText);
  }
  private void setGif (GifImageView gifImageView, int rIdGif)
  {
    try
    {
      InputStream stream  = getResources().openRawResource(rIdGif);
      byte[]      data    = new byte[stream.available()];
      stream.read(data);

      gif = gifImageView;
      gif.setBytes(data);
    }
    catch (Exception e)
    {
      MikoLogger.log("can not use loader gif");
    }
  }

  private GifImageView gif = null;
  private static final String BUNDLE_PARAM_R_ID_GIF  = "R_ID_GIF"
                            , BUNDLE_PARAM_R_ID_TEXT = "R_ID_TEXT";
}
