package geometries;

/**
 * An abstract class that represents 3D shapes with a radius that implements the Geometry interface
 * @author Ayelet and Gili
 *
 */
public abstract class RadialGeometry extends Geometry {
    /**
     * the field represent radius in a shape
     */
    protected double radius;

    /**
     * constructor that get radius
     * @param radius - doublE
     */
    RadialGeometry(double radius)
    {
        this.radius = radius;
    }
}
