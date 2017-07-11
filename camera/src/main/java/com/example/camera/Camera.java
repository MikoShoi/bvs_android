package com.example.camera;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.Surface;

import com.example.camera.databinding.CameraBinding;

public class Camera extends Fragment
{
  @Override
  public View onCreateView(LayoutInflater inflater
                          ,ViewGroup container
                          ,Bundle savedInstanceState)
  {
    camera = DataBindingUtil.inflate( inflater
                                    , R.layout.camera
                                    , container
                                    , false );
    prepareCameraDevice();
    setButtonsListeners();

    return camera.getRoot();
  }
  @Override
  public void onAttach(Context context)
  {
    super.onAttach(context);

    if( context instanceof CameraListener)
      cameraListener = (CameraListener) context;
    else
      throw new RuntimeException( "parent object must implement " +
                                  "CameraListener interface" );
  }
  @Override
  public void onDetach()
  {
    super.onDetach();

    cameraListener = null;
  }

  @Override
  public void onResume()
  {
    super.onResume();

    int orientationMode = getActivity()
            .getWindowManager   ()
            .getDefaultDisplay  ()
            .getRotation        ();

    switch (orientationMode)
    {
      case Surface.ROTATION_0:
        cameraDevice.rotatePreview(90);
        break;
      case Surface.ROTATION_90:
        cameraDevice.rotatePreview(0);
        break;
      case Surface.ROTATION_180:
        cameraDevice.rotatePreview(270);
        break;
      case Surface.ROTATION_270:
        cameraDevice.rotatePreview(180);
        break;
    }
  }

  private void prepareCameraDevice            ()
  {
    CameraDevicePreview cdp = camera.cameraPreviewWindow;
    cameraDevice            = new CameraDevice(cdp);
    cdp.setPreviewSource(cameraDevice);

    cameraDevice.setCameraListener(cameraListener);
  }
  private void setButtonsListeners            ()
  {
    setButtonDoneOnClickListener();
    setButtonInfoOnClickListener();
    setButtonCaptureOnClickListener();
  }
  private void setButtonCaptureOnClickListener()
  {
    View.OnClickListener l = new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        cameraDevice.takePicture();
      }
    };

    camera.controls.capture.setOnClickListener(l);
  }
  private void setButtonDoneOnClickListener   ()
  {
    View.OnClickListener l = new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        cameraListener.onShootingFinished();
      }
    };

    camera.controls.done.setOnClickListener(l);
  }
  private void setButtonInfoOnClickListener   ()
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
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        s.show();
      }
    };

    camera.info.setOnClickListener(l);
  }

  private CameraBinding   camera;
  private CameraDevice    cameraDevice;
  private CameraListener  cameraListener;
}


