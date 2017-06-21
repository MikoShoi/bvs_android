package com.example.bruce.miko_mk10;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AppConfigManager
{
    private Context context;
    private SharedPreferences config;
    private SharedPreferences.Editor configEditor;

    private static final String CONFIG_FILE_NAME        = "mikoFileConfig";
    private static final String CONFIG_PROPERTY_NAME    = "IsFirstTimeLaunch";

    public AppConfigManager(Context context)
    {
        this.context    = context;
        config          = context.getSharedPreferences(CONFIG_FILE_NAME, Activity.MODE_PRIVATE);
        configEditor    = config.edit();
    }

    public void setFirstTimeLaunch(boolean b)
    {
        configEditor.putBoolean(CONFIG_PROPERTY_NAME, b);
        configEditor.commit();
    }

    public boolean isAppFirstTimeLaunch()
    {
        //--default value - used in case key has not assigned any value
        boolean defaultValue = true;

        return config.getBoolean(CONFIG_PROPERTY_NAME, defaultValue);
    }
}
