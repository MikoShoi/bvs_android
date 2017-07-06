package com.example.bruce.bvs;

public enum Tab
{
    INSTRUCTIONS
    , DOCUMENTS
    , CAMERA
    , VIEWER_3D
    , LOADER
    , WELCOME
    , NO_CONNECTION;

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
            case LOADER:
                return 3;
            case WELCOME:
                return 4;
            case NO_CONNECTION:
                return 5;
            default:
                return 0;
        }
    }
}
