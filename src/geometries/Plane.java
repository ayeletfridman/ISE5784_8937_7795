package geometries;

import primitives.Double3;

import java.awt.*;
import java.util.ArrayList;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import java.util.List;
/**
 * A class that represents a plane in space by a point and a vector normal to the plane
 * @author Ayelet and Gili
 *
 */
public class Plane implements Geometry{
    /**
     * A point on the plane
     */
    final Point p;
    /**
     * A vector perpendicular to the plane
     */
    final Vector normal;

    /**
     * Plane constructor that get 3 point and calculate the normal to the plane
     * @param p1 - point
     * @param p2 - point
     * @param p3 - point
     */
    Plane(Point p1, Point p2, Point p3)
    {
        this.p = p1;
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        this.normal = v1.crossProduct(v2).normalize();
    }


    /**
     * Plane constructor with 2 parameters
     * @param p - point
     * @param v - vector
     */
    Plane(Point p, Vector v)
    {
        this.p = p;
        this.normal = v.normalize();
    }
    @Override
    public Vector getNormal(Point p) {
        return this.normal;
    }

    /**
     * getter of the filed normal
     * @return the normal vector to the plane
     */
    public Vector getNormal() {
        return normal;
    }

    @Override
    public List<Point> findIntersections(Ray ray)
    {
       return null;
    }
}
