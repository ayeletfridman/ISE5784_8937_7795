package primitives;

public class Vector extends Point {
    /**
     * vector constructor with 3 parameters
     * @param x - X axis coordinate
     * @param y - Y axis coordinate
     * @param z - Z axis coordinate
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (this.xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector can't be zero");
    }
    /**
     *  vector constructor with 1 parameter
     * @param xyz - Of type Double3
     */
   public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector can't be zero");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Vector other)
            return super.equals(other);
        return false;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Vector add(Vector vec) {
        if (this.equals(vec.scale(-1)))
            throw new IllegalArgumentException("ERROR: Adding opposite vectors gives the zero vector");
        return new Vector(this.xyz.add(vec.xyz));
    }

    /**
     * The function multiplies the vector by a scalar
     * @param scalar
     * @return new vector
     */
    public Vector scale(double scalar) {
        return new Vector(this.xyz.scale(scalar));
    }

    /**
     * The function performs a scalar multiplication
     *
     * @param v
     * @return the result of the multiplication
     */
    public double dotProduct(Vector v) {
        return this.xyz.d1 * v.xyz.d1 + this.xyz.d2 * v.xyz.d2 + this.xyz.d3 * v.xyz.d3;
    }

    /**
     * The function performs vector multiplication
     * @param other
     * @return A vector perpendicular to both vectors
     */
    public Vector crossProduct(Vector other)
    {
        double x = this.xyz.d2*other.xyz.d3 - this.xyz.d3*other.xyz.d2;
        double y = this.xyz.d3*other.xyz.d1 - this.xyz.d1*other.xyz.d3;
        double z = this.xyz.d1*other.xyz.d2 - this.xyz.d2*other.xyz.d1;
        return new Vector(x, y, z);
    }

    /**
     * A function that calculates the squared length of the vector
     *
     * @return the length of the vector squared
     */
    public double lengthSquared() {

        return this.dotProduct(this);
    }

    /**
     * A function that calculates the length of the vector
     *
     * @return the length of the vector
     */
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * the function normalize the vector
     *
     * @return A unit vector in the same direction as the original vector
     */
    public Vector normalize() {
        Vector newVector = new Vector(this.xyz);
        return newVector.scale(1 / this.length());
    }

}
