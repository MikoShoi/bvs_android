package com.cameraController;

import com.example.viewer3d.SurfaceChangeListener;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera implements SurfaceChangeListener
{

    public Camera()
    {
        cameraChangeListener    = null;
        cameraFactors           = new CameraFactors();

        recalculateViewMatrix(0,0);
    }

    public  void updateRadius               (float f)
    {
        //-- when screen has e.g. 720px height then f theoretically can be quite
        //-- close 720. I wouldn't like scale model 700 times at once so i multiply
        //-- f by some number let's say scale factor equal 1 / screen height;

        radius = cameraFactors.defaultRadius * ( 1 - f * cameraFactors.scaleFactor);

        recalculateViewMatrix(0,0);
    }
    public  void rotate                     (float dx, float dy)
    {
        float xAngle = dx * cameraFactors.xFactor;
        float yAngle = dy * cameraFactors.yFactor;

             if ( yAngle > 360.0f ) yAngle -= 360;
        else if ( yAngle <   0.0f ) yAngle += 360;

        recalculateViewMatrix(xAngle, yAngle);
    }
    public  void addCameraChangeListener    (CameraChangeListener listener)
    {
        cameraChangeListener = listener;
    }

    @Override
    public  void onSurfaceChangeHandle      (int screenWidth, int screenHeight)
    {
        cameraFactors.update(screenWidth, screenHeight);

        recalculateViewMatrix(0,0);
    }

    private void recalculateViewMatrix      (float xAngle, float yAngle)
    {
        float phi           = (float)Math.toRadians(xAngle)
            , theta         = (float)Math.toRadians(yAngle);

      Vector3f  xAxis = new Vector3f(1,0,0)
              , yAxis = new Vector3f(0,1,0);

      Matrix4f projection = new Matrix4f()
              .perspective(   (float) Math.toRadians(45)
                            , cameraFactors.aspectRatio
                            , 0.01f
                            , 100.0f );

      Matrix4f view = new Matrix4f()
              .lookAt(0.0f, 1.0f, 5.0f,
                      0.0f, 0.0f, 0.0f,
                      0.0f, 1.0f, 0.0f );

      new Matrix4f()
              .rotate(phi,yAxis)
              .rotate(theta,xAxis)
              .mul(model)
              .get(model);

      new Matrix4f()
              .mul(projection)
              .mul(view)
              .mul(model)
              .scale(radius)
              .get(cameraFactors.viewMatrix);

        informThatCameraHasChanged();
    }
    private void informThatCameraHasChanged ()
    {
        if( cameraChangeListener != null )
            cameraChangeListener.onCameraChangedHandle(cameraFactors);
    }

    private CameraFactors           cameraFactors;
    private CameraChangeListener    cameraChangeListener;

    float     radius = 1;
    Matrix4f  model  = new Matrix4f();
}
