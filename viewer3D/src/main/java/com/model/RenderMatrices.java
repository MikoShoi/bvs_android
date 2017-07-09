package com.model;

import org.joml.Matrix4f;

public class RenderMatrices
{
  public  void    update      (Matrix4f model, Matrix4f view, Matrix4f projection)
  {
    if (model != null)
      modelMatrix       = model;

    if (view != null)
      viewMatrix        = view;

    if (projection != null)
      projectionMatrix  = projection;

    recalculate();
  }
  public float[]  getMvp      ()
  {
    float[] mvp = new float[16];
    mvpMatrix.get(mvp);

    return mvp;
  }

  private void    recalculate ()
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
