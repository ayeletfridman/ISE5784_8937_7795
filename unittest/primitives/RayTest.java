package primitives;

import geometries.Polygon;
import org.junit.jupiter.api.Test;

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

        //TC01: the distance is positive
        //assertEquals(p1.add(dir.scale(2)), Point.ZERO, "ERROR: the calculation has to be zero");

        assertThrows(IllegalArgumentException.class, //
                () -> new Ray(p1,dir).getPoint(0),
                "Constructed a polygon with vertex on a side");
    }
}