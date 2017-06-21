package com.model;

public class Model3D extends Model
{
    public Model3D(ModelData modelData)
    {
        super(modelData);
    }

    @Override
    protected void prepareUniforms  (RenderMatrices matrices)
    {
        setUniform( UniformType.VECTOR_4F, "mv"       , matrices.getMv() );
        setUniform( UniformType.VECTOR_4F, "mvp"      , matrices.getMvp() );
        setUniform( UniformType.VECTOR_4F, "normal"   , matrices.getNormal() );
    }

}