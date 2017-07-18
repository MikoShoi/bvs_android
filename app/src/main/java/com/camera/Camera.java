package com.camera;

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
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bruce.bvs.R;
import com.example.bruce.bvs.databinding.CameraBinding;
import com.example.mikotools.AppManager;
import com.example.mikotools.MikoLogger;

import java.io.File;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.result.PendingResult;
import io.fotoapparat.result.PhotoResult;
import io.fotoapparat.view.CameraView;

import static io.fotoapparat.parameter.selector.AspectRatioSelectors.aspectRatio;
import static io.fotoapparat.parameter.selector.AspectRatioSelectors.standardRatio;
import static io.fotoapparat.parameter.selector.FlashSelectors.off;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.autoFocus;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.continuousFocus;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.fixed;
import static io.fotoapparat.parameter.selector.Selectors.firstAvailable;
import static io.fotoapparat.parameter.selector.SizeSelectors.biggestSize;

public class Camera
        extends Fragment
          implements PhotoUploadProgressListener
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

    progressBar = cameraBinding.progressBar;
    progressBarLabel = cameraBinding.progressBarLabel;

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
  @Override
  public void onPhotoUploadProgressChanged (int progress)
  {
    boolean b = progress == 0 || progress == 100;

    progressBarLabel.setVisibility(b ? View.INVISIBLE : View.VISIBLE);

    progressBar.setVisibility(b ? View.INVISIBLE : View.VISIBLE);
    progressBar.setProgress(progress);
  }

  private void    prepareCamera          (CameraView cameraView)
  {
    cameraView.setVisibility(View.VISIBLE);

    fotoapparat = Fotoapparat
            .with( getActivity().getApplicationContext() )
            .into(cameraView)
            .photoSize(standardRatio(biggestSize()))
            .previewSize(aspectRatio(1.6f, biggestSize()))
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
      public void onClick(final View v)
      {
        //-- inform listener that capture button was clicked
        cameraListener.onCapturePhoto();

        try
        {
          PhotoResult photoResult = fotoapparat.takePicture();

          final String  imagePath = genUniqueImageFilePath();
          final File    imageFile = new File(imagePath);
          if ( imageFile.createNewFile() )
          {
            photoResult
                    .saveToFile(imageFile)
                    .whenDone(new PendingResult.Callback<Void>()
                    {
                      @Override
                      public void onResult (Void aVoid)
                      {
                        //-- inform listener that photo was captured and saved
                        cameraListener.onPhotoCaptured(imagePath);

                        Snackbar snackbar = Snackbar.make(v, "captured", Snackbar.LENGTH_SHORT);

                        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
                        FrameLayout.LayoutParams parentParams = (FrameLayout.LayoutParams) snackBarView.getLayoutParams();
                        parentParams.width = FrameLayout.LayoutParams.WRAP_CONTENT;

                        TextView tv = (TextView) snackbar
                                .getView()
                                .findViewById(android.support.design.R.id.snackbar_text);
                        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                        snackbar.show();
                      }
                    });
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

        String fabText = getResources().getString(R.string.fabText);
        String message = Html.fromHtml(fabText).toString();

        Snackbar s  = Snackbar.make(v, message, timeDuration);
        TextView tv = (TextView) s
                .getView()
                .findViewById(android.support.design.R.id.snackbar_text);

        tv.setMaxLines(5);

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

  private ProgressBar     progressBar       = null;
  private TextView        progressBarLabel  = null;
  private Fotoapparat     fotoapparat       = null;
  private CameraListener  cameraListener    = null;
}


