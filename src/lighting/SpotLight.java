package lighting;

import geometries.Geometry;
import primitives.*;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.isZero;
//
///**
// * The SpotLight class represents a spotlight in a 3D scene.
// * It extends the PointLight class and provides functionality for a directional light beam.
// */
//public class SpotLight extends PointLight
//{
//    /**
//     * the direction of the spot light
//     */
//    private Vector direction;
//
//
//    /**
//     * Constructs a new SpotLight object with the specified intensity, position, and direction.
//     * @param intensity The color intensity of the light.
//     * @param position  The position of the light source.
//     * @param direction The direction of the spotlight beam.
//     */
//    public SpotLight(Color intensity, Point position, Vector direction) {
//        super(intensity, position);
//        this.direction = direction;
//    }
//
//    @Override
//    public Color getIntensity(Point p) {
//        double scalar = Math.max(0,direction.normalize().dotProduct(getL(p)));// calculate the scalar
//        return super.getIntensity(p).scale(scalar);// mult the scalar with the result with the point light
//    }
//


//}

public class SpotLight extends PointLight {
    private Vector direction;
    /**
     * The points on the target area grid
     */
    private List<Point> targetPoints = null;

    public List<Point> getTargetPoints() {
        return targetPoints;
    }

    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
        setRadius(0); // ברירת מחדל ללא רדיוס
    }

    public SpotLight(Color intensity, Point position, Vector direction, double radius) {
        super(intensity, position, radius);
        this.direction = direction.normalize();
        Vector v = !(isZero(direction.getX()) || isZero(direction.getY())) ? new Vector(direction.getY(), -direction.getX(), 0) :
                new Vector(1, 1, 0).normalize();
        if (radius == 0) {
            targetPoints = new LinkedList<>();
            targetPoints.add(position);
        } else
            targetPoints = BlackBoard.constructCircleBlackBoard(BlackBoard.getMAX_CELLS(), position, v, direction.crossProduct(v), radius);
    }



    @Override
    public Color getIntensity(Point p) {
        double distance = getPosition().distance(p);
        double attenuation = 1 / (getKc() + getKl() * distance + getKq() * distance * distance);
        double angle = Math.max(0, direction.dotProduct(getL(p)));
        return getIntensity().scale(attenuation * angle);
    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(getPosition()).normalize();
    }


    @Override
    public List<Point> getGridPoints(Vector l) {
       // return this.targetPoints;
        return super.getGridPoints(l);
    }

}
