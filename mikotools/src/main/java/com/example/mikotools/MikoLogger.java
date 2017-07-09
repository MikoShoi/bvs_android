package com.example.mikotools;

public class MikoLogger
{
  public static void log(String whatIsHappening)
  {
    StackTraceElement ste = Thread.currentThread().getStackTrace()[3];

    String  methodName  = ste.getMethodName()
            , className   = ste.getClassName()
            , logMessage  = "\n------------- MIKO LOGGER"
            + "\n-- description:\t" + whatIsHappening
            + "\n-- source:\t\t"    + className + "\t" + methodName;

    System.out.println(logMessage);
  }
}
