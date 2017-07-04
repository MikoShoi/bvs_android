package com.model;

import com.example.mikotools.FileReader;
import com.example.mikotools.MikoError;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

class ModelLoader
{
  ModelLoader ()
  {
    modelData       = new ModelData();
    fileReader      = FileReader.getInstance();
    separatorInText = " ";
  }

  final ModelData load(String modelFilePath, int rIdVertShader, int rIdFragShader)
  {
    loadMesh    (modelFilePath);
    loadShaders (rIdVertShader, rIdFragShader);

    //-- needed for compare two models
    modelData.setHashCode(modelFilePath);

    return modelData;
  }

  private void    loadMesh            (String modelFilePath)
  {
    List<String> modelFileContent = fileReader.getTextFileContent(modelFilePath);

    if( isFileContentValid(modelFileContent) )
    {
      //--divide model data into 2 part |First part - index data | Second part - vertex data

      List<String> vertexData = separateVerticesAndIndices(modelFileContent);

//      fillIndexBuffer (indexData);
      fillVertexBuffer(vertexData);
    }
  }
  private void    loadShaders         (int rIdVertShader, int rIdFragShader)
  {
    String  vertShader = fileReader.getShaderSourceCode(rIdVertShader)
            , fragShader = fileReader.getShaderSourceCode(rIdFragShader);

    modelData.setVertShaderCode(vertShader);
    modelData.setFragShaderCode(fragShader);
  }
  private void    fillVertexBuffer    (List<String> vertexData)
  {
    final int neededAllocationSize  = vertexData.size()
                                        * modelData.ELEMENTS_PER_VERTEX
                                        * modelData.FLOAT_SIZE_IN_BYTES;

    FloatBuffer buffer = ByteBuffer
                          .allocateDirect(neededAllocationSize)
                          .order( ByteOrder.nativeOrder() )
                          .asFloatBuffer();
    buffer.position(0);

    try
    {
      for (String line : vertexData)
      {
        String[] coordinates = line.split(separatorInText);

        for(int i = 0; i < modelData.ELEMENTS_PER_VERTEX; i++)
        {
          float coordinate = Float.valueOf( coordinates[i] );
          buffer.put(coordinate);
        }
      }
      modelData.setVertexBuffer(buffer);
    }
    catch (Exception e)
    {
      throw new MikoError(this
                        , "fillVertexBuffer"
                        , "probably out of range in for loop");
    }
  }
  private void    fillIndexBuffer     (List<String> data)
  {
    final int neededAllocationSize  = data.size()
                                        * modelData.INDICES_PER_TRIANGLE
                                        * modelData.INT_SIZE_IN_BYTES;

    IntBuffer buffer = ByteBuffer
                        .allocateDirect(neededAllocationSize)
                        .order( ByteOrder.nativeOrder() )
                        .asIntBuffer();
    buffer.position(0);

    try
    {
      for (String line : data)
      {
        String[] coordinates = line.split(separatorInText);

        //TODO: repair
//                for(int i = 0; i < modelData.INDICES_PER_TRIANGLE; i++)
        for(int i = 1; i < 4; i++)
        {
          int coordinate = Integer.valueOf( coordinates[i] );
          buffer.put(coordinate);
        }
      }
      modelData.setIndexBuffer(buffer);
    }
    catch (Exception e)
    {
      throw new MikoError(this
                        , "fillIndexBuffer"
                        , "probably out of range in for loop" );
    }
  }
  private int     getNumberOfVertices (List<String> modelFileContent)
  {
    String header           = modelFileContent.get(0);
    String numberOfVertices = header.split(separatorInText)[1];

    return Integer.parseInt(numberOfVertices);
  }
  private boolean isFileContentValid  (List<String> modelFileContent)
  {
    if ( !modelFileContent.isEmpty() )
    {
      final String  headerPattern = "OFF"
                  , header        = modelFileContent.get(0);

      if( header.contains(headerPattern) )
      {
        return true;
      }
    }

    return false;
  }
  private List<String> separateVerticesAndIndices (final  List<String> modelData)
  {
    final int vertexFrom  = 1
            , vertexTo    = vertexFrom + getNumberOfVertices(modelData);
//            , indexFrom   = vertexTo
//            , indexTo     = modelData.size();
//
//    indexData  = modelData.subList(indexFrom   , indexTo);
    return modelData.subList(vertexFrom, vertexTo);
  }

  private ModelData     modelData;
  private FileReader    fileReader;
  private final String  separatorInText;
}
