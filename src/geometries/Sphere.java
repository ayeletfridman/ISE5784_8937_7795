package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.*;
import java.util.List;

/**
 * A class that represents a Sphere in space and inherits from RadialGeometry class
 * @author Ayelet and Gili
 *
 */
public class Sphere extends RadialGeometry
{
    /**
     * the point is the center of the sphere
     */
    final Point center;

    /**
     * Sphere constructor with 2 parameters
     * @param radius - double
     * @param center - point
     */
    public Sphere(double radius, Point center)
    {
        super(radius);
        this.center = center;
    }

    @Override
    public Vector getNormal(Point p) {
        Vector n = p.subtract(center);
        return n.normalize();
    }
    /**

     Finds the intersections of a given ray with this sphere.
     @param ray The ray to find intersections with.
     @return A list of intersection points, or null if there are no intersections.
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray)
    {
        Point p = ray.getP0();
        Vector v = ray.getDir();
        // If the ray starts at the center of the sphere
        if (p.equals(center))
        {
            return List.of(new GeoPoint (this, center.add(v.scale(radius))));
        }
        Vector u = center.subtract(p);
        double tm = alignZero(v.dotProduct(u));
        //Calculation of the length of the perpendicular according to Pythagoras
        double d = alignZero(Math.sqrt(u.lengthSquared() - tm * tm));
        if (d >= radius) //there are no intersections
        {
            return null;
        }
        //Calculation of the length of the perpendicular according to Pythagoras
        double th = alignZero(Math.sqrt(radius * radius - d * d));
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);
        if (t1 > 0 && t2 > 0) //checking if t is not the head of the ray
        {
            Point P1 = ray.getPoint(t1);
            Point P2 = ray.getPoint(t2);
            return List.of(new GeoPoint (this, P1), new GeoPoint (this,P2));
        }
        if (t1 > 0)
        {
            Point P1 =ray.getPoint(t1);
            return List.of(new GeoPoint (this, P1));
        }
        if (t2 > 0)
        {
            Point P2 =ray.getPoint(t2);
            return List.of(new GeoPoint (this, P2));
        }
        return null;

    }
}