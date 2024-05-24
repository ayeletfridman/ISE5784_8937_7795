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
    public List<Point> findIntersections(Ray ray)
    {
        Point p = ray.getP0();
        Vector v = ray.getDir();
        if (p.equals(center))
        {
            return List.of(center.add(v.scale(radius)));
        }
        Vector u = center.subtract(p);
        double tm = alignZero(v.dotProduct(u));
        double d = alignZero(Math.sqrt(u.lengthSquared() - tm * tm));
        if (d >= radius) //there are no intersections
        {
            return null;
        }
        double th = alignZero(Math.sqrt(radius * radius - d * d));
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);
        if (t1 > 0 && t2 > 0) //checking if t is not the head of the ray
        {
            Point P1 = ray.getPoint(t1);
            Point P2 = ray.getPoint(t2);
            return List.of(P1, P2);
        }
        if (t1 > 0)
        {
            Point P1 =ray.getPoint(t1);
            return List.of(P1);
        }
        if (t2 > 0)
        {
            Point P2 =ray.getPoint(t2);
            return List.of(P2);
        }
        return null;

    }
}