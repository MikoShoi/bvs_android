package com.infoScreens;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bruce.miko_mk10.R;
import com.example.bruce.miko_mk10.databinding.PreloaderBinding;
import com.felipecsl.gifimageview.library.GifImageView;

import java.io.InputStream;

public class Loader extends Fragment
{
  public Loader ()
  {
    // Required empty public constructor
  }

  @Override
  public void onStart ()
  {
    super.onStart();
    gif.startAnimation();
  }
  @Override
  public void onStop  ()
  {
    super.onStop();
    gif.stopAnimation();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    PreloaderBinding preloader
            = DataBindingUtil.inflate(inflater
                                    , R.layout.preloader
                                    , container
                                    , false);
    try
    {
      InputStream stream  = container.getResources().openRawResource(R.raw.gif_8);
      byte[]      data    = new byte[stream.available()];
      stream.read(data);

      gif = preloader.gif;
      gif.setBytes(data);
    }
    catch (Exception e)
    {
      //todo: add exception handle
    }

    return preloader.getRoot();
  }

  private GifImageView gif;
}
