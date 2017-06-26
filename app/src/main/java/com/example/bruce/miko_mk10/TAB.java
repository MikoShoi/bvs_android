package com.example.bruce.miko_mk10;

public enum TAB
{
    INSTRUCTIONS
    , DOCUMENTS
    , CAMERA
    , VIEWER_3D
    , PRELOADER
    , WELCOME_SCREEN
    , NO_CONNECTION_SCREEN;

    public int index()
    {
        switch (this)
        {
            case INSTRUCTIONS:
                return 0;
            case CAMERA:
                return 1;
            case DOCUMENTS:
                return 2;
            case VIEWER_3D:
                return 3;
            case PRELOADER:
                return 4;
            case WELCOME_SCREEN:
                return 5;
            case NO_CONNECTION_SCREEN:
                return 6;
            default:
                return 0;
        }
    }
}
