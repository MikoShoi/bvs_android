package com.example.bruce.miko_mk10;

public enum TAB
{
    FIRST_LAUNCH
    , DOCUMENTS
    , CAMERA
    , VIEWER_3D
    , PRELOADER;

    public int index()
    {
        switch (this)
        {
            case FIRST_LAUNCH:
                return 0;
            case CAMERA:
                return 1;
            case DOCUMENTS:
                return 2;
            case VIEWER_3D:
                return 3;
            case PRELOADER:
                return 4;
            default:
                return 0;
        }
    }
}
