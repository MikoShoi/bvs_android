package com.example.viewer3d;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.cameraController.Camera;
import com.touchGestureProcessor.TouchGestureListener;
import com.touchGestureProcessor.TouchGestureProcessor;

public class PreviewSurface
        extends GLSurfaceView
            implements TouchGestureListener
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
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (touchGestureProcessor != null) touchGestureProcessor.touchEventHandle(event);

        return true;
    }

    @Override
    public void onScaleTouch(float ds)
    {
        camera.updateRadius(ds);
    }

    @Override
    public void onMoveTouch(float dx, float dy)
    {
        camera.rotate(dx, dy);
//        camera.rotateVertically(dy);
//        renderEngine.rotateModel(dx, 0);
    }

    private void setupRenderEngine()
    {
        setEGLContextClientVersion(3);

        setRenderer(renderEngine);
        setRenderMode(RENDERMODE_CONTINUOUSLY);
        //TODO: what about render mode - when dirty ? that's require some code modification
    }

    private Camera                camera;
    private RenderEngine          renderEngine;
    private TouchGestureProcessor touchGestureProcessor;
}