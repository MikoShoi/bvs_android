package com.model;

import android.opengl.GLES30;

import com.example.handytools.MikoError;

public class Model
{
    public Model(ModelData modelData)
    {
        this.modelData = modelData;

        program = new OesProgram();
        isItFirstDrawing = true;
    }

    public void draw(RenderMatrices matrices)
    {
        if (isItFirstDrawing) prepareToFirstDraw();

        program.active();

        //--active vao
        GLES30.glBindVertexArray(vao);

        //--prepare uniforms mvpMatrix, mvMatrix, normalMatrix and so on.
        prepareUniforms(matrices);

        //--draw elements
        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, modelData.getVerticesNumber());

        //        GLES30.glDrawElements( GLES30.GL_POINTS
        //                , modelData.getIndicesNumber()
        //                , GLES30.GL_UNSIGNED_INT
        //                , 0 );

        //--Reset to the default VAO
        GLES30.glBindVertexArray(0);
    }

    protected void setUniform(String name, float[] data)
    {
        int location = GLES30.glGetUniformLocation(program.getOesHandle(), name);

        GLES30.glUniformMatrix4fv(location, 1, false, data, 0);
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj.hashCode() == this.modelData.getHashCode();
    }

    @Override
    public int hashCode()
    {
        return modelData.getHashCode();
    }

    protected void prepareUniforms(RenderMatrices matrices)
    {
//        setUniform( "mv"       , matrices.getMv() );
        setUniform( "mvp"      , matrices.getView() );
//        setUniform( "normal"   , matrices.getNormal() );
    }

    private void    prepareToFirstDraw  ()
    {
        initProgram();
//        initIndexVbo();
        initVertexVbo();
        initVao();

        isItFirstDrawing = false;
    }
    private void    initVertexVbo       ()
    {
        vertexVbo = generateId(OesObjectType.VBO);

        GLES30.glBindBuffer( GLES30.GL_ARRAY_BUFFER, vertexVbo );
        GLES30.glBufferData( GLES30.GL_ARRAY_BUFFER
                , modelData.getVertexBufferSizeInBytes()
                , modelData.getVertexBuffer()
                , GLES30.GL_STATIC_DRAW );
    }
    private void    initIndexVbo        ()
    {
        indexVbo = generateId(OesObjectType.VBO);

        GLES30.glBindBuffer( GLES30.GL_ELEMENT_ARRAY_BUFFER, indexVbo);
        GLES30.glBufferData( GLES30.GL_ELEMENT_ARRAY_BUFFER
                , modelData.getIndexBufferSizeInBytes()
                , modelData.getIndexBuffer()
                , GLES30.GL_STATIC_DRAW );
    }
    private void    initVao             ()
    {
        //--get handle to new vao object and tell opengl that now you operate on him
        vao = generateId(OesObjectType.VAO);
        GLES30.glBindVertexArray(vao);

        //--turn on vertex vbo
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexVbo);

        //--enable vertex vbo and show opengl how to read position attribute from him
        GLES30.glEnableVertexAttribArray( 3 );
        GLES30.glVertexAttribPointer    ( 3
                , modelData.ELEMENTS_PER_POSITION
                , GLES30.GL_FLOAT
                , false
                , modelData.VERTEX_STRIDE
                , modelData.VERTEX_POSITION_OFFSET );

        //--again with normal coordinates attribute
//        GLES30.glEnableVertexAttribArray( 4 );
//        GLES30.glVertexAttribPointer    ( 4
//                , modelData.ELEMENTS_PER_NORMAL
//                , GLES30.GL_FLOAT
//                , false
//                , modelData.VERTEX_STRIDE
//                , modelData.VERTEX_NORMAL_OFFSET );

        //--tell opengl that index vbo should be turned on too
//        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, indexVbo);

        //--finally release vao
        GLES30.glBindVertexArray(0);
    }
    private void    initProgram         ()
    {
        program = new OesProgram();
        program.addShaders  ( modelData.getVertShaderCode()
                            , modelData.getFragShaderCode() );
    }
    private int     generateId          (OesObjectType objectType)
    {
        //--make place
        int[] buffers = new int[1];

        //--generate one buffer
        switch (objectType)
        {
            case VAO:
                GLES30.glGenVertexArrays(1, buffers, 0);
                break;
            case VBO:
                GLES30.glGenBuffers     (1, buffers, 0);
                break;
            default:
                throw new MikoError(this, "generateId" ,"unknown OesObjectType");
        }

        //-- and return handle/id to it
        return buffers[0];
    }

    protected ModelData         modelData;
    protected OesProgram        program;

    private boolean     isItFirstDrawing;
    private int         vao
                        , indexVbo
                        , vertexVbo;
}
