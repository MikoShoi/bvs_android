package com.model;

import com.example.mikotools.MikoError;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ModelData
{
  ModelData ()
  {

  }

  FloatBuffer getVertexBuffer            ()
  {
      if( vertexBuffer == null )
      {
          throw new MikoError( this
                  , "getVertexBuffer"
                  , "you are getting empty vertex buffer");
      }

      return vertexBuffer;
  }
  IntBuffer   getIndexBuffer             ()
  {
      if( indexBuffer == null )
      {
          throw new MikoError( this
                  , "getIndexBuffer"
                  , "you are getting empty index buffer");
      }

      return indexBuffer;
  }
  int         getHashCode                ()
  {
      if( nameHashCode == 0 )
      {
          throw new MikoError( this
                  , "getHashCode"
                  , "you are getting not set nameHashCode");
      }

      return nameHashCode;
  }
  String      getFragShaderCode          ()
  {
      if( fragShaderCode.isEmpty() )
      {
          throw new MikoError( this
                  , "getFragShaderCode"
                  , "fragment shader source code is empty");
      }

      return fragShaderCode;
  }
  String      getVertShaderCode          ()
  {
      if( vertShaderCode.isEmpty() )
      {
          throw new MikoError( this
                  , "getVertShaderCode"
                  , "vertex shader source code is empty");
      }

      return vertShaderCode;
  }
  int         getVertexBufferSizeInBytes ()
  {
      if( vertexBufferSizeInBytes == 0 )
      {
          throw new MikoError( this
                  , "getVertexBufferSizeInBytes"
                  , "vertex buffer is empty, so it size is equal 0");
      }

      return vertexBufferSizeInBytes;
  }
  int         getIndexBufferSizeInBytes  ()
  {
      if( indexBufferSizeInBytes == 0 )
      {
          throw new MikoError( this
                  , "getIndexBufferSizeInBytes"
                  , "index buffer is empty, so it size is equal 0");
      }

      return indexBufferSizeInBytes;
  }
  int         getIndicesNumber           ()
  {
      if( indicesNumber == 0 )
      {
          throw new MikoError( this
                  , "getIndicesNumber"
                  , "index buffer is empty, so indices number is equal 0");
      }

      return indicesNumber;
  }
  int         getVerticesNumber          ()
  {
      if( verticesNumber == 0 )
      {
          throw new MikoError( this
                  , "getVerticesNumber"
                  , "vertex buffer is empty, so vertices number is equal 0");
      }

      return verticesNumber;
  }

  void setHashCode       (String uniqueName)
  {
      this.name = uniqueName;
      this.nameHashCode = this.name.hashCode();
  }
  void setVertexBuffer   (FloatBuffer vertexBuffer)
  {
      this.vertexBufferSizeInBytes    = vertexBuffer.capacity() * FLOAT_SIZE_IN_BYTES;
      this.verticesNumber             = vertexBuffer.capacity() / 3;

      this.vertexBuffer               = vertexBuffer;
      this.vertexBuffer.position(0);

  }
  void setIndexBuffer    (IntBuffer indexBuffer)
  {
      this.indexBufferSizeInBytes = indexBuffer.capacity() * INT_SIZE_IN_BYTES;
      this.indicesNumber          = indexBuffer.capacity();

      this.indexBuffer            = indexBuffer;
      this.indexBuffer.position(0);
  }
  void setVertShaderCode (String vertShaderCode)
  {
      this.vertShaderCode = vertShaderCode;
  }
  void setFragShaderCode (String fragShaderCode)
  {
      this.fragShaderCode = fragShaderCode;
  }

  final   int FLOAT_SIZE_IN_BYTES     = Float.SIZE    / 8
            , INT_SIZE_IN_BYTES       = Integer.SIZE  / 8
//            , ELEMENTS_PER_NORMAL     = 3
            , ELEMENTS_PER_POSITION   = 3
            , ELEMENTS_PER_VERTEX     = ELEMENTS_PER_POSITION
            , INDICES_PER_TRIANGLE    = 3
            , VERTEX_STRIDE           = 3 * FLOAT_SIZE_IN_BYTES
//            , VERTEX_NORMAL_OFFSET    = 3 * FLOAT_SIZE_IN_BYTES
            , VERTEX_POSITION_OFFSET  = 0;

  private int indicesNumber           = 0
            , verticesNumber          = 0
            , indexBufferSizeInBytes  = 0
            , vertexBufferSizeInBytes = 0
            , nameHashCode            = 0;

  private String  name
                , vertShaderCode    = null
                , fragShaderCode    = null;

  private IntBuffer   indexBuffer   = null;
  private FloatBuffer vertexBuffer  = null;

  //TODO: improve this code
}
