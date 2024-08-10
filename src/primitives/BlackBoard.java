package primitives;


import java.util.List;
import java.util.LinkedList;

/**
 * abstract Class for create BlackBoard of points
 */
public abstract class BlackBoard {
    /**
     * The number of cells in each row and column of the targetArea grid.
     */
    private static final int MAX_CELLS = 9;

    /**
     * getter for MAX_CELLS
     *
     * @return MAX_CELLS
     */
    public static int getMAX_CELLS() {
        return MAX_CELLS;
    }

    /**
     * constructor list of points to create circle black board
     * by get size (x/y), point and direction vectors, and radius of circle
     * <p>
     * calc by GRID of x/y and ignore points outside the given radius
     * the given center Point always return in the list.
     * because circle is smaller than rectangle, you get less points than x*y in approximately 27%
     *
     * @param xy     number of points in x and y. NEED TO BE over than 1 (1 returns only the given center point)
     * @param center center point of circle
     * @param vUp    up vector of camera
     * @param vRight right vector of camera
     * @param radius radius of circle (radius 0.0 return only the center point)
     * @return list of points on BlackBoard
     */
    public static List<Point> constructCircleBlackBoard(int xy, Point center, Vector vUp, Vector vRight, double radius) {
        // Verify that the number of points in each direction is greater than 0
        if (xy < 1)
            throw new IllegalArgumentException("xy must be over than 0");

        // If xy is 1 or radius is 0, return a list containing only the center point
        if ((xy == 1) || (radius == 0.0))
            return List.of(center);

        // Create a list to store the points within the circle
        List<Point> pointList = new LinkedList<>();

        // Calculate the size of the square area that will be used to sample points
        double rectSize = 2 * radius;
        // Square of the radius for distance comparisons
        double radius2 = radius * radius;
        // Temporary point variable
        Point pIJ;
        // Distance between adjacent sample points in the grid
        double rxy = rectSize / xy;
        // Temporary variables for x and y coordinates
        double yI;
        double xJ;

        // Iterate over the grid points
        for (int i = 0; i < xy; i++) {
            for (int j = 0; j < xy; j++) {
                // Calculate the x and y coordinates of the grid point
                yI = Util.alignZero((i - (xy - 1) / 2.0) * rxy);
                xJ = Util.alignZero((j - (xy - 1) / 2.0) * rxy);

                // Start with the center point
                pIJ = center;
                // Adjust the point based on the right vector and x-coordinate
                if (xJ != 0) pIJ = pIJ.add(vRight.scale(xJ));
                // Adjust the point based on the up vector and y-coordinate
                if (yI != 0) pIJ = pIJ.add(vUp.scale(yI));

                // Calculate the squared distance from the center to the current point
                double dist = pIJ.distanceSquared(center);
                // If the distance is within the radius, add the point to the list
                if (dist <= radius2)
                    pointList.add(pIJ);
            }
        }
        // Return the list of points within the circle
        return pointList;
    }


}