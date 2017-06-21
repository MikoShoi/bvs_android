package com.model;

public class AddModelToRenderMessage
{
    public AddModelToRenderMessage  ( int rIdVertShader
                                    , int rIdFragShader
                                    , String modelFilePath
                                    , String modelUniqueName )
    {
        this.rIdVertShader      = rIdVertShader;
        this.rIdFragShader      = rIdFragShader;
        this.modelFilePath      = modelFilePath;
        this.modelUniqueName    = modelUniqueName;
    }

    public int      getrIdVertShader    ()
    {
        return rIdVertShader;
    }
    public int      getrIdFragShader    ()
    {
        return rIdFragShader;
    }
    public String   getModelUniqueName  ()
    {
        return modelUniqueName;
    }
    public String   getModelFilePath    ()
    {
        return modelFilePath;
    }

    private int     rIdVertShader, rIdFragShader;
    private String  modelFilePath, modelUniqueName;
}
