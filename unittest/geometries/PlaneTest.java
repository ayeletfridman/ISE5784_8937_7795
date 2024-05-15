package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import geometries.Plane;

import static org.junit.jupiter.api.Assertions.*;
/**
 * unit tests for PolygonTest class
 * @author gili and ayelet
 */
class PlaneTest {

    void testConstructor()
    {
        //TC01: The first and second points are connected
        assertThrows(IllegalArgumentException.class, () -> new Plane(new Point(1, 1, 1), new Point(1, 1, 1), new Point(0, 1, 0)));
        //TC02: The points are on the same line
        assertThrows(IllegalArgumentException.class, () -> new Plane(new Point(1, 0, 0), new Point(2, 0, 0), new Point(3, 0,  0)));
    }

    /**
     * Test method for getNormal function
     */
    @Test
    void getNormal() {
    }
    /**
     *  * Test method for {@link geometries.Plane#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        /// ============ Equivalence Partitions Tests ==============
        Plane plane = new Plane(new Point(1, 2, 3), new Point(4, 7, 11), new Point(2, -3, 4));

        double x = 9 / (7 * Math.sqrt(2));
        double y = 1 / (7 * Math.sqrt(2));
        double z = (-2) * Math.sqrt(2) / 7;

        assertEquals(new Vector(x, y, z), plane.getNormal(), "normal vector is not returned correctly");

    }
}