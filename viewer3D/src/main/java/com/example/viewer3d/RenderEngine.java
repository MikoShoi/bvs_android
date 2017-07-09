package com.example.viewer3d;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.model.Model;
import com.model.RenderMatrices;
import com.mvpController.MvpControllerListener;
import com.mvpController.MvpParams;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class RenderEngine
        implements GLSurfaceView.Renderer, MvpControllerListener
{
  @Override
  public void onSurfaceCreated  (GL10 gl, EGLConfig config)
  {
    GLES30.glClearColor (1.0f, 1.0f, 1.0f, 1.0f);
//    GLES30.glEnable     (GLES30.GL_CULL_FACE);
//    GLES30.glCullFace   (GLES30.GL_FRONT);
  }
  @Override
  public void onSurfaceChanged  (GL10 gl, int width, int height)
  {
    GLES30.glViewport(0, 0, width, height);

    if( listener != null )
    {
      listener.onDisplaySurfaceChanged(width, height);
    }
  }
  @Override
  public void onDrawFrame       (GL10 gl)
  {
    GLES30.glClear( GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT );

    if ( model != null)
    {
      model.draw(matrices);
    }
  }
  @Override
  public void onMvpChanged      (MvpParams mvpParams)
  {
    matrices.update(mvpParams.modelMatrix
                  , mvpParams.viewMatrix
                  , mvpParams.projectionMatrix);
  }

  void setRenderModel           (Model model)
  {
    this.model = model;
  }
  void addSurfaceChangeListener (SurfaceChangeListener listener)
  {
    this.listener = listener;
  }

  private RenderMatrices        matrices  = new RenderMatrices();
  private Model                 model     = null;
  private SurfaceChangeListener listener  = null;
}
