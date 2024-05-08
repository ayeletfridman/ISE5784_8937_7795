package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import geometries.Sphere;
import primitives.Point;
import primitives.Vector;
/**
 * unit tests for SphereTest class
 * @author gili and ayelet
 */
class SphereTest {
    Sphere sphere = new Sphere(1, new Point(0, 0, 0));
    Point point = new Point(1, 0, 0);
    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {

        /// ============ Equivalence Partitions Tests ==============
        //TC01: test that check if there is a vector normal to point on the sphere
        assertEquals(sphere.getNormal(point), new Vector(1, 0, 0).normalize(), "ERROR: the normal is wrong value");
    }
}