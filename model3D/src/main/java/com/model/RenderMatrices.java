package com.model;

import com.example.handytools.MikoMath;
import com.example.handytools.Vector3D;

public class RenderMatrices
{
    public RenderMatrices()
    {
        model       = MikoMath.getIndentityMatrix4f();
        view        = MikoMath.getIndentityMatrix4f();
        projection  = MikoMath.getIndentityMatrix4f();

        normal      = MikoMath.getIndentityMatrix4f();
        mv          = MikoMath.getIndentityMatrix4f();
        mvp         = MikoMath.getIndentityMatrix4f();
    }

    public float[] getModel     ()
    {
        return model;
    }
    public float[] getView      ()
    {
        return view;
    }
    public float[] getProjection()
    {
        return projection;
    }
    public float[] getMv        ()
    {
        return mv;
    }
    public float[] getMvp       ()
    {
        return mvp;
    }
    public float[] getNormal    ()
    {
        return normal;
    }

    public void setViewAndProjection(float[] v, float[] p)
    {
        view        = v;
        projection  = p;
    }
    public void recalculate         ()
    {
        mv      = MikoMath.multiplyMatrices     ( view      , model);
        mvp     = MikoMath.multiplyMatrices     ( projection, mv);
        normal  = MikoMath.calculateNormalMatrix( mv);
    }

    private float[] model
                    , view
                    , projection
                    , normal
                    , mv
                    , mvp;

    private final int offset = 0;
}
