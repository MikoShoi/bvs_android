package com.example.camera;

public interface CameraInterface
{
//--when user captured new photo and program should send it to server
    public void onCapturedHandle(String absoluteFilePath);

//--when user are done capture images and would like to see model
    public void onDoneHandle();
}
