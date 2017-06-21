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

    public final static int     offset              = 0;
    public       static float   twoFullRotateAngle  = 360
                                , defaultRadius     = 1;

    public float    moveFactor
                    , scaleFactor
                    , radius = defaultRadius;

    public float[]  viewMatrix
                    , projectionMatrix;
}