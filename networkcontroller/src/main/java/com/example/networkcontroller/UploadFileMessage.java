package com.example.networkcontroller;

public class UploadFileMessage
{
    public UploadFileMessage(String serverAddress, String absoluteFilePath)
    {
        this.serverAddress      = serverAddress;
        this.absoluteFilePath   = absoluteFilePath;
    }

    public String getAbsoluteFilePath()
    {
        return absoluteFilePath;
    }
    public String getServerAddress()
    {
        return serverAddress;
    }

    private String  absoluteFilePath
                    , serverAddress;
}
