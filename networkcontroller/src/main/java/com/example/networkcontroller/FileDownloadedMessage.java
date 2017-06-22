package com.example.networkcontroller;

public class FileDownloadedMessage
{
    public FileDownloadedMessage(String filePath)
    {
        this.absFilePath = filePath;
    }

    public String getAbsFilePath()
    {
        return absFilePath;
    }

    private final String absFilePath;
}
