package com.example.networkcontroller;

public class ConnectionProblemMessage
{
    public ConnectionProblemMessage(ConnectionProblemType problemType
                                    , String problemDescription )
    {
        this.problemType        = problemType;
        this.problemDescription = problemDescription;
    }

    private final ConnectionProblemType problemType;
    private final String                problemDescription;
}
