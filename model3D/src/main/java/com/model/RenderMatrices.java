package com.model;

import com.example.handytools.MikoMath;

import org.joml.Matrix4f;

public class RenderMatrices
{
  public RenderMatrices()
  {
      model       = MikoMath.getIndentityMatrix4f();
      view        = MikoMath.getIndentityMatrix4f();
      projection  = MikoMath.getIndentityMatrix4f();

      normal      = MikoMath.getIndentityMatrix4f();
      mv          = MikoMath.getIndentityMatrix4f();
      mvp         = MikoMath.getIndentityMatrix4f();
  }

  public float[] getModel     ()
  {
      return model;
  }
  public float[] getView      ()
  {
      return view;
  }
  public float[] getProjection()
  {
      return projection;
  }
  public float[] getMv        ()
  {
      return mv;
  }
  public float[] getMvp       ()
  {
    float[] mvp = new float[16];
    mvpMatrix.get(mvp);

    return mvp;
  }
  public float[] getNormal    ()
  {
      return normal;
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
    mv      = MikoMath.multiplyMatrices     ( view      , model);
    mvp     = MikoMath.multiplyMatrices     ( projection, mv);
    normal  = MikoMath.calculateNormalMatrix( mv);

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

  private float[] model
                  , view
                  , projection
                  , normal
                  , mv
                  , mvp;

  private final int offset = 0;
}
