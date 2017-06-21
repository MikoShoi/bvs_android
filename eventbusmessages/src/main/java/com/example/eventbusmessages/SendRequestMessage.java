package com.example.eventbusmessages;

public class SendRequestMessage
{
    private RequestType requestType;
    private String      address;

    public SendRequestMessage(RequestType requestType, String address)
    {
        this.requestType    = requestType;
        this.address        = address;
    }

    public RequestType getRequestType()
    {
        return requestType;
    }
    public String getAddress()
    {
        return address;
    }
}