package lighting;

import primitives.BlackBoard;
import primitives.Color;
import primitives.Point;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.isZero;

/**
 * The PointLight class represents a point light source in a 3D scene.
 * It extends the Light class and implements the LightSource interface.
 */
public class PointLight extends Light implements LightSource
{

    /**
     * the position of the light
     */
    private Point position;
    private double radius;
    /**
     * attenuation coefficients
     */
    private double Kc = 1, Kl = 0, Kq = 0;

    /**
     * Constructs a new PointLight object with the specified intensity and position.
     * @param intensity The color intensity of the light.
     * @param position  The position of the light source.
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
        this.radius=0;
    }

    public PointLight(Color intensity, Point position, double radius) {
        super(intensity);
        this.position = position;
        this.radius = radius;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public List<Vector> getLCircle(Point p, double r, int amount) {
        return List.of();
    }

    public PointLight setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        double d = position.distance(p); // the distance between the points
        return getIntensity().scale(1/(Kc + Kl*d + Kq*d*d)); // calculate by The light propagation model
    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize(); // return the vector L from pl point to p
    }

    /**
     * Sets the constant attenuation factor of the point light.
     * @param kC The constant attenuation factor to set.
     * @return The modified PointLight object.
     */
    public PointLight setKc(double kC) {
        this.Kc = kC;
        return this;
    }

    /**
     * Sets the linear attenuation factor of the point light.
     * @param kL The linear attenuation factor to set.
     * @return The modified PointLight object.
     */
    public PointLight setKl(double kL) {
        this.Kl = kL;
        return this;
    }
    public Point getPosition() {
        return position;
    }

    public double getKq() {
        return Kq;
    }

    public double getKl() {
        return Kl;
    }

    public double getKc() {
        return Kc;
    }

    /**
     * Sets the quadratic attenuation factor of the point light.
     * @param kQ The quadratic attenuation factor to set.
     * @return The modified PointLight object.
     */
    public PointLight setKq(double kQ) {
        this.Kq = kQ;
        return this;
    }

    @Override
    public double getDistance(Point point) {
        return point.distance(position);
    }

    @Override
    public List<Point> getGridPoints(Vector l) {
        if (radius == 0) {
            List<Point> li = new LinkedList<>();
            li.add(position);
            return li;
        } else {
            Vector v = !(isZero(l.getX()) || isZero(l.getY())) ? new Vector(-l.getY(), l.getX(), 0) :
                    new Vector(1, 1, 0).normalize();
            return BlackBoard.constructCircleBlackBoard(BlackBoard.getMAX_CELLS(), position, v, v.crossProduct(l), radius);
        }
    }

}