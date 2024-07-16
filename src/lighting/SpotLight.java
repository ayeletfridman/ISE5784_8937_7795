package lighting;

import geometries.Geometry;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
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

    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
        setRadius(0); // ברירת מחדל ללא רדיוס
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



}
