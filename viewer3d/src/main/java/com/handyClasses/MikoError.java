package com.handyClasses;

public class MikoError extends Error
{
    public MikoError(String className, String functionName, String description)
    {
        throwError(className, functionName, description);
    }
    public MikoError(Object object, String functionName, String description)
    {
        String className = object.getClass().getName();

        throwError(className, functionName, description);

    }
    private String throwError   ( String className, String functionName, String description )
    {
        final String errorHeader        = "\n\n ------- ERROR -------"
                    , sourcePrefix      = "\n --- source:\t\t"
                    , descriptionPrefix = "\n --- description:\t";

        String func     = sourcePrefix      + className + " : " + functionName;
        String desc     = descriptionPrefix + description;
        String message  = errorHeader       + func + desc;

        throw new Error(message);
    }
}
