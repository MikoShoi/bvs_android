package com.model;

import com.example.handytools.MikoError;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ModelData
{
    public ModelData    ()
    {
        vertexBuffer    = null;
        indexBuffer     = null;
    }

    public ModelData    ( String        vertShaderCode
                        , String        fragShaderCode
                        , String        name
                        , IntBuffer     indexBuffer
                        , FloatBuffer   vertexBuffer )
    {
        setVertShaderCode(vertShaderCode);
        setFragShaderCode(fragShaderCode);

        setNameAndHashCode(name);

        setIndexBuffer(indexBuffer);
        setVertexBuffer(vertexBuffer);
    }

    public FloatBuffer  getVertexBuffer             ()
    {
        if( vertexBuffer == null )
        {
            throw new MikoError( this
                    , "getVertexBuffer"
                    , "you are getting empty vertex buffer");
        }

        return vertexBuffer;
    }
    public IntBuffer    getIndexBuffer              ()
    {
        if( indexBuffer == null )
        {
            throw new MikoError( this
                    , "getIndexBuffer"
                    , "you are getting empty index buffer");
        }

        return indexBuffer;
    }
    public int          getNameHashCode             ()
    {
        if( nameHashCode == 0 )
        {
            throw new MikoError( this
                    , "getNameHashCode"
                    , "you are getting not set nameHashCode");
        }

        return nameHashCode;
    }
    public String       getFragShaderCode           ()
    {
        if( fragShaderCode.isEmpty() )
        {
            throw new MikoError( this
                    , "getFragShaderCode"
                    , "fragment shader source code is empty");
        }

        return fragShaderCode;
    }
    public String       getVertShaderCode           ()
    {
        if( vertShaderCode.isEmpty() )
        {
            throw new MikoError( this
                    , "getVertShaderCode"
                    , "vertex shader source code is empty");
        }

        return vertShaderCode;
    }
    public int          getVertexBufferSizeInBytes  ()
    {
        if( vertexBufferSizeInBytes == 0 )
        {
            throw new MikoError( this
                    , "getVertexBufferSizeInBytes"
                    , "vertex buffer is empty, so it size is equal 0");
        }

        return vertexBufferSizeInBytes;
    }
    public int          getIndexBufferSizeInBytes   ()
    {
        if( indexBufferSizeInBytes == 0 )
        {
            throw new MikoError( this
                    , "getIndexBufferSizeInBytes"
                    , "index buffer is empty, so it size is equal 0");
        }

        return indexBufferSizeInBytes;
    }
    public int          getIndicesNumber            ()
    {
        if( indicesNumber == 0 )
        {
            throw new MikoError( this
                    , "getIndicesNumber"
                    , "index buffer is empty, so indices number is equal 0");
        }

        return indicesNumber;
    }
    public int          getVerticesNumber           ()
    {
        if( verticesNumber == 0 )
        {
            throw new MikoError( this
                    , "getVerticesNumber"
                    , "vertex buffer is empty, so vertices number is equal 0");
        }

        return verticesNumber;
    }


    public void setNameAndHashCode  (String uniqueName)
    {
        this.name = uniqueName;
        this.nameHashCode = this.name.hashCode();
    }
    public void setVertexBuffer     (FloatBuffer vertexBuffer)
    {
        this.vertexBufferSizeInBytes    = vertexBuffer.capacity() * FLOAT_SIZE_IN_BYTES;
        this.verticesNumber             = vertexBuffer.capacity() / 3;

        this.vertexBuffer               = vertexBuffer;
        this.vertexBuffer.position(0);

    }
    public void setIndexBuffer      (IntBuffer indexBuffer)
    {
        this.indexBufferSizeInBytes = indexBuffer.capacity() * INT_SIZE_IN_BYTES;
        this.indicesNumber          = indexBuffer.capacity();

        this.indexBuffer            = indexBuffer;
        this.indexBuffer.position(0);
    }
    public void setVertShaderCode   (String vertShaderCode)
    {
        this.vertShaderCode = vertShaderCode;
    }
    public void setFragShaderCode   (String fragShaderCode)
    {
        this.fragShaderCode = fragShaderCode;
    }

    private int indicesNumber
                , verticesNumber
                , indexBufferSizeInBytes
                , vertexBufferSizeInBytes
                , nameHashCode;

    private String name
            , vertShaderCode
            , fragShaderCode;

    private IntBuffer    indexBuffer;
    private FloatBuffer  vertexBuffer;

    final int   FLOAT_SIZE_IN_BYTES         = Float.SIZE    / 8
                , INT_SIZE_IN_BYTES         = Integer.SIZE  / 8
//                , ELEMENTS_PER_NORMAL       = 3
                , ELEMENTS_PER_POSITION     = 3
                , ELEMENTS_PER_VERTEX       = ELEMENTS_PER_POSITION
                , INDICES_PER_TRIANGLE      = 3
                , VERTEX_STRIDE             = 3 * FLOAT_SIZE_IN_BYTES
//                , VERTEX_NORMAL_OFFSET      = 3 * FLOAT_SIZE_IN_BYTES
                , VERTEX_POSITION_OFFSET    = 0;

    //TODO: improve this code
}
