package com.example.eventbusmessages;

public class SendFileMessage
{
    private String absoluteFilePath;

    public SendFileMessage(String absoluteFilePath)
    {
        this.absoluteFilePath = absoluteFilePath;
    }

    public String getAbsoluteFilePath()
    {
        return absoluteFilePath;
    }
}
