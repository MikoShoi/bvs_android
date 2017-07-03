package com.example.mikotools;

public class MikoError extends Error
{
  public MikoError (String className, String functionName, String description)
  {
    throwError(className, functionName, description);
  }
  public MikoError  (Object object,   String functionName, String description)
  {
    String className = object.getClass().getName();

    throwError(className, functionName, description);
  }
  private String throwError (String className, String functionName, String description )
  {
    final String errorHeader  = "\n\n ------- ERROR -------"
                , srcPrefix   = "\n --- source:\t\t"
                , desPrefix   = "\n --- description:\t"
                , func        = srcPrefix   + className + " : " + functionName
                , desc        = desPrefix   + description
                , message     = errorHeader + func + desc;

    throw new Error(message);
  }
}
