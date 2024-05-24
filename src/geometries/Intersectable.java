package geometries;
import primitives.Point;
import primitives.Ray;
import java.util.List;


public interface Intersectable {
    /**
     * the function get ray and return list of intersections
     * @param ray
     * @return
     */
    List<Point> findIntersections(Ray ray);
}
