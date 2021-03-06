package com.example.mikotools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;

public class AppManager
{
  private AppManager()
  {
    tempDir = new File(getPublicDownloadDirPath() + "/bvsTempDir");

    if( !tempDir.exists() && !tempDir.mkdir() )
      throw new RuntimeException("can not create temp dir");
  }

  public static AppManager getInstance()
  {
    if (appManager == null)
    {
      appManager = new AppManager();
    }

    return appManager;
  }

  public  boolean isAppFirstTimeLaunch    (Context context)
  {
    final String  FILE_NAME     = "bvsAppConfig"
                , PROPERTY_NAME = "firstLaunch";

    SharedPreferences config
            = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);

    boolean isThatFirstLaunch = config.getBoolean(PROPERTY_NAME, true);

    if ( isThatFirstLaunch )
        config.edit().putBoolean(PROPERTY_NAME, false).apply();

    return isThatFirstLaunch;
  }
  public  void    cleanTempDirContent     ()
  {
    for ( File file : tempDir.listFiles() )
      file.delete();
  }
  public  String  getTempDirPath          ()
  {
    return tempDir.getAbsolutePath();
  }

  private String  getPublicDownloadDirPath()
  {
    return Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString();
  }

  private final File tempDir;
  private static AppManager appManager = null;
}
