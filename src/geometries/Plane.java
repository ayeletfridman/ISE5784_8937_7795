package geometries;

import primitives.Double3;

import java.awt.*;

public class Plane implements Geometry{

    private Point p;
    private Vector normal;

    public Plane(Vector vec, Point p) {
        this.p = p;
        Point tmp = vec.xyz;
        if(!tmp.equals(1))
            throw new IllegalArgumentException("The vector is not normalized");
        this.normal = vec;
    }



    @Override
    public Vector getNormal(Point p) {
        return null;
    }
}
