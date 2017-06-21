package com.example.viewer3d;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.cameraController.CameraChangeListener;
import com.cameraController.CameraFactors;
import com.example.handytools.Vector3D;
import com.model.AddModelToRenderMessage;
import com.model.Model3D;
import com.model.ModelData;
import com.model.ModelLoader;
import com.model.Model;
import com.model.RenderMatrices;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class RenderEngine
    implements GLSurfaceView.Renderer, CameraChangeListener
{
    public RenderEngine(Context context)
    {
        this.context = context;

        matrices                = new RenderMatrices();
        modelList               = new Vector<>();
        cameraFactors           = new CameraFactors();
        surfaceChangeListener   = null;

        AddModelToRenderMessage msg;
        msg = new AddModelToRenderMessage   ( R.raw.vertex_shader
                , R.raw.fragment_shader
                , "/storage/emulated/0/Download/model.off"
                , "uniqueName" );

        ModelLoader modelLoader = new ModelLoader(context);
        modelLoader.load(msg);
        ModelData modelData = modelLoader.getModelData();

        Model3D model3D = new Model3D(modelData);
        addModel(model3D);

        EventBus.getDefault().register(this);
        //TODO:improve
    }

    @Override
    public void onSurfaceCreated        (GL10 gl, EGLConfig config)
    {
        GLES30.glClearColor (1.0f, 1.0f, 1.0f, 1.0f);
//        GLES30.glEnable     (GLES30.GL_CULL_FACE);
//        GLES30.glCullFace   (GLES30.GL_FRONT);
    }
    @Override
    public void onSurfaceChanged        (GL10 gl, int width, int height)
    {
        GLES30.glViewport(0, 0, width, height);

        if( surfaceChangeListener != null )
            surfaceChangeListener.onSurfaceChangeHandle(width, height);
    }
    @Override
    public void onDrawFrame             (GL10 gl)
    {
        GLES30.glClear (    GLES30.GL_COLOR_BUFFER_BIT
                          | GLES30.GL_DEPTH_BUFFER_BIT );

        synchronized (modelList)
        {
            matrices.recalculate();

            for (Model object : modelList)
            {
                object.draw(matrices);
            }
        }
        //-- actually you don't add new models dynamically
        //-- so synchronized is probably redundant
    }

    @Override
    public void onCameraChangedHandle   (CameraFactors cameraFactors)
    {
        this.cameraFactors = cameraFactors;

        matrices.setViewAndProjection   ( cameraFactors.viewMatrix
                                        , cameraFactors.projectionMatrix );
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent                 (AddModelToRenderMessage event)
    {
        //--load model file using model loader
        ModelLoader modelLoader = new ModelLoader(context);
        modelLoader.load(event);

        //--get crucial model data
        ModelData modelData     = modelLoader.getModelData();

        //--create model and add to list of objects to render
        addModel( new Model3D(modelData) );
    }

    public void rotateModel(float dx, float dy)
    {
        //--if screenWidth px will respond 360 degree
        //--then dx px, will respond y degree of model rotation

        //--screenWidth_px - 360_degree | dx_px - y_degree
        //--y_degree = dx_px * 360_degree / screenWidth_px

        float dhr = dx * cameraFactors.moveFactor;

        //--but i would like rotate slower when camera will be closer so
        //--angle of rotation should be dependent on some scale factor

        dhr *= cameraFactors.radius;

        horizontalRotateAngle += dhr;

        matrices.rotateModel( horizontalRotateAngle, Vector3D.xUnitVector() );
    }
    public void addModel                (Model model)
    {
        synchronized (modelList)
        {
            if( !modelList.contains(model) )
            {
                modelList.add(model);
            }
        }
    }
    public void addSurfaceChangeListener(SurfaceChangeListener surfaceChangeListener)
    {
        this.surfaceChangeListener = surfaceChangeListener;
    }

    private RenderMatrices          matrices;
    private Context                 context;
    private CameraFactors           cameraFactors;
    private Vector<Model>           modelList;
    private SurfaceChangeListener   surfaceChangeListener;

    private float horizontalRotateAngle;
}
