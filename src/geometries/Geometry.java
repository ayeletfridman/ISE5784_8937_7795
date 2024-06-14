package geometries;

import java.awt.*;

import primitives.Material;
import primitives.Point;
import primitives.Vector;
import primitives.Color;

public abstract class Geometry extends Intersectable {
    protected Color emission = Color.BLACK;
    private Material material = new Material();

    public abstract Vector getNormal(Point p);

    public Color getEmission() {
        return emission;
    }

    public Geometry setEmission(Color emission) {

        this.emission = emission;
        return this;
    }

    public Material getMaterial() {
        return material;
    }

    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

}
