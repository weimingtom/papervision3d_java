package jgraphlib;

/**
 * @author Andrey Kokorev
 *         Created on 02.02.2015.
 */
public class Vector3d
{
    private double x, y, z;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Vector3d(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3d(Vector3d from, Vector3d to)
    {
        this.x = to.getX() - from.getX();
        this.y = to.getY() - from.getY();
        this.z = to.getZ() - from.getZ();
    }

    public Vector3d product(Vector3d v)
    {
        return new Vector3d(
                y * v.getZ() - z * v.getY(),
                z * v.getX() - x * v.getZ(),
                x * v.getY() - y * v.getX()
        );
    }

    public Vector3d product(double v)
    {
        return new Vector3d(x * v, y * v, z * v);
    }

    public double dot(Vector3d v)
    {
        return x * v.getX() + y * v.getY() + z * v.getZ();
    }

    public void normalize()
    {
        double l = Math.sqrt(x*x + y*y + z*z);
        if(l == 0) return;
        x /= l;
        y /= l;
        z /= l;
    }

    public Vector3d plus(Vector3d v)
    {
        return new Vector3d(x + v.getX(), y + v.getY(), z + v.getZ());
    }

    public Vector3d minus(Vector3d v)
    {
        return new Vector3d(x - v.getX(), y - v.getY(), z - v.getZ());
    }

    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
