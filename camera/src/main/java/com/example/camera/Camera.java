package com.example.camera;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.camera.databinding.CameraBinding;
import com.example.mikotools.AppManager;
import com.example.mikotools.MikoLogger;

import java.io.File;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.result.PhotoResult;
import io.fotoapparat.view.CameraView;

import static io.fotoapparat.parameter.selector.AspectRatioSelectors.standardRatio;
import static io.fotoapparat.parameter.selector.FlashSelectors.off;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.autoFocus;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.continuousFocus;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.fixed;
import static io.fotoapparat.parameter.selector.Selectors.firstAvailable;
import static io.fotoapparat.parameter.selector.SizeSelectors.biggestSize;

public class Camera extends Fragment
{
  @Override
  public View onCreateView(LayoutInflater inflater
                          ,ViewGroup      container
                          ,Bundle         savedInstanceState)
  {
    CameraBinding cameraBinding
            = DataBindingUtil.inflate(inflater
                                    , R.layout.camera
                                    , container
                                    , false);

    prepareCamera(cameraBinding.cameraView);

    prepareCaptureButton(cameraBinding.capture);
    prepareDoneButton(cameraBinding.done);
    prepareInfoButton(cameraBinding.info);

    return cameraBinding.getRoot();
  }

  @Override
  public void onAttach    (Context context)
  {
    super.onAttach(context);

    if( context instanceof CameraListener)
      cameraListener = (CameraListener) context;
    else
      throw new RuntimeException( "parent object must implement " +
              "CameraListener interface" );
  }
  @Override
  public void onDetach    ()
  {
    super.onDetach();

    cameraListener = null;
  }
  @Override
  public void onStart     () {
    super.onStart();

    if (fotoapparat != null)
      fotoapparat.start();
  }
  @Override
  public void onStop      () {
    super.onStop();

    if (fotoapparat != null)
      fotoapparat.stop();
  }

  private void    prepareCamera          (CameraView cameraView)
  {
    cameraView.setVisibility(View.VISIBLE);

    fotoapparat = Fotoapparat
            .with( getActivity().getApplicationContext() )
            .into(cameraView)
            .photoSize(standardRatio(biggestSize()))
            .focusMode(firstAvailable(
                    continuousFocus(),
                    autoFocus(),
                    fixed()
            ))
            .flash( off() )
            .build();
  }
  private void    prepareCaptureButton   (FloatingActionButton button)
  {
    View.OnClickListener l = new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        try
        {
          PhotoResult photoResult = fotoapparat.takePicture();

          String  imagePath = genUniqueImageFilePath();
          File    imageFile = new File(imagePath);
          if ( imageFile.createNewFile() )
          {
            photoResult.saveToFile(imageFile);
            cameraListener.onPhotoCaptured(imagePath);
          }
          else
            MikoLogger.log("image file captured but not saved");
        }
        catch (Exception e)
        {
          MikoLogger.log("can not save image");
        }
      }
    };

    button.setOnClickListener(l);
  }
  private void    prepareDoneButton      (FloatingActionButton button)
  {
    View.OnClickListener l = new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        cameraListener.onShootingFinished();
      }
    };

    button.setOnClickListener(l);
  }
  private void    prepareInfoButton      (FloatingActionButton button)
  {
    View.OnClickListener l = new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        final int timeDuration = 10000;   //milliseconds

        String message = Html.fromHtml( getResources().getString(R.string.fabText) ).toString();

        Snackbar s  = Snackbar.make(v, message, timeDuration);
        TextView tv = (TextView) s
                .getView()
                .findViewById(android.support.design.R.id.snackbar_text);

        tv.setMaxLines(5);
//        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        s.show();
      }
    };

    button.setOnClickListener(l);
  }
  private String  genUniqueImageFilePath ()
  {
    String tempDirPath      = AppManager.getInstance().getTempDirPath()
            , uniqueName    = String.valueOf( System.currentTimeMillis() )
            , fileExtension = ".jpg";

    return tempDirPath + "/" + uniqueName + fileExtension;
  }

  private Fotoapparat     fotoapparat     = null;
  private CameraListener  cameraListener  = null;
}


