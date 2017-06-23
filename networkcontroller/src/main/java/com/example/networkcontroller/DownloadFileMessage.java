package com.example.networkcontroller;

public class DownloadFileMessage
{
    public DownloadFileMessage(String serverAddress, String fileName)
    {
        this.fileName       = fileName;
        this.serverAddress  = serverAddress;
    }

    public String getFileName()
    {
        return fileName;
    }
    public String getServerAddress()
    {
        return serverAddress;
    }

    private final String fileName
                        , serverAddress;
}
