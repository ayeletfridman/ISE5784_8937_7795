package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable {
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
    public List<Point> findIntersections(Ray ray) {
        List<Point> result = null;
        //for each geometry in composite check intersection points
        for (var item: intersections ) {

            // get intersection point for a specific geometry in composite
            List<Point> itemList = item.findIntersections(ray);

            // points were found , add to composite's total intersection points list
            if(itemList != null) {
                if(result==null){
                    result= new LinkedList<>();
                }
                result.addAll(itemList);
            }
        }
        // return list of points - null if no intersection points were found
        return result;
    }
}
