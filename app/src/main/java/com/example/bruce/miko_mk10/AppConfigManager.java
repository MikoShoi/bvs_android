package com.example.bruce.miko_mk10;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AppConfigManager
{
    public AppConfigManager(Context context)
    {
        config = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
    }

    public void     setFirstTimeLaunch  (boolean b)
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

    private SharedPreferences config;
    private static final String FILE_NAME       = "mikoAppConfig"
                                , PROPERTY_NAME = "firstAppLaunch";
}
