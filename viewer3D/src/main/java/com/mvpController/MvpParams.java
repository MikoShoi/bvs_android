package com.mvpController;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class MvpParams
{
  void update(int screenWidth, int screenHeight)
  {
    aspectRatio = (float) screenWidth / screenHeight;
    scaleFactor = 1.0f / screenHeight;
    xFactor     = fullRotateAngle / screenWidth;
    yFactor     = fullRotateAngle / screenHeight;
    zFactor     = 2.0f;
  }

  public Matrix4f modelMatrix       = new Matrix4f()
                , viewMatrix        = new Matrix4f()
                , projectionMatrix  = new Matrix4f();

  final Vector3f  xVersor = new Vector3f(1,0,0)
                , yVersor = new Vector3f(0,1,0)
                , zVersor = new Vector3f(0,0,1);

  final float fullRotateAngle = 360
            , defaultRadius   = 1
            , nearPlane       = 0.01f
            , farPlane        = 100.f
            , fovy            = (float) Math.toRadians(45);

        float radius          =  defaultRadius
            , xFactor         = 1
            , yFactor         = 1
            , zFactor         = 1
            , scaleFactor     = 1
            , aspectRatio     = 1;
}