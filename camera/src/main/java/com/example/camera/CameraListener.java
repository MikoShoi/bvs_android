package com.example.camera;

public interface CameraListener
{
    public void onPhotoCaptured     (String absoluteFilePath);
    public void onShootingFinished  ();
}
