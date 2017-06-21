package com.model;

import android.opengl.GLES30;

public class OesProgram
{
    public OesProgram   ()
    {}

    public  int     getOesHandle    ()
    {
        return programHandle;
    }
    public  void    active          ()
    {
        GLES30.glUseProgram(programHandle);
    }
    public  void    addShaders      ( String vertShaderCode, String fragShaderCode )
    {
        programHandle = GLES30.glCreateProgram();

        //--create vertex and fragment shaders
        int vertShader   = createShader(GLES30.GL_VERTEX_SHADER,   vertShaderCode)
            , fragShader = createShader(GLES30.GL_FRAGMENT_SHADER, fragShaderCode);

        //--add them to opengl programHandle
        GLES30.glAttachShader(programHandle, vertShader);
        GLES30.glAttachShader(programHandle, fragShader);

        //--and link both to one programHandle
        GLES30.glLinkProgram(programHandle);

        //--after all delete both shaders, now they are redundant
        deleteShader(vertShader);
        deleteShader(fragShader);
    }

    private int     createShader    (int shaderType, String sourceCode)
    {
        int shader = GLES30.glCreateShader(shaderType);

        GLES30.glShaderSource   (shader, sourceCode);
        GLES30.glCompileShader  (shader);

        return shader;
    }
    private void    deleteShader    (int shader)
    {
        GLES30.glDeleteShader(shader);
    }

    private int programHandle;
}
