package com.example.networkcontroller;

import com.androidnetworking.error.ANError;

import okhttp3.Response;

public interface ResponseListener
{
    void onUploadedFile         (String serverAddress, Response response);
    void onDownloadedFile       (String serverAddress, String   absFilePath);
    void onGETResponseReceived (String serverAddress, Response response);
    
    void onErrorOccurred        (ANError error);
}
