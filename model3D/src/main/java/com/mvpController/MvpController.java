package com.mvpController;

import com.example.viewer3d.SurfaceChangeListener;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class MvpController implements SurfaceChangeListener
{

    public MvpController ()
    {
      mvpControllerListener = null;
      mvpParams = new MvpParams();

      recalculateModelMatrix(0,0,0);
      recalculateViewMatrix();
      recalculateProjectionMatrix();
    }

    public  void updateRadius               (float f)
    {
      //-- when screen has e.g. 720px height then f theoretically can be quite
      //-- close 720. I wouldn't like scale model 700 times at once so i multiply
      //-- f by some number let's say scale factor equal 1 / screen height;

      mvpParams.radius
              = mvpParams.defaultRadius * ( 1 - f * mvpParams.scaleFactor);

      recalculateViewMatrix();
    }
    public  void rotate                     (float dx, float dy, float dz)
    {
      float x = dx * mvpParams.xFactor
          , y = dy * mvpParams.yFactor
          , z = dz * mvpParams.zFactor;

      recalculateModelMatrix(x, y, dz);
    }
    public  void addCameraChangeListener    (MvpControllerListener listener)
    {
        mvpControllerListener = listener;
    }

    @Override
    public  void onSurfaceChangeHandle      (int screenWidth, int screenHeight)
    {
      mvpParams.update(screenWidth, screenHeight);

      recalculateProjectionMatrix();
    }
    private void recalculateProjectionMatrix()
    {
      mvpParams.projectionMatrix
              .identity()
              .perspective( mvpParams.fovy
                          , mvpParams.aspectRatio
                          , mvpParams.nearPlane
                          , mvpParams.farPlane );
    }

    private void recalculateModelMatrix (float dxa, float dya, float dza)
    {
     final Vector3f xAxis = new Vector3f(1,0,0)
                  , yAxis = new Vector3f(0,1,0)
                  , zAxis = new Vector3f(0,0,1);

      new Matrix4f()
              .rotate(dxa, yAxis)
              .rotate(dya, xAxis)
              .rotate(dza, zAxis)
              .mul(mvpParams.modelMatrix)
              .get(mvpParams.modelMatrix);

        informThatCameraHasChanged();
    }
    private void recalculateViewMatrix      ()
    {
      mvpParams.viewMatrix
              .identity()
              .lookAt(0.0f, 1.0f, 5.0f,
                      0.0f, 0.0f, 0.0f,
                      0.0f, 1.0f, 0.0f)
              .scale(mvpParams.radius);
    }
    private void informThatCameraHasChanged ()
    {
        if( mvpControllerListener != null )
            mvpControllerListener.onMvpChanged(mvpParams);
    }

    private MvpParams mvpParams;
    private MvpControllerListener mvpControllerListener;
}
