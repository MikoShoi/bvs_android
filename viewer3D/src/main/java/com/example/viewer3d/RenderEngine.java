package com.example.viewer3d;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.model.Model;
import com.model.RenderMatrices;
import com.mvpController.MvpControllerListener;
import com.mvpController.MvpParams;

import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class RenderEngine
    implements GLSurfaceView.Renderer, MvpControllerListener
{
  public RenderEngine()
  {
    matrices  = new RenderMatrices();
    modelList = new Vector<>();
    listener  = null;
  }

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
        listener.onDisplaySurfaceChanged(width, height);
  }
  @Override
  public void onDrawFrame       (GL10 gl)
  {
    GLES30.glClear( GLES30.GL_COLOR_BUFFER_BIT
                  | GLES30.GL_DEPTH_BUFFER_BIT );

    synchronized (modelList)
    {
        matrices.recalculate();

        for (Model object : modelList)
        {
            object.draw(matrices);
        }
    }
    //TODO: fix
  }

  @Override
  public void onMvpChanged      (MvpParams mvpParams)
  {
    matrices.setModel(mvpParams.modelMatrix);
    matrices.setViewMatrix(mvpParams.viewMatrix);
    matrices.setProjectionMatrix(mvpParams.projectionMatrix);

    matrices.recalculate();
  }

  void addModel                 (Model model)
  {
    synchronized (modelList)
    {
      if( !modelList.contains(model) )
      {
        modelList.add(model);
      }
    }
  }
  void addSurfaceChangeListener (SurfaceChangeListener listener)
  {
    this.listener = listener;
  }

  private RenderMatrices          matrices;
  private Vector<Model>           modelList;
  private SurfaceChangeListener listener;
}
