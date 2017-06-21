package com.example.networkcontroller;

public class UploadFileMessage
{
    private String absoluteFilePath;

    public UploadFileMessage(String absoluteFilePath)
    {
        this.absoluteFilePath = absoluteFilePath;
    }

    public String getAbsoluteFilePath()
    {
        return absoluteFilePath;
    }
}
