package com.example.viewer3d;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.mvpController.MvpController;
import com.touchGesture.GestureAnalyser;
import com.touchGesture.GestureAnalyserListener;
import com.touchGesture.TouchAnalyser;

public class PreviewSurface
        extends GLSurfaceView
            implements GestureAnalyserListener
{
  public PreviewSurface(Context context, AttributeSet attributeSet)
  {
    super(context, attributeSet);

    //-- responsible for change view of object based on user interaction
    mvpController = new MvpController();

    //-- responsible for model rendering
    renderEngine  = new RenderEngine(context);

    //-- mvp will inform render engine when view when it occur
    mvpController.addCameraChangeListener(renderEngine);

    //-- render engine will inform mvpController when display aspect ration changes
    renderEngine.addSurfaceChangeListener(mvpController);

    //-- responsible for gestures recognition
    gestureAnalyser = new GestureAnalyser(this);
    //-- responsible for touches recognition
    touchAnalyser   = new TouchAnalyser();
    touchAnalyser.addListener(gestureAnalyser);

    //-- set some openGL settings for render engine
    setupRenderEngine();
  }

  @Override
  public void     onDrag            (float dx, float dy)
  {
    float x = (float) Math.toRadians(dx),
          y = (float) Math.toRadians(dy);

    mvpController.rotate(x, y, 0);
  }
  @Override
  public void     onScale           (float d)
  {
    mvpController.updateRadius(d);
  }
  @Override
  public void     onZRotate         (float da)
  {
    mvpController.rotate(0, 0, da);
  }
  @Override
  public boolean  onTouchEvent      (MotionEvent event)
  {
    touchAnalyser.analyze(event);
    requestRender();

    return true;
  }

  private void    setupRenderEngine ()
  {
      setEGLContextClientVersion(3);

      setRenderer(renderEngine);
      setRenderMode(RENDERMODE_WHEN_DIRTY);
  }

  protected MvpController mvpController;
  protected RenderEngine  renderEngine;

  private TouchAnalyser   touchAnalyser;
  private GestureAnalyser gestureAnalyser;
}