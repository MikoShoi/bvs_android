package com.model;

import com.handyClasses.MikoMath;
import com.handyClasses.Vector3D;

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

    public void setViewAndProjection    (float[] v, float[] p)
    {
        view        = v;
        projection  = p;
    }
    public void rotateModel             (float angle, Vector3D v)
    {
        model = MikoMath.getRotatedMatrix4f(angle, v);
    }
    public void recalculate()
    {
        mv      = MikoMath.multiplyMatrices     ( view      , model);
        mvp     = MikoMath.multiplyMatrices     ( projection, mv);
        normal  = MikoMath.calculateNormalMatrix( mv);
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

    private float[] model
                    , view
                    , projection
                    , normal
                    , mv
                    , mvp;

    private final int offset = 0;
}
