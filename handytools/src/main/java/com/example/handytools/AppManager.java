package com.example.handytools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;

public class AppManager
{
    public AppManager(Context context)
    {
        this.tempDirPath = "";
        this.context     = context;
        config = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
    }

    private void     setFirstTimeLaunch  (boolean b)
    {
        config.edit().putBoolean(PROPERTY_NAME, b).apply();
    }
    public boolean  isAppFirstTimeLaunch()
    {
        boolean isThatFirstLaunch = config.getBoolean(PROPERTY_NAME, true);

        if ( isThatFirstLaunch )
            setFirstTimeLaunch(false);

        return isThatFirstLaunch;
    }

    public void     cleanTempDirContent()
    {
        if ( !tempDirPath.isEmpty() )
        {
            File tempDir = new File(tempDirPath);

            for ( File file : tempDir.listFiles() )
                file.delete();
        }
    }
    public String getTempDirPath()
    {
        if ( tempDirPath == null )
        {
            tempDirPath  = context.getFilesDir() + "/tempDir";
            File tempDir = new File(tempDirPath);

            if( !tempDir.exists() && !tempDir.mkdir() )
            {
                throw new MikoError ( this
                        , "getTempDirPath"
                        , "can not create temp dir: ");
            }
        }

        return tempDirPath;
    }

    private Context context;
    private String  tempDirPath;
    private SharedPreferences config;
    private static final String FILE_NAME       = "mikoAppConfig"
                                , PROPERTY_NAME = "firstAppLaunch";
}
