package com.example.mikotools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;

public class AppManager
{
  public AppManager()
  {
    final String tempDirPath = getPublicDownloadDirPath() + "/bvsTempDir";

    tempDir = new File(tempDirPath);
    if( !tempDir.exists() && !tempDir.mkdir() )
    {
        throw new MikoError ( this
                , "constructor"
                , "can not create temp dir: ");
    }
  }

  public  boolean  isAppFirstTimeLaunch   (Context context)
  {
    final String  FILE_NAME     = "bvsAppConfig"
                , PROPERTY_NAME = "firstLaunch";

    SharedPreferences config  = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
    boolean isThatFirstLaunch = config.getBoolean(PROPERTY_NAME, true);

    if ( isThatFirstLaunch )
        config.edit().putBoolean(PROPERTY_NAME, false).apply();

    return isThatFirstLaunch;
  }
  public  void     cleanTempDirContent    ()
  {
    for ( File file : tempDir.listFiles() )
      file.delete();
  }
  public  String   getTempDirPath         ()
  {
    return tempDir.getAbsolutePath();
  }

  private String getPublicDownloadDirPath ()
  {
    String path = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString();
    return path;
  }

  private final File tempDir;
}
