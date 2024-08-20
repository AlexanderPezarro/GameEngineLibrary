package main.java;

import processing.core.PApplet;
import processing.core.PVector;

import static processing.core.PApplet.dist;

public class BoundingCircle extends BoundingShape {

    private float radius;

    public BoundingCircle(PApplet engine, PVector center, float radius) {
        super(engine, Shape.CIRCLE);
        this.center = center;
        this.radius = radius;
    }

    @Override
    public void scale(float x, float y) {

    }

    @Override
    public void move(PVector position) {
        center = position.copy();
    }

    @Override
    public void rotate(float angle) {

    }

    @Override
    public void setSize(float w, float h) {
        this.radius = w > h ? w/2 : h/2;
    }

    @Override
    public PVector getSize() {
        return new PVector(radius*2, radius*2);
    }

    @Override
    public AABB getAABB() {
        return new AABB(center, radius*2, radius*2);
    }

    @Override
    public PVector[] getVertices() {
        return new PVector[] {center};
    }

    @Override
    public boolean containsMouse() {
        return containsPoint(new PVector(engine.mouseX, engine.mouseY));
    }

    @Override
    public boolean containsPoint(PVector p) {
        return (dist(center.x, center.y, p.x, p.y) <= radius);
    }

    @Override
    public void draw() {
        // pushStyle();
        // noFill();
        // stroke(255);
        // circle(center.x,center.y,radius);
        // popStyle();
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}