package com.model;

import com.example.mikotools.MikoLogger;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

class ModelData
{
  FloatBuffer getVertexBuffer            ()
  {
    if( vertexBuffer == null )
    {
      MikoLogger.log("you are getting empty vertex buffer");
    }

    return vertexBuffer;
  }
  IntBuffer   getIndexBuffer             ()
  {
      if( indexBuffer == null )
      {
        MikoLogger.log("you are getting empty index buffer");
      }

      return indexBuffer;
  }
  int         getVerticesNumber          ()
  {
    if( verticesNumber == 0 )
    {
      MikoLogger.log("vertex buffer is empty, so vertices number is equal 0");
    }

    return verticesNumber;
  }
  int         getIndicesNumber           ()
  {
    if( indicesNumber == 0 )
    {
      MikoLogger.log("index buffer is empty, so indices number is equal 0");
    }

    return indicesNumber;
  }
  int         getVertexBufferSizeInBytes ()
  {
    if( vertexBufferSizeInBytes == 0 )
    {
      MikoLogger.log("vertex buffer is empty, so it size is equal 0");
    }

    return vertexBufferSizeInBytes;
  }
  int         getIndexBufferSizeInBytes  ()
  {
      if( indexBufferSizeInBytes == 0 )
      {
        MikoLogger.log("index buffer is empty, so it size is equal 0");
      }

      return indexBufferSizeInBytes;
  }

  void setVertexBuffer   (float[] vertices)
  {
    final int dataByteSize = vertices.length * FLOAT_SIZE_IN_BYTES;

    FloatBuffer buffer = ByteBuffer
            .allocateDirect(dataByteSize)
            .order( ByteOrder.nativeOrder() )
            .asFloatBuffer()
            .put(vertices);
    buffer.position(0);

    this.vertexBuffer               = buffer;
    this.verticesNumber             = vertexBuffer.capacity() / 3;
    this.vertexBufferSizeInBytes    = vertexBuffer.capacity() * FLOAT_SIZE_IN_BYTES;
  }
  void setIndexBuffer    (IntBuffer indexBuffer)
  {
      this.indexBufferSizeInBytes = indexBuffer.capacity() * INT_SIZE_IN_BYTES;
      this.indicesNumber          = indexBuffer.capacity();

      this.indexBuffer            = indexBuffer;
      this.indexBuffer.position(0);
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
            , vertexBufferSizeInBytes = 0;

  private IntBuffer   indexBuffer   = null;
  private FloatBuffer vertexBuffer  = null;

  //TODO: improve this code
}
