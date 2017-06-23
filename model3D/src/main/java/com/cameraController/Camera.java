package com.cameraController;

import android.opengl.Matrix;
import android.util.Log;

import com.example.viewer3d.SurfaceChangeListener;
import com.example.handytools.MikoMath;
import com.example.handytools.Vector3D;

public class Camera implements SurfaceChangeListener
{
    public Camera()
    {
        cameraChangeListener    = null;
        cameraFactors           = new CameraFactors();

        recalculateViewMatrix();
    }

    public  void updateRadius               (float f)
    {
        //-- when screen has e.g. 720px height then f theoretically can be quite
        //-- close 720. I wouldn't like scale model 700 times at once so i multiply
        //-- f by some number let's say scale factor equal 1 / screen height;

        radius = cameraFactors.defaultRadius * ( 1 - f * cameraFactors.scaleFactor);

        recalculateViewMatrix();
    }
    public  void rotate                     (float dx, float dy)
    {
        xAngle += dx * cameraFactors.xFactor;
        yAngle += dy * cameraFactors.yFactor;

             if ( yAngle > 360.0f ) yAngle -= 360;
        else if ( yAngle <   0.0f ) yAngle += 360;

        isYAxisUpsideDown = ( yAngle >= 90.0f && yAngle <= 270.0f );

        recalculateViewMatrix();
    }
    public  void addCameraChangeListener    (CameraChangeListener listener)
    {
        cameraChangeListener = listener;
    }

    @Override
    public  void onSurfaceChangeHandle      (int screenWidth, int screenHeight)
    {
        cameraFactors.update(screenWidth, screenHeight);

        recalculatePerpectiveMatrix();
        recalculateViewMatrix();
    }

    private void recalculatePerpectiveMatrix()
    {
        Matrix.setIdentityM(cameraFactors.projectionMatrix, cameraFactors.offset);
        Matrix.perspectiveM(cameraFactors.projectionMatrix, cameraFactors.offset
                , cameraFactors.fovy
                , cameraFactors.aspectRatio
                , cameraFactors.nearPlane
                , cameraFactors.farPlane );
    }
    private void recalculateViewMatrix      ()
    {
        float phi           = (float)Math.toRadians(xAngle)
            , theta         = (float)Math.toRadians(yAngle)
            , upDirection   = isYAxisUpsideDown ? -1 : 1;

        //-- sVec is a camera position in spherical, cVec in cartesian c.s.
        Vector3D  sVec = new Vector3D(radius, phi, theta)
                , cVec = MikoMath.transformToCartesian(sVec);

        Matrix.setIdentityM ( cameraFactors.viewMatrix, cameraFactors.offset);
        Matrix.setLookAtM   ( cameraFactors.viewMatrix
                            ,cameraFactors.offset
                            //--camera position                 | x, y, z
                            ,  cVec.x   , cVec.z        , cVec.y
                            //--point which observe             | x, y, z
                            ,  0        ,  0            ,  0
                            //--up vector                       | x, y, z
                            ,  0        , upDirection   ,  0 );

        informThatCameraHasChanged();
    }
    private void informThatCameraHasChanged ()
    {
        if( cameraChangeListener != null )
            cameraChangeListener.onCameraChangedHandle(cameraFactors);
    }

    private CameraFactors           cameraFactors;
    private CameraChangeListener    cameraChangeListener;

    private boolean isYAxisUpsideDown;

    float   radius   = 1
            , xAngle = 0
            , yAngle = 0;
}
