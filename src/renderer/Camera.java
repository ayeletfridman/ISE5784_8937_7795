package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.MissingResourceException;

import static primitives.Util.isZero;

/**
 * The Camera class represents a virtual camera in a 3D graphics environment.
 * It is used to define the viewpoint and parameters for rendering scenes.
 * The class utilizes a builder pattern to construct camera instances with specified parameters.
 */
public class Camera implements Cloneable {

    // Private instance variables for camera parameters
    private Point p0;
    private Vector vTo;
    private Vector vUp;
    private Vector vRight;
    private double viewPlaneWidth;
    private double viewPlaneHeight;
    private double viewPlaneDistance;

    // Private constructor to enforce the use of the builder pattern
    private Camera() {}

    /**
     * Static method to obtain a builder for creating a Camera instance.
     *
     * @return A new instance of the Camera.Builder class.
     */
    public static Builder getBuilder() { return new Builder(); }

    /**
     * Builder class for constructing Camera instances with specified parameters.
     */
    public static class Builder {

        // Private instance of the Camera class being built
        private final Camera camera = new Camera();

        /**
         * Set the location of the camera.
         *
         * @param location The location (Point) of the camera.
         * @return The Builder instance for method chaining.
         */
        public Builder setLocation(Point location) {
            camera.p0 = location;
            return this;
        }

        /**
         * Set the direction vectors for the camera.
         *
         * @param to The direction vector towards which the camera is pointing.
         * @param up The up vector for the camera orientation.
         * @return The Builder instance for method chaining.
         * @throws IllegalArgumentException if the input vectors are not orthogonal.
         */
        public Builder setDirection(Vector to, Vector up) throws IllegalArgumentException {
            if (!isZero(to.dotProduct(up))) throw new IllegalArgumentException("The vectors aren't orthogonal");
            camera.vTo = to.normalize();
            camera.vUp = up.normalize();
            return this;
        }

        /**
         * Set the size of the view plane.
         *
         * @param width  The width of the view plane.
         * @param height The height of the view plane.
         * @return The Builder instance for method chaining.
         * @throws IllegalArgumentException if the input dimensions are not valid.
         */
        public Builder setVPSize(double width, double height) throws IllegalArgumentException {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Invalid length or width");
            }
            camera.viewPlaneWidth = width;
            camera.viewPlaneHeight = height;
            return this;
        }

        /**
         * Set the distance from the camera to the view plane.
         *
         * @param distance The distance from the camera to the view plane.
         * @return The Builder instance for method chaining.
         * @throws IllegalArgumentException if the input distance is not valid.
         */
        public Builder setVPDistance(double distance) throws IllegalArgumentException {
            if (distance <= 0) {
                throw new IllegalArgumentException("Invalid distance");
            }
            camera.viewPlaneDistance = distance;
            return this;
        }

        /**
         * Build the Camera instance with the specified parameters.
         *
         * @return The constructed Camera instance.
         * @throws MissingResourceException if any required parameter is missing.
         */
        public Camera build() throws MissingResourceException {
            // Check for missing arguments
            String missingArgMsg = "there's a missing argument";
            String className = "Camera";
            if (camera.p0 == null) throw new MissingResourceException(missingArgMsg, className, "p0 - the location of the camera");
            if (camera.vUp == null) throw new MissingResourceException(missingArgMsg, className, "vUp - one of the direction vectors of the camera");
            if (camera.vTo == null) throw new MissingResourceException(missingArgMsg, className, "vTo - one of the direction vectors of the camera");

            // Calculate the right vector and normalize it
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            // Check for zero values and orthogonality
            if (Util.alignZero(camera.viewPlaneWidth) == 0) throw new MissingResourceException(missingArgMsg, className, "planeWidth");
            if (Util.alignZero(camera.viewPlaneHeight) == 0) throw new MissingResourceException(missingArgMsg, className, "planeHeight");
            if (Util.alignZero(camera.viewPlaneDistance) == 0) throw new MissingResourceException(missingArgMsg, className, "planeDistance");
            if (!isZero(camera.vRight.dotProduct(camera.vTo))) throw new IllegalArgumentException();

            // Check for valid dimensions and distance
            if (camera.viewPlaneWidth < 0 || camera.viewPlaneHeight < 0) {
                throw new IllegalArgumentException("Invalid length or width");
            }
            if (camera.viewPlaneDistance < 0) {
                throw new IllegalArgumentException("Invalid distance");
            }

            // Calculate the view plane center point
           // camera.viewPlanePC = camera.p0.add(camera.vTo.scale(camera.viewPlaneDistance));

            // Attempt to clone the camera instance
            try {
                return (Camera) camera.clone(); // Cloneable – get a full copy
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    } // end of Builder class

    /**
     * This method creates a ray that goes through the center of a pixel.
     *
     * @param nX The total number of pixels in the x-direction.
     * @param nY The total number of pixels in the y-direction.
     * @param j  The x-coordinate of the pixel.
     * @param i  The y-coordinate of the pixel.
     * @return A Ray instance representing the ray through the center of the specified pixel.
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        Point pCenter = p0.add(vTo.scale(viewPlaneDistance));

        double Ry = (double) viewPlaneHeight / nY;
        double Rx = (double) viewPlaneWidth / nX;

        double yI = -(i - (double) (nY - 1) / 2) * Ry;
        double xJ = (j - (double) (nX - 1) / 2) * Rx;

        Point pIJ = pCenter;
        if (xJ != 0) pIJ = pIJ.add(vRight.scale(xJ));
        if (yI != 0) pIJ = pIJ.add(vUp.scale(yI));

        Vector vIJ = pIJ.subtract(p0);
        return new Ray(p0, vIJ);
    }

    public double getViewPlaneDistance() {
        return viewPlaneDistance;
    }

    public double getViewPlaneHeight() {
        return viewPlaneHeight;
    }

    public Vector getvRight() {
        return vRight;
    }

    public Vector getvUp() {
        return vUp;
    }

    public Vector getvTo() {
        return vTo;
    }

    public Point getP0() {
        return p0;
    }

    public double getViewPlaneWidth() {
        return viewPlaneWidth;
    }
}
