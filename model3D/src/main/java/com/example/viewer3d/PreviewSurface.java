package com.example.viewer3d;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.cameraController.Camera;
import com.touchGestureProcessor.TouchGestureListener;
import com.touchGestureProcessor.TouchGestureProcessor;

public class PreviewSurface
        extends GLSurfaceView
            implements  TouchGestureListener
                      , GestureAnalyserListener
{
    public PreviewSurface(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);

        camera                  = new Camera();
        renderEngine            = new RenderEngine(context);
        touchGestureProcessor   = new TouchGestureProcessor();

        camera.addCameraChangeListener(renderEngine);

        //-- render engine will inform camera when aspect ration changes
        renderEngine.addSurfaceChangeListener(camera);

        //-- touch gesture processor will inform preview surface about any user interaction
        touchGestureProcessor.addTouchGestureListener(this);

        //-- set some openGL settings for render engine
        setupRenderEngine();

      gestureAnalyser = new GestureAnalyser();
      gestureAnalyser.addListener(this);
      touchAnalyser = new TouchAnalyser(context);
      touchAnalyser.addListener(gestureAnalyser);
    }

  @Override
  public void onDrag (PointF shiftVector)
  {
    System.out.println("Drag");
    camera.rotate(shiftVector.x, shiftVector.y);
    requestRender();
  }

  @Override
  public void onScale (float d)
  {
    System.out.println("onScale");
    camera.updateRadius(d);
    requestRender();
  }

  @Override
    public boolean onTouchEvent(MotionEvent event)
    {
      touchAnalyser.analyze(event);

//        if (touchGestureProcessor != null)
//            touchGestureProcessor.touchEventHandle(event);

        return true;
    }
    @Override
    public void onScaleTouch(float ds)
    {
        camera.updateRadius(ds);
        requestRender();

        //TODO: i'm sure you can make it better
    }
    @Override
    public void onMoveTouch(float dx, float dy)
    {
        camera.rotate(dx, dy);
        requestRender();

        //TODO: and this too
    }

    private void setupRenderEngine()
    {
        setEGLContextClientVersion(3);

        setRenderer(renderEngine);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    protected Camera                camera;
    protected RenderEngine          renderEngine;
    protected TouchGestureProcessor touchGestureProcessor;
    private int initTwoFingersDistance = 0;
    private int lastFfId = -1, lastSfId = -1;
  private TouchAnalyser touchAnalyser;
  private GestureAnalyser gestureAnalyser;
}