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
    private int numOfRays = 0;
    /**
     * Constructs with one param.
     *
     * @param scene the scene to be traced
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
        this.numOfRays=55;
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

    public SimpleRayTracer setNumOfRadius(int numOfRays)
    {
        this.numOfRays = numOfRays;
        return this;
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
     * Calculates the soft shadow effect by averaging the light intensity
     * from multiple rays cast from a point on the geometry towards a spotlight.
     *
     * @param light The spotlight source.
     * @param gp The GeoPoint representing the intersection point on the geometry.
     * @return The average light intensity considering the shadows.
     */
    private Color calculateSoftShadow(LightSource light, GeoPoint gp) {
        // Calculate the direction from the intersection point to the light source
        Vector lightDirection = light.getL(gp.point);

        // Get the normal vector at the intersection point
        Vector normal = gp.geometry.getNormal(gp.point);

        // Construct multiple rays from the intersection point towards the light source
        List<Ray> lightRays = constructRaysToLight(
                light,
                lightDirection,
                normal,
                gp
        );

        // Initialize the total light intensity to black (no intensity)
        Color totalIntensity = Color.BLACK;
        // Initialize a counter to keep track of the number of unblocked rays
        int count = 0;

        // Iterate over each constructed ray
        for (Ray ray : lightRays) {
            // Get the direction of the current ray
            Vector l = ray.getDir();
            // Calculate the dot product of the normal vector and the ray direction
            double nl = alignZero(normal.dotProduct(l));
            // Only consider rays that are in the same direction as the normal
            if (nl > 0) {
                // Find the closest intersection point of the current ray with the scene
                GeoPoint intersection = findClosestIntersection(ray);
                // Check if the ray is not blocked or if the blocking intersection is farther than the light source
                if (intersection == null || intersection.point.distance(gp.point) > light.getDistance(gp.point)) {
                    // Add the light intensity at the ray's origin point to the total intensity
                    totalIntensity = totalIntensity.add(light.getIntensity(ray.getP0()));
                    // Increment the count of unblocked rays
                    count++;
                }
            }
        }
        // Return the average light intensity if there are unblocked rays, otherwise return black
        return count == 0 ? Color.BLACK : totalIntensity.scale(1.0 / count);
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
//    private Double3 transparency (GeoPoint gp, LightSource light, Vector l, Vector n) {
//        Double3 sum = Double3.ZERO;// sum of ktr - Coefficients
//        List<Ray> beams = constructRaysToLight(light, l, n, gp);// create numberOfRays rays
//        for (Ray ray : beams) { // for each ray
//            List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);// calculate Intersections
//            if (intersections != null)// there are intersections
//            {
//                double distance = light.getDistance(gp.point);
//                Double3 ktr = Double3.ONE;
//                for (GeoPoint geoPoint : intersections) {
//                    if (alignZero(geoPoint.point.distance(gp.point)) <= distance)
//                    {
//                        //negative when the intersection point is before(from the object view) the light source
//                        ktr = ktr.product(geoPoint.geometry.getMaterial().Kt);
//                        if (ktr.lowerThan(MIN_CALC_COLOR_K))// if we got to the minimum value of k- stop the recursion
//                        {
//                            ktr = Double3.ZERO;
//                            break;// stop the checking for current point
//                        }
//                    }
//                    // else -> the intersection point is after(from the object view) the light
//                    // source, there is no shadow
//                }
//                sum = sum.add(ktr);
//            }
//            else// no intersections
//            {
//                sum = sum.add(Double3.ONE);
//            }
//        }
//        return sum.reduce(beams.size());// Average of Coefficients
//    }


//    private Double3 transparency( GeoPoint geoPoint,LightSource ls, Vector l, Vector n) {
//        Double3 ktr = Double3.ZERO;
//        int count = 0;
//
//        List<Point> targetAreaPoints = ls.getGridPoints(l);
//        // in case the light is directional, we simply add a point in the opposite direction, so we will do 1 check
//        // as we used to do (Directional light is NOT affected by super sampling)
//        if (targetAreaPoints == null) {
//            targetAreaPoints = new LinkedList<>();
//            targetAreaPoints.add(geoPoint.point.add(l.scale(-1)));
//        }
//
//        for (Point p : targetAreaPoints) {
//            List<GeoPoint> intersections = scene.geometries.findGeoIntersections(new Ray(p.subtract(geoPoint.point).normalize(), geoPoint.point, n));
//            if (intersections == null) {
//                ktr = ktr.add(Double3.ONE);
//                count++;
//            } else {
//                double lightDistance = ls.getDistance(geoPoint.point);
//                for (GeoPoint geop : intersections) {
//                    if (Util.alignZero(geop.point.distance(geoPoint.point) - lightDistance) <= 0) {
//                        ktr = ktr.add(geop.geometry.getMaterial().Kt);
//                        count++;
//                    } else {
//                        ktr = ktr.add(Double3.ONE);
//                        count++;
//                    }
//                }
//            }
//            //if (ktr.scale(1.0 / count).lowerThan(MIN_CALC_COLOR_K))
//            //    return Double3.ZERO;
//        }
//        return ktr.scale(1.0 / count).lowerThan(MIN_CALC_COLOR_K) ? Double3.ZERO : ktr.scale(1.0 / count);
//    }

//
//
//    public class MultiThreadingUtil {
//
//        private static final int NUM_THREADS = 4;
//        private static final ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
//
//        public static <T> List<T> executeTasks(List<Callable<T>> tasks) throws InterruptedException, ExecutionException {
//            List<Future<T>> futures = executor.invokeAll(tasks);
//            List<T> results = new ArrayList<>();
//
//            for (Future<T> future : futures) {
//                results.add(future.get());
//            }
//
//            return results;
//        }
//
//        public static void shutdown() {
//            executor.shutdown();
//            try {
//                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
//                    executor.shutdownNow();
//                }
//            } catch (InterruptedException e) {
//                executor.shutdownNow();
//                Thread.currentThread().interrupt();
//            }
//        }
//    }
//
//    private Callable<Double3> calculateTransparencyCallable(GeoPoint geoPoint, Point p, Vector l, Vector n, LightSource ls) {
//        return () -> {
//            List<GeoPoint> intersections = scene.geometries.findGeoIntersections(new Ray(p.subtract(geoPoint.point).normalize(), geoPoint.point, n));
//            Double3 ktr = Double3.ZERO;
//
//            if (intersections == null) {
//                ktr = Double3.ONE;
//            } else {
//                double lightDistance = ls.getDistance(geoPoint.point);
//                for (GeoPoint geop : intersections) {
//                    if (Util.alignZero(geop.point.distance(geoPoint.point) - lightDistance) <= 0) {
//                        ktr = geop.geometry.getMaterial().Kt;
//                    } else {
//                        ktr = Double3.ONE;
//                    }
//                }
//            }
//
//            return ktr;
//        };
//    }
//
//    private Double3 transparency(GeoPoint geoPoint, LightSource ls, Vector l, Vector n) {
//        Double3 ktr = Double3.ZERO;
//        int count = 0;
//
//        List<Point> targetAreaPoints = ls.getGridPoints(l);
//
//        if (targetAreaPoints == null) {
//            targetAreaPoints = new LinkedList<>();
//            targetAreaPoints.add(geoPoint.point.add(l.scale(-1)));
//        }
//
//        List<Point> sampledPoints = adaptiveSampling(targetAreaPoints, geoPoint, l, n, ls);
//
//        List<Callable<Double3>> tasks = new ArrayList<>();
//        for (Point p : sampledPoints) {
//            tasks.add(calculateTransparencyCallable(geoPoint, p, l, n, ls));
//        }
//
//        List<Double3> results = new ArrayList<>();
//        try {
//            results = MultiThreadingUtil.executeTasks(tasks);
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace(); // או טיפול בחריגה בצורה מתאימה
//            // במקרה של חריגה, ניתן להחזיר ערך ברירת מחדל או להפסיק את החישוב
//            return Double3.ZERO;
//        }
//
//        for (Double3 result : results) {
//            ktr = ktr.add(result);
//            count++;
//        }
//
//        return ktr.scale(1.0 / count).lowerThan(MIN_CALC_COLOR_K) ? Double3.ZERO : ktr.scale(1.0 / count);
//    }
//
//
//    private List<Point> adaptiveSampling(List<Point> points, GeoPoint geoPoint, Vector l, Vector n, LightSource ls) {
//        List<Point> initialSamples = List.of(points.get(0), points.get(points.size() - 1),
//                points.get(points.size() / 2), points.get(points.size() / 2 - 1));
//        List<Point> sampledPoints = new LinkedList<>(initialSamples);
//
//        List<Callable<Double3>> tasks = new ArrayList<>();
//        for (Point p : initialSamples) {
//            tasks.add(calculateTransparencyCallable(geoPoint, p, l, n, ls));
//        }
//
//        List<Double3> results = new ArrayList<>();
//        try {
//            results = MultiThreadingUtil.executeTasks(tasks);
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace(); // או טיפול בחריגה בצורה מתאימה
//            // במקרה של חריגה, ניתן להחזיר את הדגימות הראשוניות בלבד או להפסיק את החישוב
//            return sampledPoints; // החזרת התוצאות הראשוניות במקרה של שגיאה
//        }
//
//        if (shouldSampleMore(results)) {
//            for (int i = 1; i < points.size() - 1; i++) {
//                if (!sampledPoints.contains(points.get(i))) {
//                    sampledPoints.add(points.get(i));
//                }
//            }
//        }
//
//        return sampledPoints;
//    }




    private Double3 transparency(GeoPoint geoPoint, LightSource ls, Vector l, Vector n) {
        Double3 ktr = Double3.ZERO;
        int count = 0;

        List<Point> targetAreaPoints = ls.getGridPoints(l);

        if (targetAreaPoints == null) {
            targetAreaPoints = new LinkedList<>();
            targetAreaPoints.add(geoPoint.point.add(l.scale(-1)));
        }

        // דגימה אדפטיבית
        List<Point> sampledPoints = adaptiveSampling(targetAreaPoints, geoPoint, l, n, ls);

        for (Point p : sampledPoints) {
            List<GeoPoint> intersections = scene.geometries.findGeoIntersections(new Ray(p.subtract(geoPoint.point).normalize(), geoPoint.point, n));
            if (intersections == null) {
                ktr = ktr.add(Double3.ONE);
                count++;
            } else {
                double lightDistance = ls.getDistance(geoPoint.point);
                for (GeoPoint geop : intersections) {
                    if (Util.alignZero(geop.point.distance(geoPoint.point) - lightDistance) <= 0) {
                        ktr = ktr.add(geop.geometry.getMaterial().Kt);
                        count++;
                    } else {
                        ktr = ktr.add(Double3.ONE);
                        count++;
                    }
                }
            }
        }

        return ktr.scale(1.0 / count).lowerThan(MIN_CALC_COLOR_K) ? Double3.ZERO : ktr.scale(1.0 / count);
    }

    private List<Point> adaptiveSampling(List<Point> points, GeoPoint geoPoint, Vector l, Vector n, LightSource ls) {
        // דגימה ראשונית של 4 נקודות פינה
        List<Point> initialSamples = List.of(points.get(0), points.get(points.size() - 1),
                points.get(points.size() / 2), points.get(points.size() / 2 - 1));
        List<Point> sampledPoints = new LinkedList<>(initialSamples);

        // חישוב קרניים והתוצאה שלהן
        List<Double3> results = new LinkedList<>();
        for (Point p : initialSamples) {
            results.add(calculateTransparency(geoPoint, p, l, n, ls));
        }

        // בדיקה אם יש צורך בדגימה נוספת על פי סטיית התקן או שינוי משמעותי בתוצאה
        if (shouldSampleMore(results)) {
            // הוספת נקודות דגימה נוספות
            for (int i = 1; i < points.size() - 1; i++) {
                if (!sampledPoints.contains(points.get(i))) {
                    sampledPoints.add(points.get(i));
                }
            }
        }

        return sampledPoints;
    }

    private Double3 calculateTransparency(GeoPoint geoPoint, Point p, Vector l, Vector n, LightSource ls) {
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(new Ray(p.subtract(geoPoint.point).normalize(), geoPoint.point, n));
        Double3 ktr = Double3.ZERO;

        if (intersections == null) {
            ktr = Double3.ONE;
        } else {
            double lightDistance = ls.getDistance(geoPoint.point);
            for (GeoPoint geop : intersections) {
                if (Util.alignZero(geop.point.distance(geoPoint.point) - lightDistance) <= 0) {
                    ktr = geop.geometry.getMaterial().Kt;
                } else {
                    ktr = Double3.ONE;
                }
            }
        }

        return ktr;
    }

    private boolean shouldSampleMore(List<Double3> results) {
        for (int i = 0; i < results.size(); i++) {
            for (int j = i + 1; j < results.size(); j++) {
                if (!isColorsEqual(results.get(i), results.get(j))) {
                    return true; // נמצא שינוי משמעותי בין הצבעים, יש לבצע דגימה נוספת
                }
            }
        }
        return false; // כל הצבעים קרובים מספיק זה לזה, אין צורך בדגימה נוספת
    }

    private boolean isColorsEqual(Double3 double3, Double3 double31) {
        double c1 = Math.sqrt(double3.d1 * double3.d1 + double3.d2 * double3.d2 + double3.d3 * double3.d3);
        double c2 = Math.sqrt(double31.d1 * double31.d1 + double31.d2 * double31.d2 + double31.d3 * double31.d3);
        double avg = (c1 + c2) / 2d;
        return double3.distance(double31) <= 0.25 * avg;
    }


    /**

     Constructs a list of rays from a given point towards a light source.

     @param light The light source
     @param l Vector representing the direction from the point to the light source
     @param n Normal vector at the integration point
     @param geopoint The integration point on the object's surface
     @return A list of rays from the integration point towards the light source
     */
    private List<Ray> constructRaysToLight(LightSource light, Vector l, Vector n, GeoPoint geopoint) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(geopoint.point, lightDirection, n);
        List<Ray> beam = new LinkedList<>();// create list of rays
        beam.add(lightRay);
        if (light.getRadius() == 0) //if the light is one point(no radius) send one ray
            return beam;
        Point p0 = lightRay.getP0();// the start point of the ray (the integration point)
        Vector v = lightRay.getDir();
        Vector vx= v.OrthogonalVector();
        Vector vy = (v.crossProduct(vx)).normalize();
        double r = light.getRadius();
        double distance = light.getDistance(p0);
        Point pC;
        if(isZero(distance))
            pC =p0;
        else
            pC = lightRay.getPoint(distance);// calculate the center point of the light
        for (int i = 0; i < numOfRays - 1; i++)
        {
            // create random polar system coordinates of a point in circle of radius r
            double cosTeta = ThreadLocalRandom.current().nextDouble(-1, 1);
            double sinTeta = Math.sqrt(1 - cosTeta * cosTeta);
            double d = ThreadLocalRandom.current().nextDouble(0, r);
            // Convert polar coordinates to Cartesian ones
            double x = d * cosTeta;
            double y = d * sinTeta;
            // pC - center of the circle
            // p0 - start of central ray, v - its direction, distance - from p0 to pC
            Point point = pC;
            //move the new point from the center to the random point
            if (!isZero(x))
                point = point.add(vx.scale(x));
            if (!isZero(y))
                point = point.add(vy.scale(y));
            if(!(isZero(x)&& isZero(y)))
                beam.add(new Ray(p0, point.subtract(p0), n)); // normalized inside Ray ctor
        }
        return beam;

    }


}
