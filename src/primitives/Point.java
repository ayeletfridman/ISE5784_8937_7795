package primitives;

public class Point {


    /**
     * A field of type Double3 to specify a point
     */
    final protected Double3 xyz;

    /**
     * point constructor with 3 parameters
     * @param x - X axis coordinate
     * @param y - Y axis coordinate
     * @param z - Z axis coordinate
     */
    public Point(double x, double y, double z) {
        xyz = new Double3(x,y,z);
    }
    /**
     * point constructor with 1 parameter
     * @param xyz - Of type Double3
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other)
                && this.xyz.equals(other.xyz);
    }
    @Override
    public String toString() {
        return  " " + xyz;
    }
    /**
     * The function adds a vector to a point
     * @param vec - the vector
     * @return new point
     */
    public Point add(Vector vec){
        return new Point(xyz.add(vec.xyz));
    }
    /**
     * The function performs vector subtraction
     * @param p - the start point
     * @return new vector
     */
    public Vector subtract(Point p){
        if(xyz.equals(p.xyz))
            throw new IllegalArgumentException("ERROR: Subtraction of identical vectors gives the zero vector");
        return new Vector(xyz.subtract(p.xyz));
    }
    /**
     * the function get a point and calculate the distance
     * @param p - the point
     * @return the square distance between this point an the current object
     */
    public double distanceSquared(Point p){
        return (this.xyz.d1-p.xyz.d1)*(this.xyz.d1-p.xyz.d1)+
                (this.xyz.d2-p.xyz.d2)*(this.xyz.d2-p.xyz.d2)+
                (this.xyz.d3-p.xyz.d3)*(this.xyz.d3-p.xyz.d3);
    }
    /**
     *  the function get a point and calculate the distance
     * @param p - the point
     * @return the distance between this point an the current objec
     */
    public double distance(Point p){
        return Math.sqrt(this.distanceSquared(p));
    }
}
