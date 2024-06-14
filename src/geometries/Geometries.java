package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Geometries extends Intersectable {
    private final List<Intersectable> intersections = new LinkedList<Intersectable>();

    /**
     * empty constructor
     */
    public Geometries(){
    }

    /**
     Constructor with parameters
     @param geometries An array of intersectable geometries.
     */
    public Geometries(Intersectable... geometries) {

        Collections.addAll(intersections, geometries);
    }

    /**
     Adds one or more Intersectable geometries to this Geometries object.
     @param geometries One or more intersectable geometries to add to this Geometries object.
     */

    public void add(Intersectable... geometries)
    {
        if(geometries != null)
            Collections.addAll(intersections, geometries);
    }

    /**
     * find intersection between ray and all geometries in the geometry composite
     * @param ray ray towards the composite of geometries
     * @return list of intersection points
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray)
    {
        // create list of points
        List<GeoPoint> points = null;
        //find intersections for each shape in geometries
        for(Intersectable shape: intersections)
        {
            List<GeoPoint> temPoints = shape.findGeoIntersections(ray);
            if(temPoints != null)
            {
                if(points == null)
                    points = new LinkedList<>();
                points.addAll(temPoints);// add the new point to the list
            }
        }
        return points;
    }
}
