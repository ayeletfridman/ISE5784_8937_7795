package primitives;

import java.util.List;

import static primitives.Util.isZero;
import geometries.Intersectable.GeoPoint;
import geometries.Intersectable.GeoPoint;
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
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestGeoPoint(points.stream().map(p -> new GeoPoint(null, p)).toList()).point;
    }
    /**
     Finds the closest point to a given point within a list of points.
     @param points The list of points to search for the closest point.
     @return The closest point to the given point, or null if the list is empty.
     */
    public GeoPoint findClosestGeoPoint(List<GeoPoint> points)
    {
        GeoPoint closetPoint = null;
        double dis = Double.MAX_VALUE;
        double tempDis;

        if(points.isEmpty()) //check if the list is empty
            return null;

        for (GeoPoint gPoint : points) //going through all the points in the list
        {
            tempDis = p0.distance(gPoint.point);
            if(tempDis < dis) //check if this point closer to the head of the ray
            {
                closetPoint = gPoint;
                dis = tempDis;
            }
        }
        return closetPoint;
    }



}
