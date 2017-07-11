package com.example.bruce.bvs;

enum Tab
{
    INSTRUCTIONS
    , DOCUMENTS
    , CAMERA
    , VIEWER_3D
    , LOADER
    , WELCOME
    , INTERNER_CONNECTION_UNAVAILABLE;

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
            case INTERNER_CONNECTION_UNAVAILABLE:
                return 5;
            default:
                return 0;
        }
    }
}
