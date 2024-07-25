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

        if (xy < 1)
            throw new IllegalArgumentException("xy must be over than 0");

        if ((xy == 1) || (radius == 0.0))
            return List.of(center);

        List<Point> pointList = new LinkedList<>();

        double rectSize = 2 * radius;
        double radius2 = radius * radius;
        Point pIJ;
        double rxy = rectSize / xy;
        double yI;
        double xJ;
        for (int i = 0; i < xy; i++) {
            for (int j = 0; j < xy; j++) {
                yI = Util.alignZero((i - (xy - 1) / 2.0) * rxy); // math.random to make jitter
                xJ = Util.alignZero((j - (xy - 1) / 2.0) * rxy);

                pIJ = center;
                if (xJ != 0) pIJ = pIJ.add(vRight.scale(xJ));
                if (yI != 0) pIJ = pIJ.add(vUp.scale(yI));

                double dist = pIJ.distanceSquared(center);
                if (dist <= radius2)
                    pointList.add(pIJ);
            }
        }
        return pointList;
    }

}