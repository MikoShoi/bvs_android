package com.infoScreens;

import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.bruce.miko_mk10.R;
import com.example.bruce.miko_mk10.databinding.InfoScreenBinding;

public class InfoScreen extends Fragment
{
  public InfoScreen()
  {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView( LayoutInflater inflater
                          , ViewGroup container
                          , Bundle savedInstanceState)
  {
    screen = DataBindingUtil.inflate( inflater
                                    , R.layout.info_screen
                                    , container
                                    , false);

    return screen.getRoot();
  }

  @Override
  public void onResume()
  {
    super.onResume();

//    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
}

  protected void scaleImage()
  {
    ImageView   image   = screen.image;
    Display     display = getActivity().getWindowManager().getDefaultDisplay();

    Point           displaySize     = new Point();
    DisplayMetrics  displayMetrics  = new DisplayMetrics();

    display.getSize(displaySize);
    display.getMetrics(displayMetrics);

    final int imageWidth    = image.getDrawable().getIntrinsicWidth()
            , imageHeight   = image.getDrawable().getIntrinsicHeight()
            , statusBar     = 40 * (int) displayMetrics.density
            , displayWidth  = displaySize.x
            , displayHeight = displaySize.y - statusBar;

    float scaleFactor       = 1
            , hScaleFactor  = (float) displayWidth  / imageWidth
            , vScaleFactor  = (float) displayHeight / imageHeight;

    if ( hScaleFactor > 1 && vScaleFactor > 1 )
    {
        //-- display size is greater than image size -> scale up image
    }
    else
    {
        //-- display size is smaller than image size -> scale down image

        if ( hScaleFactor > vScaleFactor )
        {
            //-- scale down image to horizontal edge
            scaleFactor = displayWidth / ( imageWidth  * vScaleFactor );
        }
        else
        {
            //-- scale up image to vertical edge
        }
    }

    image.setScaleX(scaleFactor);
    image.setScaleY(scaleFactor);

    //TODO: improve
  }

  protected InfoScreenBinding screen;
}
