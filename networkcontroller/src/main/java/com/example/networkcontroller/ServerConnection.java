package com.example.networkcontroller;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.example.handytools.MikoError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class ServerConnection extends IntentService
{
    public ServerConnection()
    {
        super(ServerConnection.class.getName());

        eventBus = EventBus.getDefault();
        eventBus.register(this);
    }

    public boolean isConnectionAvailable(String serverAddress)
    {
        AndroidNetworking
                .get(serverAddress)
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener()
                {
                    @Override
                    public void onResponse(Response response)
                    {
                        setConnectionAvailable(true);
                    }

                    @Override
                    public void onError(ANError anError)
                    {
                        setConnectionAvailable(false);
                    }
                });

        return connectionAvailable;
    }

    @Override
    public void onHandleIntent(Intent intent)
    {
        init();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(UploadFileMessage event)
    {
        uploadFile  ( event.getServerAddress()
                    , event.getAbsoluteFilePath() );
    }
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(DownloadFileMessage event)
    {
        downloadFile( event.getServerAddress()
                    , event.getFileName() );
    }

    private void init           ()
    {
        final int timeout = 120;

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout (timeout, TimeUnit.SECONDS)
                .readTimeout    (timeout, TimeUnit.SECONDS)
                .writeTimeout   (timeout, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);
        AndroidNetworking.enableLogging();
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.HEADERS);
        
        createTempDirIfNoExist();
    }
    private void uploadFile     (String address, String absoluteFilePath)
    {
        File file = new File(absoluteFilePath);

        if( file.exists() )
        {
            informAboutProblem( ConnectionProblemType.BAD_FILE_PATH
                    , "File path" + absoluteFilePath + " no exist");

            return;
        }

        AndroidNetworking.upload(address)
                .addMultipartFile("image", file)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                        long percent = (bytesUploaded/totalBytes) * 100;
                        String percentage = String.valueOf(percent) + "%";

                        Log.i("Uploading ","in progress ... " + percentage );
                    }
                })
                .getAsOkHttpResponse(new OkHttpResponseListener()
                {
                    @Override
                    public void onResponse(Response response)
                    {
                        Log.i("Uploading ","is completed");
                    }

                    @Override
                    public void onError(ANError error)
                    {
                        informAboutProblem( ConnectionProblemType.NO_CONNECTION_TO_THE_SERVER
                                , "Problem while uploading file to the server");
                    }
                });
    }
    private void downloadFile   (String address, final String fileName)
    {
        AndroidNetworking.download(address, tempDirPath, fileName)
                .setTag("downloadTest")
                .setPriority(Priority.MEDIUM)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener()
                {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes)
                    {
                        Log.i("\n\nDownloading file: ", "in progress");
                        // do anything with progress
                    }
                })
                .startDownload(new DownloadListener()
                {
                    @Override
                    public void onDownloadComplete()
                    {
                        String absFilePath = tempDirPath + "/" + fileName;
                        eventBus.postSticky( new FileDownloadedMessage(absFilePath) );
                    }
                    @Override
                    public void onError(ANError error)
                    {
                        informAboutProblem( ConnectionProblemType.NO_CONNECTION_TO_THE_SERVER
                                , getErrorDescription(error) );
                    }
                });
    }

    private String  getErrorDescription     (ANError error)
    {
        String  e       = error.toString()                          + "\n"
                , eBody = error.getErrorBody()                      + "\n"
                , eCode = Integer.toString( error.getErrorCode() )  + "\n"
                , eDesc = e + eBody + eCode;

        return eDesc;
    }
    private void    informAboutProblem      (ConnectionProblemType problemType, String description)
    {
        eventBus.postSticky( new ConnectionProblemMessage(problemType, description) );
    }
    private void    createTempDirIfNoExist  ()
    {
        String tempDirPath = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/downloadedFiles";

        File tempDir = new File(tempDirPath);

        if ( !tempDir.exists() && tempDir.mkdir() )
                throw new MikoError(this
                                    , "createTempDirIfNoExist"
                                    , "Can not create temp dir");
    }
    private void    setConnectionAvailable  (boolean b)
    {
        connectionAvailable = b;
    }

    private String  tempDirPath         = null;
    private boolean connectionAvailable = false;

    EventBus eventBus;
}
