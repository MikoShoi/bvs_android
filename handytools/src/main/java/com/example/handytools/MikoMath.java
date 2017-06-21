package com.example.handytools;

import android.opengl.Matrix;

import com.example.handytools.MikoError;

import java.util.Vector;

public class MikoMath
{
    public MikoMath()
    {}

    public static Vector3D  transformToCartesian    (Vector3D rPhiThetaVector)
    {
        float r     = rPhiThetaVector.x
            , phi   = rPhiThetaVector.y
            , theta = rPhiThetaVector.z

            , sinPhi   = (float) Math.sin(phi)
            , cosPhi   = (float) Math.cos(phi)
            , sinTheta = (float) Math.sin(theta)
            , cosTheta = (float) Math.cos(theta)

            , x = r * cosTheta * cosPhi
            , y = r * cosTheta * sinPhi
            , z = r * sinTheta;

        return new Vector3D(x, y, z);
    }
    public static float[]   multiplyMatrices        (float[] a, float[] b)
    {
        try
        {
            float[] result = new float[a.length];

            Matrix.setIdentityM ( result, offset);
            Matrix.multiplyMM   ( result, offset
                                , a     , offset
                                , b     , offset );
            return result;
        }
        catch (Exception e)
        {
            throw new MikoError ( "MikoMath"
                                , "multiplyMatrices"
                                , "error during multiplication two matrices" );
        }
    }
    public static float[]   multiplyMatrices        (float[] a, float[] b, float[] c)
    {
        float[] tempMatrix = multiplyMatrices(a, b)
                , result   = multiplyMatrices(tempMatrix, c);

        return result;
    }
    public static float[]   getRotatedMatrix4f      (float angle, Vector3D v)
    {
        float[] result = new float[16];

        Matrix.setIdentityM(result, offset);
        Matrix.rotateM(result, offset, angle, v.x, v.y, v.z);

        return result;
    }
    public static float[]   calculateNormalMatrix   (float[] modelViewMatrix)
    {
        //--calculate normalMatrix as transpose( inverse( model-view matrix ) )
        float[] invertedVmMatrix    = new float[16];
        float[] normalMatrix        = new float[16];

        Matrix.invertM      ( invertedVmMatrix  , offset
                            , modelViewMatrix   , offset );

        Matrix.transposeM   ( normalMatrix      , offset
                            , invertedVmMatrix  , offset );

        return normalMatrix;
    }
    public static float[]   getIndentityMatrix4f    ()
    {
        float[] m = new float[16];
        Matrix.setIdentityM(m, offset);

        return m;
    }
    public static void      setMatricesAsIdnetity   (Vector<float[]> matrices)
    {
        for (float[] matrix : matrices)
        {
            matrix = getIndentityMatrix4f();
        }
    }

    private final static int offset = 0;
}
