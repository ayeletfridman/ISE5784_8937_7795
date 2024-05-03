package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * A class that represents Tube space and inherits from a RadialGeometry class
 * @author Ayelet and Gili
 *
 */
public class Tube extends RadialGeometry
{
    /**
     * the ray is the direction of the tube
     */
    final Ray axisRay;

    /**
     * Tube constructor with 2 parameters
     * @param radius - double
     * @param axisRay - ray
     */
    Tube(double radius, Ray axisRay) {
        super(radius);
        this.axisRay = axisRay;
    }

    @Override
    public Vector getNormal(Point p) {
        return null;
    }
}