package com.example.camera;

import android.content.Context;
import android.hardware.Camera;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.handytools.AppManager;

import java.io.File;
import java.io.FileOutputStream;

public class CameraDevice
{
    public CameraDevice(SurfaceView v)
    {
        holder          = v.getHolder();
        context         = v.getContext();
        cameraListener  = null;

        init();
    }

    public void restart         ()
    {
        if( camera != null )
        {
            try
            {
                camera.setPreviewDisplay(holder);
                camera.stopPreview();
                camera.startPreview();
            }
            catch (Exception e)
            {
                System.err.println(e);
                return;
            }
        }
    }
    public void turnOff         ()
    {
        camera.stopPreview();
        camera.release();
        camera = null;
    }
    public void takePicture     ()
    {
        //--at first cameraDevice must get focus, then lock, and after capture image
        //--so that takePicture start from below line
        camera.autoFocus(cameraAutoFocusCallback);
    }
    public void setCameraListener(CameraListener cameraListener)
    {
        this.cameraListener = cameraListener;
    }
    public void rotatePreview   (int degrees)
    {
        camera.setDisplayOrientation(degrees);
    }

    private void    init                ()
    {
        camera = android.hardware.Camera.open();

        setSettings();
        setCaptureCallback();
        setAutoFocusCallback();

        restart();
    }
    private void    setSettings         ()
    {
        Camera.Parameters cp             = camera.getParameters();
        Camera.Size       maxPictureSize = cp.getSupportedPictureSizes().get(0);

        cp.setPictureSize       (maxPictureSize.width, maxPictureSize.height);
        cp.setJpegQuality       (100);
        cp.setAutoExposureLock  (true);
        cp.setWhiteBalance      (android.hardware.Camera.Parameters.WHITE_BALANCE_AUTO);

        camera.setParameters(cp);
    }
    private void    setCaptureCallback  ()
    {
        cameraCaptureCallback = new android.hardware.Camera.PictureCallback()
        {
            @Override
            public void onPictureTaken(byte[] data, android.hardware.Camera camera)
            {
                try
                {
                    String filePath = genUniqueAbsFilePath();

                    FileOutputStream outStream = new FileOutputStream(filePath);
                    outStream.write(data);
                    outStream.close();

                    if ( cameraListener != null )
                        cameraListener.onPhotoCaptured(filePath);

                    //--TODO: wysyłaj to natychmiast bez zapisywania do pliku!;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                Toast.makeText( context, "Picture Saved", Toast.LENGTH_SHORT).show();
                restart();
            }
        };
    }
    private void    setAutoFocusCallback()
    {
        cameraAutoFocusCallback = new android.hardware.Camera.AutoFocusCallback()
        {
            @Override
            public void onAutoFocus(boolean success, android.hardware.Camera camera)
            {
                Handler handler = new Handler();
                handler.postDelayed(capture, 1000);
                camera.lock();
            }

            Runnable capture = new Runnable()
            {
                public void run()
                {
                    camera.takePicture(null, null, cameraCaptureCallback);
                }
            };
        };
    }
    private String  genUniqueAbsFilePath()
    {
        String tempDirPath      = new AppManager().getTempDirPath()
                , uniqueName    = String.valueOf( System.currentTimeMillis() )
                , fileEtension  = ".jpg"
                , absFilePath   = tempDirPath + "/" + uniqueName + fileEtension;

        return absFilePath;
    }

    private SurfaceHolder                               holder                  = null;
    private Context                                     context                 = null;

    private android.hardware.Camera                     camera                  = null;
    private android.hardware.Camera.PictureCallback     cameraCaptureCallback   = null;
    private android.hardware.Camera.AutoFocusCallback   cameraAutoFocusCallback = null;

    private CameraListener cameraListener;
}