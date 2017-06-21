package com.example.networkcontroller;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class NetworkController extends IntentService
{
    public final static String  intentParamServerAddress    = "SERVER_ADDRESS"
                                , intentParamGetEndpoint    = "GET_ENDPOINT"
                                , intentParamPostEndpoint   = "POST_ENDPOINT";

    private String  serverAddress   = null
                    , postEndpoint  = null
                    , getEndpoint   = null
                    , tempDirPath   = null;

    public NetworkController()
    {
        super(NetworkController.class.getName());

        EventBus.getDefault().register(this);
    }

    @Override
    public void onHandleIntent(Intent intent)
    {
        setConnectionParams(intent);

        createDirForDownloadedFilesIfNoExist();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(UploadFileMessage event)
    {
        String filePath = event.getAbsoluteFilePath();

        uploadFile(serverAddress+postEndpoint, filePath);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(DownloadFileMessage event)
    {
        //TODO: repair, try not to use literals as "model.off"
        downloadFile(serverAddress + getEndpoint, tempDirPath, "model.off");
    }

    private void downloadFile   (    String address
                                    ,String dirPath
                                    ,String fileName )
    {
        AndroidNetworking.download(address, dirPath, fileName)
                .setTag("downloadTest")
                .setPriority(Priority.MEDIUM)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener()
                {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes)
                    {
                        Log.i("\n\nDownload: ", "in progress");
                        // do anything with progress
                    }
                })
                .startDownload(new DownloadListener()
                {
                    @Override
                    public void onDownloadComplete()
                    {
                        Log.i("\n\nDownload: ", "complete");
                        // do anything after completion
                    }
                    @Override
                    public void onError(ANError error)
                    {
                        printErrorInfo(error, "ButtonDownload");
                        // handle error
                    }
                });
    }
    private void uploadFile     (    String address
                                    ,String absoluteFilePath)
    {
        File file = new File( absoluteFilePath );

        if( !file.exists() )
        {
            Log.i("\n\n----WARNING","NetController:uploadFile() "
                                    + "- given file path is invalid");

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
                        Log.i("Uploading ","is complete");
                    }

                    @Override
                    public void onError(ANError error)
                    {
                        Log.i("Uploading ","is failed");
                        printErrorInfo(error, "ButtonSendPost");
                    }
                });
    }


    private void    setConnectionParams (Intent intent)
    {
        serverAddress   = intent.getStringExtra(intentParamServerAddress);
        postEndpoint    = intent.getStringExtra(intentParamPostEndpoint);
        getEndpoint     = intent.getStringExtra(intentParamGetEndpoint);

        if( isAnyParameterEmpty(serverAddress, postEndpoint, getEndpoint) )
            throw new Error("\n\n----Error source:\tNetworkController:setConnectionParams");

        int timeout = 120;

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout (timeout, TimeUnit.SECONDS)
                .readTimeout    (timeout, TimeUnit.SECONDS)
                .writeTimeout   (timeout, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);
        AndroidNetworking.enableLogging();
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.HEADERS);
    }
    private boolean isAnyParameterEmpty (String... parameters)
    {
        //--for each given parameters
        for (String parameter : parameters)
        {
            //--check whether it is empty, if so return true - params are empty
            if( parameter.isEmpty() )
                return true;
        }
        //--else return false - all of them are valid, are NOT EMPTY
        return false;
    }

    private StringRequestListener getListenerFor  (final String parentName)
    {
        StringRequestListener listener = new StringRequestListener()
        {
            @Override
            public void onResponse(String response)
            {
                Log.i(parentName, response.toString());
            }

            @Override
            public void onError(ANError error)
            {
                printErrorInfo(error, parentName);
            }
        };

        return listener;
    }
    private void                  printErrorInfo  (ANError error, String tag)
    {
        Log.i( "\n\t\t ---------","We have a error in network connection");
        Log.i( tag, error.toString()                            );
        Log.i( tag, error.getErrorBody()                        );
        Log.i( tag, Integer.toString( error.getErrorCode() )    );
    }

    private void createDirForDownloadedFilesIfNoExist()
    {
        String appPath  = getApplicationContext().getFilesDir().getPath();
        tempDirPath     = appPath + "/downloadedFiles";

        File tempDir = new File(tempDirPath);

        if ( tempDir.exists() )
            System.out.println("Dir exist");
        else
        {
            if( tempDir.mkdir() )
                System.out.println("Dir created");
            else
                throw new Error("Can not create temp dir");
        }
    }
}
