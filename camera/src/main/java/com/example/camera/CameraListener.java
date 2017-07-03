package com.example.camera;

public interface CameraListener
{
  void onPhotoCaptured    (String absoluteFilePath);
  void onShootingFinished ();
}
