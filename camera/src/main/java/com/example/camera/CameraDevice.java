package com.example.camera;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.mikotools.AppManager;
import com.example.mikotools.MikoError;

import java.io.FileOutputStream;

class CameraDevice
{
  CameraDevice(SurfaceView v)
  {
    holder          = v.getHolder();
    context         = v.getContext();

    init();
  }

  void restart          ()
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
        throw new MikoError(this
                          , "restart"
                          , "error while restart cammera");
      }
    }
  }
  void turnOff          ()
  {
    camera.stopPreview();
    camera.release();
    camera = null;
  }
  void takePicture      ()
  {
    //--at first cameraDevice must get focus, then lock, and after capture image
    //--so that takePicture start from below line
    camera.autoFocus(cameraAutoFocusCallback);
  }
  void setCameraListener(CameraListener cameraListener)
  {
    this.cameraListener = cameraListener;
  }
  void rotatePreview    (int degrees)
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
          throw new MikoError(this
                            , "onPictureTaken"
                            , "problem while file operations");
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

  private CameraListener  cameraListener  = null;
  private SurfaceHolder   holder          = null;
  private Context         context         = null;

  private android.hardware.Camera                   camera                  = null;
  private android.hardware.Camera.PictureCallback   cameraCaptureCallback   = null;
  private android.hardware.Camera.AutoFocusCallback cameraAutoFocusCallback = null;
}