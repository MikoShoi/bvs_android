package com.connectionMessages;

import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.example.networkcontroller.RequestType;

public class SendRequestMessage
{
    public SendRequestMessage ( RequestType requestType
                                , String                    serverAddress
                                , OkHttpResponseListener    responseListener )
    {
        this.requestType        = requestType;
        this.serverAddress      = serverAddress;
        this.responseListener   = responseListener;
    }

    public RequestType              getRequestType      ()
    {
        return requestType;
    }
    public String                   getServerAddress    ()
    {
        return serverAddress;
    }
    public OkHttpResponseListener   getResponseListener ()
    {
        return responseListener;
    }

    private final RequestType               requestType;
    private final String                    serverAddress;
    private final OkHttpResponseListener    responseListener;
}
