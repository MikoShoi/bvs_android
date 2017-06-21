package com.example.handytools;

import android.content.Context;
import android.content.res.Resources;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FileReader
{
    public static String       getFileContent(int rIdShader, Context context)
    {
        try
        {
            Resources resources = context.getResources();
            InputStream stream  = resources.openRawResource(rIdShader);
            String shaderCode   = IOUtils.toString(stream, encoding);

            return shaderCode;
        }
        catch (IOException e)
        {
            throw new MikoError( "FileReader"
                                , "getFileContent"
                                , "error while reading shader file" );
        }
    }
    public static List<String> getFileContent(File modelFile)
    {
        try
        {
            FileInputStream stream      = new FileInputStream(modelFile);
            List<String> fileContent    = IOUtils.readLines(stream, encoding);

            return fileContent;
        }
        catch (IOException e)
        {
            throw new MikoError ( "FileReader"
                    , "getFileContent"
                    , "error while reading model file" );
        }
    }

    private static final String encoding = "UTF-8";
}
