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
            case PRELOADER:
                return 3;
            case WELCOME_SCREEN:
                return 4;
            case NO_CONNECTION_SCREEN:
                return 5;
            default:
                return 0;
        }
    }
}
