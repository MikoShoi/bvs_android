package com.HandyClasses;

public class Vector3D
{
    public Vector3D()
    {}

    public Vector3D(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3D xUnitVector()
    {
        return new Vector3D(1.0f, 0.0f, 0.0f);
    }

    public static Vector3D yUnitVector()
    {
        return new Vector3D(0.0f, 1.0f, 0.0f);
    }

    public static Vector3D zUnitVector()
    {
        return new Vector3D(0.0f, 0.0f, 1.0f);
    }

    public float x, y, z;
}
