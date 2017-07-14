package com.example.camera;

public interface CameraListener
{
  void onCapturePhoto     ();
  void onPhotoCaptured    (String absoluteFilePath);
  void onShootingFinished ();
}
