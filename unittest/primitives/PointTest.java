package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * unit tests for point class
 * @author gili and ayelet
 */
class PointTest {

    private final Point p1 = new Point(1, 2, 3);
    private final Point p2 = new Point(-1, -2, -3);
    final double DELTA = 0.000001;
    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     */
    @Test
     void add() {
        /// ============ Equivalence Partitions Tests ==============

        //TC01: test that check the sum of positive point and positive vector
        assertEquals(p1.add(new Vector(2, 3, 4)), new Point(3, 5, 7), "ERROR: Point + Vector does not work correctly");
        //TC02: test that check the sum of positive point and negative vector
        assertEquals(p1.add(new Vector(-2, -3, -4)), new Point(-1, -1, -1), "ERROR: Point + Vector does not work correctly");

        //TC03: test that check the sum of negative point and positive vector
        assertEquals(p2.add(new Vector(2, 3, 4)), new Point(1, 1, 1), "ERROR: Point + Vector does not work correctly");
        //TC04: test that check the sum of negative point and negative vector
        assertEquals(p2.add(new Vector(-2, -3, -4)), new Point(-3, -5, -7), "ERROR: Point + Vector does not work correctly");

        // =============== Boundary Values Tests ==================
        //TC11: test that check the sum of point and minus itself
        assertEquals(p2.add(new Vector(1, 2, 3)), new Point(0,0,0), "ERROR: Point + itself Vector does not work correctly");


    }
    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    void subtract() {
        /// ============ Equivalence Partitions Tests ==============

        //TC01: test that check the difference of positive point and positive vector
        assertEquals(p1.subtract(new Point(2, 3, 4)), new Vector(-1, -1, -1), "ERROR: Point - Point does not work correctly");
        //TC02: test that check the difference of positive point and negative vector
        assertEquals(p1.subtract(new Point(-2, -3, -4)), new Vector(3, 5, 7), "ERROR: Point - Point does not work correctly");

        //TC03: test that check the difference of negative point and positive vector
        assertEquals(p2.subtract(new Point(2, 3, 4)), new Vector(-3, -5, -7), "ERROR: Point - Point does not work correctly");
        //TC04: test that check the difference of negative point and negative vector
        assertEquals(p2.subtract(new Point(-2, -3, -4)), new Vector(1, 1, 1), "ERROR: Point - Point does not work correctly");
    }
    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     */
    @Test
    void distanceSquared() {
        /// ============ Equivalence Partitions Tests ==============

        //TC01: test that check the distance Squared of positive point and positive point
        assertEquals(p1.distanceSquared(new Point(2, 3, 4)), 3, "ERROR: The distance squared between two Points does not work correctly");
        //TC02: test that check the distance Squared of positive point and negative point
        assertEquals(p1.distanceSquared(new Point(-2, -3, -4)), 83, "ERROR: The distance squared between two Points does not work correctly");

        //TC03: test that check the distance Squared of negative point and positive point
        assertEquals(p2.distanceSquared(new Point(2, 3, 4)), 83, "ERROR: The distance squared between two Points does not work correctly");
        //TC04: test that check the distance Squared of negative point and negative point
        assertEquals(p2.distanceSquared(new Point(-2, -3, -4)), 3, "ERROR: The distance squared between two Points does not work correctly");

        // =============== Boundary Values Tests ==================
        //TC11: test that check the distance between point to itself
        assertEquals(p1.distanceSquared(p1), 0, "ERROR: point squared distance to itself is not zero");


    }
    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     */
    @Test
    void distance() {

        /// ============ Equivalence Partitions Tests ==============

        //TC01: test that check the distance of positive point and positive point
        assertEquals(p1.distance(new Point(2, 3, 4)), Math.sqrt(3), "ERROR: The distance between two Points does not work correctly");
        //TC02: test that check the distance of positive point and negative point
        assertEquals(p1.distance(new Point(-2, -3, -4)), Math.sqrt(83), "ERROR: The distance between two Points does not work correctly");

        //TC03: test that check the distance of negative point and positive point
        assertEquals(p2.distance(new Point(2, 3, 4)), Math.sqrt(83), "ERROR: The distance between two Points does not work correctly");
        //TC04: test that check the distance of negative point and negative point
        assertEquals(p2.distance(new Point(-2, -3, -4)), Math.sqrt(3), "ERROR: The distance between two Points does not work correctly");

        // =============== Boundary Values Tests ==================
        //TC11: test that check the distance between point to itself
        assertNotEquals(p1.distance(p1), DELTA,0, "ERROR: point squared distance to itself is not zero");


    }
}