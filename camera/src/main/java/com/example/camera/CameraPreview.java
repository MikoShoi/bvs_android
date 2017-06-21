package com.example.camera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class CameraPreview
        extends SurfaceView
        implements SurfaceHolder.Callback
{
    Camera camera = null;

    public CameraPreview    (Context context)
    {
        super(context);
        getHolder().addCallback(this);
    }
    public CameraPreview    (Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        getHolder().addCallback(this);
    }
    public CameraPreview    (Context context, AttributeSet attrs)
    {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated      (SurfaceHolder holder)
    {
        if( camera != null )
            camera.restart();
    }
    @Override
    public void surfaceChanged      (SurfaceHolder holder, int format, int w, int h)
    {
        if( camera != null )
            camera.restart();
    }
    @Override
    public void surfaceDestroyed    (SurfaceHolder holder)
    {
        if (camera != null)
            camera.turnOff();
    }

    public void setPreviewSource    (Camera cam)
    {
        camera = cam;
    }
}