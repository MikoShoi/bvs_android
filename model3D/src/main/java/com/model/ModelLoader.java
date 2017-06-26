package com.model;

import android.content.Context;
import android.util.Pair;

import com.example.handytools.FileReader;
import com.example.handytools.MikoError;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

public class ModelLoader
{
    public ModelLoader(Context context)
    {
        this.context    = context;
        separatorInText = " ";
    }

    public ModelData load(int rIdVertShader, int rIdFragShader, String modelFilePath)
    {
        ModelData modelData = new ModelData();

        loadMesh(modelFilePath, modelData);
        loadShaders(rIdVertShader, rIdFragShader,modelData);

        //-- needed to compare two models
        modelData.setHashCode(modelFilePath);

        return modelData;
    }

    private void    loadMesh                (String modelFilePath, ModelData modelData)
    {
        File modelFile = new File(modelFilePath);

        if( !modelFile.exists() )
        {
            throw new MikoError( this
                    , "loadMesh"
                    , "file: " + modelFilePath + " - no exist." );
        }

        List<String> modelFileContent = FileReader.getFileContent(modelFile);
        if( isFileContentValid(modelFileContent) )
        {
            //--divide model data into 2 part |First part - index data | Second part - vertex data
            Pair< List<String>, List<String> > dividedData = getSeparatedData(modelFileContent);

//            loadIndexBufferFromText (dividedData.first);
            loadVertexBufferFromText(dividedData.second, modelData);
        }
    }
    private void    loadShaders             (int rIdVertShader, int rIdFragShader, ModelData modelData)
    {
        String v = FileReader.getFileContent(rIdVertShader, context);
        String f = FileReader.getFileContent(rIdFragShader, context);

        modelData.setVertShaderCode(v);
        modelData.setFragShaderCode(f);
    }
    private void    loadVertexBufferFromText(List<String> vertexData, ModelData modelData)
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
                    , "loadVertexBufferFromText"
                    , "probably out of range in for loop");
        }
    }
    private void    loadIndexBufferFromText (List<String> data, ModelData modelData)
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
                    , "loadIndexBufferFromText"
                    , "probably out of range in for loop" );
        }
    }
    private int     getNumberOfVertices     (List<String> modelData)
    {
        String header           = modelData.get(0);
        String numberOfVertices = header.split(separatorInText)[1];

        return Integer.parseInt(numberOfVertices);
    }
    private boolean isFileContentValid      (List<String> modelFileData)
    {
        final boolean FILE_CONTENT_IS_VALID   = true
                    , FILE_CONTENT_IS_INVALID = false;

        if ( !modelFileData.isEmpty() )
        {
            //TODO: what about reg expr instead string "OFF" ?
            final String headerPattern  = "OFF";
            String header               = modelFileData.get(0);

            if( header.contains(headerPattern) )
            {
                return FILE_CONTENT_IS_VALID;
            }
        }

        return FILE_CONTENT_IS_INVALID;
    }
    private Pair< List<String>, List<String> >  getSeparatedData    (List<String> modelData)
    {
        final int vertexFrom    = 1
                , vertexTo      = vertexFrom + getNumberOfVertices(modelData)
                , indexFrom     = vertexTo
                , indexTo       = modelData.size();

        List<String> indexData   = modelData.subList(indexFrom   , indexTo);
        List<String> vertexData  = modelData.subList(vertexFrom  , vertexTo);

        return Pair.create(indexData, vertexData);
    }

    private final   Context     context;
    private final   String      separatorInText;
}
