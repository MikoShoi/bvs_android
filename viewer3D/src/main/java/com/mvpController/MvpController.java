package com.mvpController;

import com.example.viewer3d.SurfaceChangeListener;

import org.joml.Matrix4f;

public class MvpController implements SurfaceChangeListener
{
  public MvpController ()
  {
    listener = null;
    params   = new MvpParams();

    recalculateModelMatrix(0,0,0);
    recalculateViewMatrix();
    recalculateProjectionMatrix();
  }

  @Override
  public  void onDisplaySurfaceChanged    (int screenWidth, int screenHeight)
  {
    params.update(screenWidth, screenHeight);

    recalculateProjectionMatrix();
  }

  public  void updateRadius               (float f)
  {
    //-- when screen has e.g. 720px height then f theoretically can be quite
    //-- close 720. I wouldn't like scale model 700 times at once so i multiply
    //-- f by some number let's say scale factor equal 1 / screen height;

    params.radius = params.defaultRadius * ( 1 - f * params.scaleFactor);

    recalculateViewMatrix();
  }
  public  void rotate                     (float dx, float dy, float dz)
  {
    float x = dx * params.xFactor
        , y = dy * params.yFactor
        , z = dz * params.zFactor;

    recalculateModelMatrix(x, y, z);
  }
  public  void addCameraChangeListener    (MvpControllerListener listener)
  {
    this.listener = listener;
    listener.onMvpChanged(params);
  }

  private void recalculateProjectionMatrix()
  {
    params.projectionMatrix
            .identity()
            .perspective( params.fovy
                        , params.aspectRatio
                        , params.nearPlane
                        , params.farPlane );

    informThatCameraHasChanged();
  }

  private void recalculateModelMatrix     (float dxa, float dya, float dza)
  {
    new Matrix4f()
            .rotate(dxa, params.yVersor)
            .rotate(dya, params.xVersor)
            .rotate(dza, params.zVersor)
            .mul(params.modelMatrix)
            .get(params.modelMatrix);

      informThatCameraHasChanged();
  }
  private void recalculateViewMatrix      ()
  {
    params.viewMatrix
            .identity()
            .lookAt(0.0f, 1.0f, 5.0f,
                    0.0f, 0.0f, 0.0f,
                    0.0f, 1.0f, 0.0f)
            .scale(params.radius);
  }
  private void informThatCameraHasChanged ()
  {
      if( listener != null )
          listener.onMvpChanged(params);
  }

  private MvpParams             params;
  private MvpControllerListener listener;
}
