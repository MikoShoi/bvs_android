package com.example.mikotools;

import android.content.res.Resources;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class FileReader
{
  private FileReader()
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
  public void   setResources        (Resources resources)
  {
    this.resources = resources;
  }
  public String getShaderSourceCode (int rIdShader)
  {
    if (resources == null)
    {
      MikoLogger.log("no handle to resources. Did you use setResourcer() ?");
      return "";
    }

    try
    {
      InputStream stream  = resources.openRawResource(rIdShader);

      return IOUtils.toString(stream, "UTF-8");
    }
    catch (IOException e)
    {
      MikoLogger.log("error while reading shader file");
      return "";
    }
  }

  private Resources resources = null;
  private static FileReader fileReader = null;
}
