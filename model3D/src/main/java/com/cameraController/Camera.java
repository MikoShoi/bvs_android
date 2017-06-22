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

    public  void updateRadius                (float f)
    {
        //-- when screen has e.g. 720px height then f theoretically can be quite
        //-- close 720. I wouldn't like scale model 700 times at once so i multiply
        //-- f by some number let's say scale factor equal 1 / screen height;

        cameraFactors.radius = CameraFactors.defaultRadius * ( 1 - f * cameraFactors.scaleFactor);

        recalculateViewMatrix();
    }
    public  void rotate(float dx, float dy)
    {
        int   xAngle = 0
            , yAngle = 0;

                if (dx >  1) Log.i( "x angle: ", Integer.toString(xAngle++) );
        else    if (dx < -1) Log.i( "x angle: ", Integer.toString(xAngle--) );

                if (dy >  1) Log.i( "y angle: ", Integer.toString(yAngle++) );
        else    if (dy < -1) Log.i( "y angle: ", Integer.toString(yAngle--) );
    }
    public  void rotateVertically            (float ry)
    {
        float vhr = ry * cameraFactors.moveFactor * cameraFactors.radius;

        verticalRotateAngle -= vhr;

             if ( verticalRotateAngle > 360 )
                    verticalRotateAngle -= 360;
        else if ( verticalRotateAngle < 0.0f )
                    verticalRotateAngle += 360;

        isYAxisUpsideDown = verticalRotateAngle >= 90 && verticalRotateAngle < 270;

        recalculateViewMatrix();
    }
    public  void addCameraChangeListener     (CameraChangeListener listener)
    {
        cameraChangeListener = listener;
    }

    @Override
    public  void onSurfaceChangeHandle      (int screenWidth, int screenHeight)
    {
        cameraFactors.scaleFactor = 1.0f / screenHeight;
        cameraFactors.moveFactor  = CameraFactors.twoFullRotateAngle / screenWidth;

        Matrix.setIdentityM(cameraFactors.projectionMatrix, CameraFactors.offset);
        Matrix.perspectiveM(cameraFactors.projectionMatrix, CameraFactors.offset
                            , 120                                   // fovy
                            , (float) screenWidth / screenHeight    // aspectRatio
                            , 0.1f                                  // near
                            , 10000);                               // far

        recalculateViewMatrix();
    }

    private void recalculateViewMatrix      ()
    {
        float r     = cameraFactors.radius
            , theta = 0
            , phi   = (float)Math.toRadians(verticalRotateAngle)
            , yAxis = isYAxisUpsideDown ? -1 : 1;

        //-- sVec is a camera position in spherical, cVec in cartesian c.s.
        Vector3D  sVec = new Vector3D(r, phi, theta)
                , cVec = MikoMath.transformToCartesian(sVec);

        Matrix.setIdentityM ( cameraFactors.viewMatrix, CameraFactors.offset);
        Matrix.setLookAtM   ( cameraFactors.viewMatrix
                            ,CameraFactors.offset
                            //--camera position                 | x, y, z
                            ,  cVec.x   , cVec.y    , cVec.z
                            //--point which observe             | x, y, z
                            ,  0        ,  0        ,  0
                            //--up vector                       | x, y, z
                            ,  0        , yAxis     ,  0 );

        informThatCameraHasChanged();
    }
    private void informThatCameraHasChanged ()
    {
        if( cameraChangeListener != null )
            cameraChangeListener.onCameraChangedHandle(cameraFactors);
    }

    private float   verticalRotateAngle;
    private boolean isYAxisUpsideDown;

    private CameraFactors        cameraFactors;
    private CameraChangeListener cameraChangeListener;
}
