package com.vlad.dino3d.utils;

public class Facet {

    public Vector normal;
    public Point3d A;
    public Point3d B;
    public Point3d C;

    public Facet(){};

    public Facet(Vector normal, Point3d a, Point3d b, Point3d c) {
        this.normal = normal;
        A = a;
        B = b;
        C = c;
    }
}
