package com.connectionMessages;

import com.androidnetworking.interfaces.DownloadListener;

public class DownloadFileMessage
{
    public DownloadFileMessage( String              fileName
                                , String            serverAddress
                                , DownloadListener  responseListener)
    {
        this.fileName           = fileName;
        this.serverAddress      = serverAddress;
        this.responseListener   = responseListener;
    }

    public String           getFileName         ()
    {
        return fileName;
    }
    public String           getServerAddress    ()
    {
        return serverAddress;
    }
    public DownloadListener getResponseListener ()
    {
        return responseListener;
    }

    private final String fileName, serverAddress;
    private final DownloadListener responseListener;
}
