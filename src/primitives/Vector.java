package primitives;

public class Vector extends Point{

    public Vector(double x, double y, double z) {
        super(x, y, z);
        if(this.xyz.equals(Double3.ZERO))
            throw  new IllegalArgumentException("Vector can't be zero");
    }
    Vector(Double3 xyz) {
        super(xyz);
        if(xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector can't be zero");
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
    public Vector add(Vector v) {
        return new Vector(this.xyz.add(v.xyz));
    }
    public Vector scale(double scale) {
        return new Vector(this.xyz.scale(scale));
    }
    public Vector  dotProduct(Vector v) {
        return new Vector(this.xyz.d1*v.xyz.d1,this.xyz.d2*v.xyz.d2,this.xyz.d3*v.xyz.d3);
    }
    public Vector  crossProduct(Vector v) {
        double x=this.xyz.d2*v.xyz.d3-this.xyz.d3*v.xyz.d2;
        double y=this.xyz.d1*v.xyz.d3-this.xyz.d3*v.xyz.d1;
        double z=this.xyz.d1*v.xyz.d2-this.xyz.d2*v.xyz.d1;
        return new Vector(x,y,z);
    }
    public Vector lengthSquared(){
        return this;
    }
    public Vector  length(){
        return Math.sqrt(this.lengthSquared());
    }

}
