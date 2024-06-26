package primitives;

import geometries.Polygon;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class RayTest {

    Point p0 = new Point(1,2,3);
    Point p1 = new Point(-2,0,-8);
    Vector dir = new Vector(1,0,0);

    @Test
    void getPoint() {
        Ray ray1 = new Ray(p0,dir);
        /// ============ Equivalence Partitions Tests ==============
        //TC01: the distance is positive
        assertEquals(ray1.getPoint(2), new Point(3, 2, 3), "ERROR: the calculation of the positive distance is not correct");
        //TC02: the distance is negative
        assertEquals(ray1.getPoint(-2), new Point(-1, 2, 3), "ERROR: the calculation of the negative distance is not correct");

        // =============== Boundary Values Tests ==================+


        //assertEquals(p1.add(dir.scale(2)), Point.ZERO, "ERROR: the calculation has to be zero");
        //TC11: the distance is zero
       // assertThrows(IllegalArgumentException.class, //
         //       () -> new Ray(p1,dir).getPoint(0),
          //      "ERROR: the calculation of the zero distance is not correct");
    }
    /**
     * Test method for {@link primitives.Ray#findClosestPoint(List<Point>)}.
     */
    @Test
    void testFindClosestPoint() {


        List<Point> points = new LinkedList<>();
        Point p1 = new Point(2, 2, 2);
        Point p2 = new Point(3, 1, 1);
        Point p3 = new Point(5, 6, 7);
        Point p4 = new Point(7, 3, 1);
        Point p5 = new Point(8, 9, 2);
        Ray ray = new Ray(new Point(1, 1, 1), new Vector(3, 2, 1));

        // ============ Equivalence Partitions Tests ==============

        //TC01: A point in the middle of the list is the one closest to the beginning of the foundation
        points.add(p2);
        points.add(p1);
        points.add(p4);
        points.add(p5);
        points.add(p3);
        assertEquals(ray.findClosestPoint(points), p1, "The point is not in the middle of the list");

        // =============== Boundary Values Tests ==================

        //TC11: if the list is empty
        points.clear();
        assertNull(ray.findClosestPoint(points), "there is no intersection points");

        //TC12: The first point is closest to the beginning of the ray
        points.clear();
        points.add(p1);
        points.add(p4);
        points.add(p5);
        points.add(p2);
        points.add(p3);
        assertEquals(ray.findClosestPoint(points), p1, "The first point isn't closest to the beginning");

        //TC13: The last point is closest to the beginning of the ray
        points.clear();
        points.add(p3);
        points.add(p2);
        points.add(p5);
        points.add(p4);
        points.add(p1);
        assertEquals(ray.findClosestPoint(points), p1, "The last point isn't closest to the beginning");
    }
}