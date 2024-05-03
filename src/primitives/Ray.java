package primitives;

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
    Ray(Point p0, Vector dir)
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
}
