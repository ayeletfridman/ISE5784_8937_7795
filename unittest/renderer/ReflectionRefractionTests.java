/**
 *
 */
package renderer;

import static java.awt.Color.*;

import geometries.Plane;
import lighting.PointLight;
import org.junit.jupiter.api.Test;

import geometries.Sphere;
import geometries.Polygon;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.SpotLight;
import primitives.*;
import renderer.*;
import scene.Scene;

/** Tests for reflection and transparency functionality, test for partial
 * shadows
 * (with transparency)
 * @author dzilb */
public class ReflectionRefractionTests {
    /** Scene for the tests */
    private final Scene          scene         = new Scene("Test scene");
    /** Camera builder for the tests with triangles */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), new Vector(0,1,0))
            .setRayTracer(new SimpleRayTracer(scene));

    /** Produce a picture of a sphere lighted by a spot light */
    @Test
    public void twoSpheres() {
        scene.geometries.add(
                new Sphere(50d,new Point(0, 0, -50)).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKd(0.4).setKs(0.3).setShininess(100).setKt(0.3)),
                new Sphere(25d,new Point(0, 0, -50)).setEmission(new Color(RED))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)));
        scene.lights.add(
                new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2))
                        .setKl(0.0004).setKq(0.0000006).setRadius(2));

        cameraBuilder.setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
                .setVpSize(150, 150)
                .setImageWriter(new ImageWriter("refractionTwoSpheres", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }

    /** Produce a picture of a sphere lighted by a spot light */
    @Test
    public void twoSpheresOnMirrors()  {
        scene.geometries.add(
                new Sphere(400d,new Point(-950, -900, -1000)).setEmission(new Color(0, 50, 100))
                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)
                                .setKt(new Double3(0.5, 0, 0))),
                new Sphere(200d,new Point(-950, -900, -1000)).setEmission(new Color(100, 50, 20))
                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)),
                new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
                        new Point(670, 670, 3000))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setKr(1.0)),
                new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
                        new Point(-1500, -1500, -2000))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setKr(new Double3(0.5, 0, 0.4))));
        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));
        scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4))
                .setKl(0.00001).setKq(0.000005).setRadius(2));

        cameraBuilder.setLocation(new Point(0, 0, 10000)).setVpDistance(10000)
                .setVpSize(2500, 2500)
                .setImageWriter(new ImageWriter("reflectionTwoSpheresMirrored", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }

    /** Produce a picture of a two triangles lighted by a spot light with a
     * partially
     * transparent Sphere producing partial shadow */
    @Test
    public void trianglesTransparentSphere() {
        scene.geometries.add(
                new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                        new Point(75, 75, -150))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)),
                new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)),
                new Sphere(30d,new Point(60, 50, -50)).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.6)));
        scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.15));
        scene.lights.add(
                new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1))
                        .setKl(4E-5).setKq(2E-7).setRadius(2));

        cameraBuilder.setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
                .setVpSize(200, 200)
                .setImageWriter(new ImageWriter("refractionShadow", 600, 600))
                .build()
                .renderImage()
                .writeToImage();
    }
    @Test
    public void newS() {
        scene.geometries.add( //
                new Triangle(new Point(-170, -170, -115), new Point(140, -170, -135), new Point(90, 25, -150)) //
                        .setMaterial(new Material().setKs(0.8).setShininess(60)).setEmission(new Color(140, 70, 20)), //

                new Triangle(new Point(-170, -170, -115), new Point(-80, 20, -140), new Point(90, 25, -150)) //
                        .setMaterial(new Material().setKs(0.8).setShininess(60)).setEmission(new Color(140, 70, 20)),//

                new Sphere(25d ,new Point(70, 70, -11)) //
                        .setEmission(new Color(100, 150, 80)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)), //

                new Sphere(30d ,new Point(0, -14, 0)) //
                        .setEmission(new Color(BLUE)) //
                        .setMaterial(new Material().setKd(0.2).setKs(0.3).setShininess(30).setKt(0.6)), //


                new Sphere(17d ,new Point(-20, 24, 13)) //
                        .setEmission(new Color(BLUE)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)), //

                new Sphere(17d ,new Point(20, 24, 13)) //
                        .setEmission(new Color(BLUE)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)), //

                new Sphere(5d, new Point(0, -16, 50)).setEmission(new Color(20, 20, 20)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)),

                new Plane(new Point (-170, -150, -115),new Vector(1,0,1) ) // הגדלתי את ה-y ב-40 והרחבתי את ה-x ב-80 לצד שמאל)
                .setMaterial(new Material().setKd(0.6).setKs(0.4).setShininess(25))
                .setEmission(new Color(gray))




        );

        scene.lights.add( //
                new SpotLight(new Color(700, 400, 400), new Point(30, 30, 115), new Vector(-1, -1, -4)) //
                        .setKl(4E-4).setKq(2E-5).setRadius(13));

//
//        scene.lights.add( //
//                new PointLight(new Color(700, 400, 400), new Point(30, 30, 115)) //
//                        .setKl(4E-4).setKq(2E-5).setRadius(15));

        /*** scene.lights.add( //
         new DirectionalLight(new Color(100, 600, 0), new Vector(-1, -1, -2))); //**/

        cameraBuilder.setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
                .setVpSize(200, 200)
                .setImageWriter(new ImageWriter("newSone", 600, 600))
                .build()
                .renderImage()
                .writeToImage();
    }

    @Test
    public void test() {



        scene.geometries.add(
                //train pass-Rails

                new Triangle(new Point(-150, -70, -130), new Point(150, -70, -130), new Point(150, -75, -130)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(-150, -70, -130), new Point(-150, -75, -130), new Point(150, -75, -130)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(-150, -50, -100), new Point(150, -50, -100), new Point(150, -45, -100)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(-150, -50, -100), new Point(-150, -45, -100), new Point(150, -45, -100)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(-90, -70, -130), new Point(-85, -70, -130), new Point(-75, -50, -100)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(-90, -70, -130), new Point(-80, -50, -100), new Point(-75, -50, -100)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(-40, -70, -130), new Point(-35, -70, -130), new Point(-26, -50, -100)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(-40, -70, -130), new Point(-31, -50, -100), new Point(-26, -50, -100)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(10, -70, -130), new Point(15, -70, -130), new Point(21, -50, -100)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(10, -70, -130), new Point(16, -50, -100), new Point(21, -50, -100)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(60, -70, -130), new Point(65, -70, -130), new Point(71, -50, -100)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.24)).setEmission(new Color(150, 75, 0)),
                new Triangle(new Point(60, -70, -130), new Point(66, -50, -100), new Point(71, -50, -100)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.24)).setEmission(new Color(150, 75, 0)),
                //Wheels
                new Sphere(20, new Point(-60, -44, -80)) //
                        .setEmission(new Color(GRAY)) //
                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(50).setKt(0)),
                new Sphere(10,new Point(-59, -44, -60)) //
                        .setEmission(new Color(BLACK)) //
                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(50).setKt(0)),
                new Sphere(25, new Point(40, -40, -80)) //
                        .setEmission(new Color(GRAY)) //
                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(50).setKt(0)),
                new Sphere(12.5, new Point(39, -40, -60)) //
                        .setEmission(new Color(BLACK)) //
                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(50).setKt(0)),


                //red box
                new Triangle(new Point(-90, -44, -90), new Point(70, -44, -90), new Point(70, 10, -90)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0)).setEmission(new Color(RED)),
                new Triangle(new Point(-90, -44, -90), new Point(-90, 10, -90), new Point(70, 10, -90)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0)).setEmission(new Color(RED)),
                new Triangle(new Point(70, -44, -90), new Point(80, -34, -110), new Point(70, 10, -100)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0)).setEmission(new Color(RED)),
                new Triangle(new Point(70, 10, -100), new Point(80, -34, -110), new Point(80, 20, -110)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0)).setEmission(new Color(RED)),
                new Triangle(new Point(-90, 10, -90), new Point(70, 10, -100), new Point(80, 20, -110)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0)).setEmission(new Color(RED)),
                new Triangle(new Point(-90, 10, -90), new Point(-80, 20, -110), new Point(80, 20, -110)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0)).setEmission(new Color(RED)),

                // blue box
                new Triangle(new Point(70, 10, -100), new Point(80, 20, -110), new Point(70, 50, -100)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0)).setEmission(new Color(50, 0, 100)),
                new Triangle(new Point(80, 60, -110), new Point(80, 20, -110), new Point(70, 50, -100)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0)).setEmission(new Color(50, 0, 100)),
                new Triangle(new Point(80, 60, -110), new Point(0 - 20, 50, -100), new Point(70, 50, -100)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0)).setEmission(new Color(50, 0, 100)),
                new Triangle(new Point(80, 60, -110), new Point(-20, 50, -100), new Point(-10, 60, -110)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0)).setEmission(new Color(50, 0, 100)),
                new Triangle(new Point(70, 10, -100), new Point(70, 50, -100), new Point(-20, 10, -90)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0)).setEmission(new Color(50, 0, 100)),
                new Triangle(new Point(-20, 10, -90), new Point(70, 50, -100), new Point(-20, 50, -90)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0)).setEmission(new Color(50, 0, 100)),

                //steam
                new Triangle(new Point(-70, -5, -100), new Point(-50, -5, -100), new Point(-70, 50, -100)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0)).setEmission(new Color(PINK)),
                new Triangle(new Point(-70, 50, -100), new Point(-50, -5, -100), new Point(-50, 50, -100)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0)).setEmission(new Color(PINK)),
                new Triangle(new Point(-50, 0, -100), new Point(-50, 50, -100), new Point(-45, 55, -105)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0)).setEmission(new Color(PINK)),
                new Triangle(new Point(-45, 55, -105), new Point(-50, 0, -100), new Point(-45, 15, -110)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0)).setEmission(new Color(PINK)),
                new Triangle(new Point(-70, 50, -100), new Point(-50, 50, -100), new Point(-45, 55, -105)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0)).setEmission(new Color(PINK)),
                new Triangle(new Point(-70, 50, -100), new Point(-65, 55, -105), new Point(-45, 55, -105)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0)).setEmission(new Color(PINK)),
                //Smoke cloud
                new Sphere(9,new Point(-55, 57, -106)) //
                        .setEmission(new Color(GRAY)) //
                        .setMaterial(new Material().setShininess(50).setKt(0.4)),
                new Sphere(8,new Point(-45, 65, -110)) //
                        .setEmission(new Color(GRAY)) //
                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(50).setKt(0.4)),
                new Sphere(13, new Point(-33, 73, -110)) //
                        .setEmission(new Color(GRAY)) //
                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(50).setKt(0.4)),


                //windows
                new Triangle(new Point(-5, 40, -80), new Point(17.5, 40, -80), new Point(17.5, 20, -80)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.24)).setEmission(new Color(0, 200, 200)),
                new Triangle(new Point(-5, 40, -80), new Point(-5, 20, -80), new Point(17.5, 20, -80)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.24)).setEmission(new Color(0, 200, 200)),
                new Triangle(new Point(32.5, 40, -80), new Point(55, 40, -80), new Point(55, 20, -80)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.24)).setEmission(new Color(0, 200, 200)),
                new Triangle(new Point(32.5, 40, -80), new Point(32.5, 20, -80), new Point(55, 20, -80)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.24)).setEmission(new Color(0, 200, 200)),


                //Stick
                new Triangle(new Point(-59, -45, -50), new Point(-59, -39, -50), new Point(45, -42, -50)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0)).setEmission(new Color(YELLOW)),
                new Triangle(new Point(-59, -39, -50), new Point(45, -36, -50), new Point(45, -42, -50)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0)).setEmission(new Color(YELLOW)),


                //background
                new Plane(new Point(-150, -150, -250), new Vector(0,0,1))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(20).setKt(0.5)).setEmission(new Color(gray)),
                new Plane(new Point(0, -75, 0), new Vector(0,1,0))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(20).setKr(0.1)).setEmission(new Color(BLACK))


        );

        scene.lights.add(new PointLight(new Color(WHITE), new Point(-91, 0, -80)) //
                .setKl(0.001).setKq(0.0005).setRadius(5));
        scene.lights.add(new PointLight(new Color(100, 40, 80), new Point(-100, 150, 0)) //
                .setKl(4E-5).setKq(2E-8).setRadius(5));


        cameraBuilder.setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
                .setVpSize(200, 200)
                .setImageWriter(new ImageWriter("newPic", 600, 600))
                .build()
                .renderImage()
                .writeToImage();


    }
}
