package com.model;

import org.joml.Matrix4f;

public class RenderMatrices
{
  public RenderMatrices()
  {

  }

  float[] getMvp       ()
  {
    float[] mvp = new float[16];
    mvpMatrix.get(mvp);

    return mvp;
  }

  public void setModel            (Matrix4f modelMatrix)
  {
    this.modelMatrix = modelMatrix;
  }
  public void setViewMatrix       (Matrix4f viewMatrix)
  {
    this.viewMatrix = viewMatrix;
  }
  public void setProjectionMatrix (Matrix4f projectionMatrix)
  {
    this.projectionMatrix = projectionMatrix;
  }
  public void recalculate         ()
  {
    mvpMatrix
            .identity()
            .mul(projectionMatrix)
            .mul(viewMatrix)
            .mul(modelMatrix);
  }

  private Matrix4f  modelMatrix       = new Matrix4f()
                  , viewMatrix        = new Matrix4f()
                  , projectionMatrix  = new Matrix4f()
                  , mvpMatrix         = new Matrix4f();
}
