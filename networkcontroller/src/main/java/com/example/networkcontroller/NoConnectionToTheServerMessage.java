package com.example.networkcontroller;

public class NoConnectionToTheServerMessage
{
    public NoConnectionToTheServerMessage(String errorDescription)
    {
        this.errorDescription = errorDescription;
    }

    public String getErrorDescription()
    {
        return errorDescription;
    }

    private final String errorDescription;
}
