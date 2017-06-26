package com.example.networkcontroller;

import com.androidnetworking.error.ANError;

import okhttp3.Response;

public interface HttpConnectionListener
{
    void onUploadedFile         (String serverAddress, Response response);
    void onDownloadedFile       (String serverAddress, String   absFilePath);
    void onGetResponseReceived  (String serverAddress, Response response);

    void onErrorOccurred        ( String            serverAddress
                                , RequestType operationType
                                , ANError           error);
}
