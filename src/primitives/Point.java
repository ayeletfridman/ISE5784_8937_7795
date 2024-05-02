package primitives;

public class Point {
    final protected Double3 xyz;

    public Point(double x, double y, double z) {
        xyz = new Double3(x,y,z);
    }
    Point(Double3 xyz) {
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
        return "Point [xyz=" + xyz + "]";
    }
    public Point add(Vector vec){
        Double3 temp=this.xyz.add(vec.xyz);
        return new Point(temp);
    }

    public primitives.Vector subtract(Point p){
        Double3 temp = p.xyz.subtract(this.xyz);
        return new primitives.Vector(temp);
    }
    public double distanceSquared(Point p){
        return (this.xyz.d1-p.xyz.d1)*(this.xyz.d1-p.xyz.d1)+
                (this.xyz.d2-p.xyz.d2)*(this.xyz.d2-p.xyz.d2)+
                (this.xyz.d3-p.xyz.d3)*(this.xyz.d3-p.xyz.d3);
    }
    public double distance(Point p){
        return Math.sqrt(this.distanceSquared(p));
    }
}
