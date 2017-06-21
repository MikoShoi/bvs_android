package com.example.camera;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.eventbusmessages.SendFileMessage;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Camera
{
//-- Variables
    private SurfaceHolder                               holder                  = null;
    private Context                                     context                 = null;

    private android.hardware.Camera                     camera                  = null;
    private android.hardware.Camera.PictureCallback     cameraCaptureCallback   = null;
    private android.hardware.Camera.AutoFocusCallback   cameraAutoFocusCallback = null;

    private String photoDirPath;

//-- Interface
    public Camera(SurfaceView v)
    {
        holder  = v.getHolder();
        context = v.getContext();

        init();

        createPhotoDirIfNoExist();
    }

    public void restart     ()
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
    public void turnOff     ()
    {
        camera.stopPreview();
        camera.release();
        camera = null;
    }
    public void takePicture ()
    {
        //--at first camera must get focus, then lock, and after capture image
        //--so that takePicture start from below line
        camera.autoFocus(cameraAutoFocusCallback);
    }

//-- Implementation
    private void init                   ()
    {
        camera = android.hardware.Camera.open();

        setSettings();
        setCaptureCallback();
        setAutoFocusCallback();

        restart();
    }
    private void setSettings            ()
    {
        android.hardware.Camera.Parameters cc = camera.getParameters();

        cc.setJpegQuality(100);
        cc.setRotation(180);
        cc.setAutoExposureLock(true);
        cc.setPictureSize(2560,1920);
        cc.setWhiteBalance(android.hardware.Camera.Parameters.WHITE_BALANCE_AUTO);

        camera.setParameters(cc);
        camera.setDisplayOrientation(180);
    }
    private void setCaptureCallback     ()
    {
        cameraCaptureCallback = new android.hardware.Camera.PictureCallback()
        {
            @Override
            public void onPictureTaken(byte[] data, android.hardware.Camera camera)
            {
                try
                {
                    String currentTime = String.valueOf( System.currentTimeMillis() );
                    String fileName = currentTime + ".jpg";
                    String filePath = photoDirPath + "/" + fileName;
//                    String filepath = String.format( "/sdcard/%d.jpg", System.currentTimeMillis() );

                    FileOutputStream outStream = new FileOutputStream(filePath);
                    outStream.write(data);
                    outStream.close();

                    Log.i("Camera","Wysyłam prośbę o wysłanie zdjęcia");
                    EventBus.getDefault().postSticky( new SendFileMessage( filePath ));
                    //--wysyłaj to natychmiast bez zapisywania do pliku!;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                Toast.makeText( context, "Picture Saved", 1000).show();
                restart();
            }
        };
    }
    private void setAutoFocusCallback   ()
    {
        cameraAutoFocusCallback = new android.hardware.Camera.AutoFocusCallback()
        {
            @Override
            public void onAutoFocus(boolean success, android.hardware.Camera camera)
            {
                Handler h = new Handler();
                h.postDelayed(DoAutoFocus, 1000);
                camera.lock();
            }

            Runnable DoAutoFocus = new Runnable()
            {
                public void run()
                {
                    camera.takePicture(null, null, null, cameraCaptureCallback);
                }
            };
        };
    }
    private void createPhotoDirIfNoExist()
    {
        photoDirPath = context.getFilesDir() + "/capturedImages";
        File tempDir = new File(photoDirPath);

        if( !tempDir.exists() && !tempDir.mkdir() )
        {
            String errorSource = "MainActivity:createPhotoDirIfNoExist";
            throw new Error("\n\n------Error source:\t" + errorSource);
        }
    }
}