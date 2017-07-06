package com.example.camera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class CameraDevicePreview
        extends SurfaceView
        implements SurfaceHolder.Callback
{
  public CameraDevicePreview  (Context context)
  {
    super(context);
    getHolder().addCallback(this);
  }
  public CameraDevicePreview  (Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    getHolder().addCallback(this);
  }
  public CameraDevicePreview  (Context context, AttributeSet attrs)
  {
    super(context, attrs);
    getHolder().addCallback(this);
  }

  @Override
  public void surfaceCreated  (SurfaceHolder holder)
  {
    if( cameraDevice != null )
        cameraDevice.restart();
  }
  @Override
  public void surfaceChanged  (SurfaceHolder holder, int format, int w, int h)
  {
    if( cameraDevice != null )
        cameraDevice.restart();
  }
  @Override
  public void surfaceDestroyed(SurfaceHolder holder)
  {
    if (cameraDevice != null)
        cameraDevice.turnOff();
  }
  public void setPreviewSource(CameraDevice cam)
  {
    cameraDevice = cam;
  }

  private CameraDevice cameraDevice = null;
}