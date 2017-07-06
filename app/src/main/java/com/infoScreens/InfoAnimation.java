package com.infoScreens;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bruce.bvs.R;
import com.example.bruce.bvs.databinding.InfoAnimationBinding;
import com.felipecsl.gifimageview.library.GifImageView;

import java.io.InputStream;

public class InfoAnimation extends Fragment
{
  public InfoAnimation ()
  {
    // Required empty public constructor
  }

  public static InfoAnimation newInstance (int rIdGif, int rIdText)
  {
    InfoAnimation fragment = new InfoAnimation();

    Bundle args = new Bundle();
    args.putInt(BUNDLE_PARAM_R_ID_GIF,  rIdGif);
    args.putInt(BUNDLE_PARAM_R_ID_TEXT, rIdText);
    fragment.setArguments(args);

    return fragment;
  }

  @Override
  public void onCreate    ( Bundle savedInstanceState )
  {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView( LayoutInflater inflater
                          , ViewGroup container
                          , Bundle savedInstanceState )
  {
    InfoAnimationBinding infoAnimation
            = DataBindingUtil.inflate(inflater
                                    , R.layout.info_animation
                                    , container
                                    , false);

    if (getArguments() != null)
    {
      int rIdGif  = getArguments().getInt(BUNDLE_PARAM_R_ID_GIF)
        , rIdText = getArguments().getInt(BUNDLE_PARAM_R_ID_TEXT);

      setGif(infoAnimation.gif,   rIdGif);
      setTest(infoAnimation.text, rIdText);
    }
    else
      Log.i( "InfoAnimation: "
            ," r id's are invalid, did you use newInstance()?");

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
      Log.i("onCreateView"
              , "error while loading gif file");
    }
  }

  private GifImageView gif = null;
  private static final String BUNDLE_PARAM_R_ID_GIF  = "R_ID_GIF"
                            , BUNDLE_PARAM_R_ID_TEXT = "R_ID_TEXT";
}
