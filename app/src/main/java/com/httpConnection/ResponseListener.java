package com.httpConnection;

import com.androidnetworking.error.ANError;

import okhttp3.Response;

public interface ResponseListener
{
  void onUploadedFile         (String serverAddress,  Response response);
  void onDownloadedFile       (String serverAddress,  String   absFilePath);
  void onGETResponseReceived  (String serverAddress,  Response response);
  void onUploadProgressChanged(String filePath,       float    percentage);

  void onErrorOccurred        (ANError error);
}
