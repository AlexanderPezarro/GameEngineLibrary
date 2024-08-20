package main.java;

import processing.core.PApplet;
import processing.core.PVector;

public class BoundingTriangle extends BoundingShape {
    PVector top;
    PVector botLeft;
    PVector botRight;
    PVector size;
    float angle;

    public BoundingTriangle (PApplet engine, PVector top, PVector botLeft, PVector botRight) {
        super(engine, Shape.TRIANGLE);
        this.top = top;
        this.botLeft = botLeft;
        this.botRight = botRight;
        center = PVector.add(top,botLeft).add(botRight).div(3);
        size = new PVector(PVector.dist(botLeft, botRight), PVector.dist(top, center)*3/2);
    }

    public BoundingTriangle (PApplet engine, PVector center, float w, float h) {
        super(engine, Shape.TRIANGLE);
        this.top = new PVector(center.x, center.y-h*2/3);
        this.botLeft = new PVector(center.x-w/2, center.y+h/3);
        this.botRight = new PVector(center.x+w/2, center.y+h/3);
        this.center = center.copy();
        size = new PVector(w, h);
    }

    public void scale(float x, float y) {
        setSize(size.x * x, size.y * y);
    }

    public void move(PVector position) {
        if (position.equals(center)) return;

        PVector topDir = PVector.sub(top, center);
        PVector botLeftDir = PVector.sub(botLeft, center);
        PVector botRightDir = PVector.sub(botRight, center);

        center = position.copy();

        top = PVector.add(center, topDir);
        botLeft = PVector.add(center, botLeftDir);
        botRight = PVector.add(center, botRightDir);
    }

    public void rotate(float angle) {
        float angleDiff = angle - this.angle;
        if (angleDiff == 0) return;
        this.angle = angle;

        PVector topDir = PVector.sub(top, center);
        PVector botLeftDir = PVector.sub(botLeft, center);
        PVector botRightDir = PVector.sub(botRight, center);

        topDir.rotate(angleDiff);
        botLeftDir.rotate(angleDiff);
        botRightDir.rotate(angleDiff);
        
        top = PVector.add(center, topDir);
        botLeft = PVector.add(center, botLeftDir);
        botRight = PVector.add(center, botRightDir);
    }

    public void setSize(float x, float y) {
        PVector curentSize = getSize();
        float diffX = curentSize.x - x;
        float diffY = curentSize.y - y;
        float change = engine.sqrt(engine.sq(diffX) + engine.sq(diffY));

        PVector topDir = PVector.sub(center, top);
        topDir.setMag(topDir.mag() + change);

        PVector botLeftDir = PVector.sub(center, botLeft);
        botLeftDir.setMag(botLeftDir.mag() + change);

        PVector botRightDir = PVector.sub(center, botRight);
        botRightDir.setMag(botRightDir.mag() + change);
        
        top = PVector.add(center, topDir);
        botLeft = PVector.add(center, botLeftDir);
        botRight = PVector.add(center, botRightDir);
    }

    public PVector getSize() {
        return size;
    }

    public AABB getAABB() {
        return new AABB(center, size.x, size.y);
    }

    public PVector[] getVertices() {
        return new PVector[] {top, botRight, botLeft};
    }

    public boolean containsMouse() {
        return containsPoint(new PVector(engine.mouseX, engine.mouseY));
    }

    private float sign(PVector p1, PVector p2, PVector p3) {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }

    public boolean containsPoint(PVector p) {
        float d1, d2, d3;
        boolean has_neg, has_pos;

        d1 = sign(p, top, botLeft);
        d2 = sign(p, botLeft, botRight);
        d3 = sign(p, botRight, top);

        has_neg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        has_pos = (d1 > 0) || (d2 > 0) || (d3 > 0);

        return !(has_neg && has_pos);
    }

    public void draw() {
        // pushStyle();
        // stroke(255);
        // line(top.x, top.y, botRight.x, botRight.y);
        // line(botRight.x, botRight.y, botLeft.x, botLeft.y);
        // line(botLeft.x, botLeft.y, top.x, top.y);
        // popStyle();
    }
}