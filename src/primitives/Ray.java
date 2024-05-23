package primitives;

import static primitives.Util.isZero;

public class Ray
{
    /**
     * the head of the ray
     */
    final Point p0;
    /**
     * the direction of the ray
     */
    final Vector dir;


    /**
     * Ray constructor with to parameters
     * @param p0 - point
     * @param dir - vector
     */
    public Ray(Point p0, Vector dir)
    {
        this.p0 = p0;
        this.dir = dir.normalize();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.p0.equals(other.p0)
                && this.dir.equals(other.dir);
    }

    @Override
    public String toString(){
        return p0.toString() + ", " + dir.toString();
    }

    /**
     * getter of the field p0
     * @return the head point of the ray
     */
    public Point getP0()
    {
        return p0;
    }

    /**
     * getter of the filed dir
     * @return the direction vector of the ray
     */
    public Vector getDir()
    {
        return dir;
    }
    /**
     * Calculation of a point on ray
     * @param t
     * @return the point
     */
    public Point getPoint(double t)
    {
        if(t==0)
            throw new IllegalArgumentException("vector can't be zero");
        return p0.add(dir.scale(t));

    }

}
