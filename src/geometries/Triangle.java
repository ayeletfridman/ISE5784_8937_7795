package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.*;
import java.util.List;

/**
 * A class that represents a triangle in the plane and inherits from class Polygon
 * @author Ayelet and Gili
 *
 */
public class Triangle extends Polygon
{
    /**
     * Triangle constructor that get 3 points
     * @param p1 - point
     * @param p2 - point
     * @param p3 - point
     */
    public Triangle(Point p1, Point p2, Point p3)
    {
        super(p1, p2, p3);
    }

    /**

     Find the intersection points of a ray with a triangle.
     @param ray The ray to intersect with the triangle.
     @return A list of intersection points between the ray and the triangle, or null if there are no intersections.
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray)
    {
        Point p = ray.getP0();
        Vector v = ray.getDir();
        // calculate the three edges of the triangle
        Vector v1 = vertices.get(0).subtract(p);
        Vector v2 = vertices.get(1).subtract(p);
        Vector v3 = vertices.get(2).subtract(p);
        // calculate the three edges of the triangle
        Vector n1 = (v1.crossProduct(v2)).normalize();
        Vector n2 = (v2.crossProduct(v3)).normalize();
        Vector n3 = (v3.crossProduct(v1)).normalize();
        // calculate the dot products of the ray direction with the three normals
        double d1 = alignZero(v.dotProduct(n1));
        double d2 = alignZero(v.dotProduct(n2));
        double d3 = alignZero(v.dotProduct(n3));
        // calculate the dot products of the ray direction with the three normals
        if((d1>0 && d2>0 && d3>0) || (d1<0 && d2<0 && d3<0))
        {
            List<GeoPoint> list = plane.findGeoIntersections(ray);
            if (list != null) {
                Point intersectPoint = list.get(0).point;
                return List.of((new GeoPoint(this, intersectPoint)));
            }
        }
        return null;
    }

}