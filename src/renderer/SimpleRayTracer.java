package renderer;

import geometries.Intersectable;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;
import primitives.*;
import lighting.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;


import geometries.Intersectable.GeoPoint;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class SimpleRayTracer extends RayTracerBase{

    private static final double DELTA = 0.1;
    private static final Double3 INITIAL_K = Double3.ONE;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;

    /**
     * Constructs with one param.
     *
     * @param scene the scene to be traced
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Traces a ray through the scene and calculates the color at the point where the ray intersects with an object.
     * @param ray the ray to trace through the scene
     * @return the color at the point where the ray intersects with an object, or the background color if no intersection is found
     */
    @Override
    public Color traceRay(Ray ray)
    {
        GeoPoint closestPoint = findClosestIntersection(ray);
        if (closestPoint == null)
            return scene.background;
        return calcColor(closestPoint, ray);// call the function that return the color of the point
    }

    private Color calcColor(GeoPoint gp, Ray ray)
    {
        return calcColor(gp, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K).add(scene.ambientLight.getIntensity());
    }


    /**
     * Calculates the color of a point in the scene based on the ambient light present.
     * @param point the point in the scene for which to calculate the color
     * @return the color of the point based on the ambient light present
     */
    private Color calcColor(GeoPoint point, Ray ray, int level, Double3 k)
    {
        Color color = calcLocalEffects(point,ray, k);
        return 1 == level ? color : color.add(calcGlobalEffects(point, ray, level, k));
    }

    /**
     * Calculates the local effects of the given geometric point and ray.
     * @param// gp The geometric point for which to calculate the local effects.
     * @param// ray The ray used for the calculation.
     * @return The resulting color after applying the local effects.
     */

    private Color calcLocalEffects(GeoPoint gp, Ray ray, Double3 k) {
        Color color = gp.geometry.getEmission();
        Vector v = ray.getDir();
        Vector n = gp.geometry.getNormal(gp.point);
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0)
            return color;
        Material material = gp.geometry.getMaterial();

        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(gp.point);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) {
                Double3 ktr = transparency(gp, lightSource, l, n);
                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                    Color iL;
                    //if (lightSource instanceof SpotLight || lightSource instanceof PointLight) {
                       // iL = calculateSoftShadow(lightSource, gp);
                   // } else {
                        iL = lightSource.getIntensity(gp.point);
                   // }
                    iL = iL.scale(ktr);
                    color = color.add(
                            iL.scale(calcDiffusive(material, nl)),
                            iL.scale(calcSpecular(material, n, l, nl, v))
                    );
                }
            }
        }
        return color;
    }


    /**
     * Calculates the diffuse component of the material.
     * @param mat The material for which to calculate the diffuse component.
     * @param nl The dot product of the surface normal and the light direction.
     * @return The calculated diffuse component as a Double3 vector.
     */
    private Double3 calcDiffusive(Material mat, double nl)
    {
        // Scale the diffuse coefficient of the material by the absolute value of nl
        return mat.Kd.scale(Math.abs(nl));
    }

    /**
     * Calculates the specular component of the material.
     * @param mat The material for which to calculate the specular component.
     * @param n The surface normal vector.
     * @param l The direction vector from the geometric point to the light source.
     * @param nl The dot product of the surface normal and the light direction.
     * @param v The direction vector from the camera to the geometric point.
     * @return The calculated specular component as a Double3 vector.
     */
    private Double3 calcSpecular(Material mat,Vector n,Vector l,double nl,Vector v)
    {
        // Calculate the reflection vector using the surface normal, light direction, and dot product
        Vector r = l.subtract(n.scale(nl*2)).normalize();
        // Calculate the specular reflection coefficient of the material and scale by the specular coefficient of the material
        return mat.Ks.scale(Math.pow(Math.max(0, v.scale(-1).dotProduct(r)), mat.nShininess));
    }

    /**
     * Checks if a given point is unshaded by finding intersections between the point and the light source.
     *
     * @param gp The geometric point to check for shading.
     * @param l The direction from the point towards the light source.
     * @param n The normal vector at the point.
     * @param light The light source.
     * @param nv The dot product between the normal vector and the light direction.
     * @return {@code true} if the point is unshaded, {@code false} otherwise.
     */
    private boolean unshaded(GeoPoint gp, Vector l, Vector n, double nv, LightSource light) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Vector epsVector = n.scale(nv < 0 ? DELTA : -DELTA);
        Point point = gp.point.add(epsVector);
        Ray lightRay = new Ray(point, lightDirection);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);
        if (intersections == null)
            return true;
        double lightDistance = light.getDistance(gp.point);
        for (GeoPoint gp1 : intersections) {
            if (Util.alignZero(gp1.point.distance(gp.point) - lightDistance) <= 0)

                return false;
        }
        return true;
    }
    private GeoPoint findClosestIntersection(Ray ray)
    {
        List <GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if (intersections == null)
            return null;
        return ray.findClosestGeoPoint(intersections);
    }

    private Ray constructReflectedRay(Vector normal, Point point, Vector v)
    {
        double nv = alignZero(normal.dotProduct(v));
        if (isZero(nv))
            return null;
        Vector r = v.subtract(normal.scale(nv*2));
        return new Ray(point, r, normal);
    }

    private Ray constructRefractedRay(Vector normal, Point point, Vector vector) {
        return new Ray(point, vector, normal);
    }

    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k)
    {
        Color color= Color.BLACK;
        Vector v = ray.getDir();
        Vector n = gp.geometry.getNormal(gp.point);
        Material material= gp.geometry.getMaterial();
        return calcGlobalEffect(constructReflectedRay(n, gp.point, v), level, k, material.Kr)
                .add(calcGlobalEffect(constructRefractedRay(n, gp.point, v), level, k, material.Kt));
    }

    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx)
    {
        Double3 kkx= k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K))
            return Color.BLACK;
        GeoPoint gp= findClosestIntersection(ray);
        if (gp == null)
            return scene.background.scale(kx);
        return isZero(gp.geometry.getNormal(gp.point).dotProduct(ray.getDir())) ? Color.BLACK: calcColor(gp, ray, level - 1, kkx).scale(kx);
    }

    /**
     *
     * function that creates Partial shading in case the body or bodies that block
     * the light source from the point have transparency at some level or another
     *
     * @param //gp- the point in the geometry
     * @param //light
     * @param //l         -the vector from the light to the point
     * @param //n-        normal vector to the point at the geometry
     * @return double number represent the shadow
     */
    private Double3 transparency(GeoPoint geoPoint, LightSource ls, Vector l, Vector n) {
        // Initialize transparency value to zero and count of samples to zero
        Double3 ktr = Double3.ZERO;
        int count = 0;

        // Get target area points from the light source based on the light direction
        List<Point> targetAreaPoints = ls.getGridPoints(l);

        // If no target area points are found, create a default point opposite to the light direction
        if (targetAreaPoints == null) {
            targetAreaPoints = new LinkedList<>();
            targetAreaPoints.add(geoPoint.point.add(l.scale(-1)));
        }

        // Perform adaptive sampling to refine the list of target area points
        List<Point> sampledPoints = adaptiveSampling(targetAreaPoints, geoPoint, l, n, ls);

        // Iterate through each sampled point
        for (Point p : sampledPoints) {
            // Create a ray from the sampled point to geoPoint and find intersections with geometries in the scene
            List<GeoPoint> intersections = scene.geometries.findGeoIntersections(
                    new Ray(p.subtract(geoPoint.point).normalize(), geoPoint.point, n)
            );

            // If no intersections are found, assume full transparency
            if (intersections == null) {
                ktr = ktr.add(Double3.ONE);
                count++;
            }
            else {
                // Calculate the distance from the light source to geoPoint
                double lightDistance = ls.getDistance(geoPoint.point);

                // Iterate through all intersection points
                for (GeoPoint geop : intersections) {
                    // If the intersection is within the light distance
                    if (Util.alignZero(geop.point.distance(geoPoint.point) - lightDistance) <= 0) {
                        // Update transparency based on the material's transparency (Kt)
                        ktr = ktr.add(geop.geometry.getMaterial().Kt);
                    }
                    else {
                        // Update transparency to full transparency
                        ktr = ktr.add(Double3.ONE);
                    }
                    count++;
                }
            }
        }

        // Calculate the average transparency and return zero if it is below the minimum threshold
        return ktr.scale(1.0 / count).lowerThan(MIN_CALC_COLOR_K) ? Double3.ZERO : ktr.scale(1.0 / count);
    }


    private List<Point> adaptiveSampling(List<Point> points, GeoPoint geoPoint, Vector l, Vector n, LightSource ls) {
        // Initial sampling of 4 corner points
        List<Point> initialSamples = List.of(points.get(0), points.get(points.size() - 1),
                points.get(points.size() / 2), points.get(points.size() / 2 - 1));

        // Create a new list of sampled points starting with the initial samples
        List<Point> sampledPoints = new LinkedList<>(initialSamples);

        // Calculate the transparency/light effect for the initial samples
        List<Double3> results = new LinkedList<>();
        for (Point p : initialSamples) {
            results.add(calculateTransparency(geoPoint, p, l, n, ls));
        }

        // Check if additional sampling is needed based on standard deviation or significant variation
        if (shouldSampleMore(results)) {
            // Add more sample points if needed
            for (int i = 1; i < points.size() - 1; i++) {
                if (!sampledPoints.contains(points.get(i))) {
                    sampledPoints.add(points.get(i));
                }
            }
        }

        // Return the final list of sampled points
        return sampledPoints;
    }


    private Double3 calculateTransparency(GeoPoint geoPoint, Point p, Vector l, Vector n, LightSource ls) {
        // Create a ray from point p towards geoPoint
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(
                new Ray(p.subtract(geoPoint.point).normalize(), geoPoint.point, n)
        );

        // Initialize the transparency value to zero (no light passes)
        Double3 ktr = Double3.ZERO;

        // If there are no intersections, transparency is full (all light passes)
        if (intersections == null) {
            ktr = Double3.ONE;
        }
        else {
            // Calculate the distance from the light source to the geoPoint
            double lightDistance = ls.getDistance(geoPoint.point);

            // Loop through all intersection points
            for (GeoPoint geop : intersections) {
                // If the intersection is closer to the light source than geoPoint
                if (Util.alignZero(geop.point.distance(geoPoint.point) - lightDistance) <= 0) {
                    // Update transparency based on the material's transparency (Kt)
                    ktr = geop.geometry.getMaterial().Kt;
                }
                else {
                    // If the intersection is beyond the light source, full transparency
                    ktr = Double3.ONE;
                }
            }
        }

        // Return the calculated transparency value
        return ktr;
    }


    private boolean shouldSampleMore(List<Double3> results) {
        // Loop through all results
        for (int i = 0; i < results.size(); i++) {
            // Compare the current result with all subsequent results
            for (int j = i + 1; j < results.size(); j++) {
                // If any two results are not equal
                if (!isColorsEqual(results.get(i), results.get(j))) {
                    // Significant difference found, additional sampling is needed
                    return true;
                }
            }
        }
        // All results are sufficiently close to each other, no additional sampling needed
        return false;
    }


    private boolean isColorsEqual(Double3 double3, Double3 double31) {
        // Calculate the magnitude (length) of the first color vector
        double c1 = Math.sqrt(double3.d1 * double3.d1 + double3.d2 * double3.d2 + double3.d3 * double3.d3);
        // Calculate the magnitude (length) of the second color vector
        double c2 = Math.sqrt(double31.d1 * double31.d1 + double31.d2 * double31.d2 + double31.d3 * double31.d3);
        // Compute the average magnitude of the two color vectors
        double avg = (c1 + c2) / 2d;
        // Return true if the distance between the two color vectors is less than or equal to 25% of the average magnitude
        return double3.distance(double31) <= 0.25 * avg;
    }


}
