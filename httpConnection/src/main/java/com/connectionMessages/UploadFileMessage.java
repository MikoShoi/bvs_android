package com.connectionMessages;

import com.androidnetworking.interfaces.OkHttpResponseListener;

public class UploadFileMessage
{
    public UploadFileMessage(String                     filePath
                            , String                    serverAddress
                            , OkHttpResponseListener    responseListener)
    {
        this.filePath           = filePath;
        this.serverAddress      = serverAddress;
        this.responseListener   = responseListener;
    }

    public String                   getFilePath         ()
    {
        return filePath;
    }
    public String                   getServerAddress    ()
    {
        return serverAddress;
    }
    public OkHttpResponseListener   getResponseListener ()
    {
        return responseListener;
    }

    private String filePath, serverAddress;
    private OkHttpResponseListener  responseListener;
}
