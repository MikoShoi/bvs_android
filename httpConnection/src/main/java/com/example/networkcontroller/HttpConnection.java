package com.example.networkcontroller;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.example.mikotools.AppManager;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class HttpConnection
{
  public HttpConnection(Context context, ResponseListener listener)
  {
    init(context);
    this.listener = listener;

    tempDirPath = new AppManager().getTempDirPath();
  }

  public void uploadFile      (final String serverAddress, String absFilePath)
  {
    AndroidNetworking
            .upload             ( serverAddress )
            .addMultipartFile   ( "image", new File(absFilePath) )
            .setPriority        (Priority.IMMEDIATE)
            .build              ( )
            .getAsOkHttpResponse( new OkHttpResponseListener()
            {
                @Override
                public void onResponse(Response response)
                {
                    listener.onUploadedFile(serverAddress, response);
                }

                @Override
                public void onError(ANError anError)
                {
                    listener.onErrorOccurred(anError);
                }
            });
  }
  public void downloadFile    (final String serverAddress, final String fileName)
  {
    AndroidNetworking
            .download       ( serverAddress, tempDirPath, fileName )
            .setPriority    ( Priority.IMMEDIATE )
            .build          ( )
            .startDownload  ( new DownloadListener()
            {
                @Override
                public void onDownloadComplete()
                {
                    String absFilePath = tempDirPath + "/" + fileName;

                    listener.onDownloadedFile(serverAddress, absFilePath);
                }

                @Override
                public void onError(ANError anError)
                {
                    listener.onErrorOccurred(anError);
                }
            });
  }
  public void sendGetRequest  (final String serverAddress)
  {
    AndroidNetworking
            .get                (serverAddress)
            .build              ()
            .getAsOkHttpResponse(new OkHttpResponseListener()
            {
                @Override
                public void onResponse(Response response)
                {
                    listener.onGetResponseReceived(serverAddress, response);
                }

                @Override
                public void onError(ANError anError)
                {
                    listener.onErrorOccurred(anError);
                }
            });
  }

  private void init           (Context context)
  {
    final int timeout = 60;

    OkHttpClient okHttpClient = new OkHttpClient()
            .newBuilder     ()
            .connectTimeout (timeout, TimeUnit.SECONDS)
            .readTimeout    (timeout, TimeUnit.SECONDS)
            .writeTimeout   (timeout, TimeUnit.SECONDS)
            .build          ();

    AndroidNetworking.initialize(context, okHttpClient);
//    AndroidNetworking.enableLogging();
//    AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.HEADERS);
  }

  private String           tempDirPath;
  private ResponseListener listener;
}
