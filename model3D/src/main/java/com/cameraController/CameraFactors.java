package com.cameraController;

import android.opengl.Matrix;

public class CameraFactors
{
    public CameraFactors()
    {
        viewMatrix          = new float[16];
        projectionMatrix    = new float[16];

        Matrix.setIdentityM(viewMatrix      , offset);
        Matrix.setIdentityM(projectionMatrix, offset);
    }

    public void update(int screenWidth, int screenHeight)
    {
        scaleFactor = 1.0f / screenHeight;
        xFactor     = fullRotateAngle / screenWidth;
        yFactor     = fullRotateAngle / screenHeight;
        aspectRatio = (float) screenWidth / screenHeight;
    }

    public final int    offset          = 0;
    public final float  fullRotateAngle = 360
                        , defaultRadius = 1
                        , nearPlane     = 0.1f
                        , farPlane      = 100.f
                        , fovy          = 120;

    public float  xFactor       = 1
                , yFactor       = 1
                , scaleFactor   = 1
                , aspectRatio   = 1;

    public float[]  viewMatrix
                    , projectionMatrix;
}