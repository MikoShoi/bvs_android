package com.example.mikotools;

import android.content.res.Resources;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FileReader
{
  FileReader()
  {

  }

  public static FileReader getInstance()
  {
    if (fileReader == null)
    {
      fileReader = new FileReader();
    }
    return fileReader;
  }

  public void         setResources        (Resources resources)
  {
    this.resources = resources;
  }

  public String       getShaderSourceCode (int rIdShader)
  {
    if ( resources == null )
    {
      throw new MikoError(this
                        , "getShaderSourceCode"
                        , "no handle to resources");
    }

    try
    {
      InputStream stream  = resources.openRawResource(rIdShader);

      return IOUtils.toString(stream, encoding);
    }
    catch (IOException e)
    {
      throw new MikoError( "FileReader"
                          , "getTextFileContent"
                          , "error while reading shader file" );
    }
  }
  public List<String> getTextFileContent  (String modelFilePath)
  {
    try
    {
      File modelFile = new File(modelFilePath);

      if( !modelFile.exists() )
      {
        throw new MikoError( this
                , "loadMesh"
                , "file: " + modelFilePath + " - no exist." );
      }

      FileInputStream stream = new FileInputStream(modelFile);

      return IOUtils.readLines(stream, encoding);
    }
    catch (IOException e)
    {
      throw new MikoError ( "FileReader"
                          , "getTextFileContent"
                          , "error while reading model file" );
    }
  }

  private static FileReader fileReader = null;
  private final String encoding = "UTF-8";
  private Resources resources = null;
}
