package lighting;

import javax.swing.text.Position;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import java.util.List;

/**
 * The DirectionalLight class represents a directional light source in a 3D scene.
 * It extends the Light class and implements the LightSource interface.
 */
public class DirectionalLight extends Light implements LightSource
{
    /**
     * the direction of the light
     */
    private Vector direction;

    /**
     * Constructs a new DirectionalLight object with the specified intensity and direction.
     * @param intensity The color intensity of the light.
     * @param direction The direction of the light.
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction;
    }

    @Override
    public Color getIntensity(Point p) {
        return getIntensity();
    }

    @Override
    public Vector getL(Point p) {
        return direction.normalize();
    }

    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public List<Vector> getLCircle(Point p, double r, int amount) {
        return List.of(getL(p));
    }
    @Override
    public List<Point> getGridPoints(Vector l) { return null; }
}