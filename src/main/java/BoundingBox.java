package main.java;

import processing.core.PApplet;
import processing.core.PVector;

public class BoundingBox extends BoundingShape {
    private PVector topLeft;
    private PVector topRight;
    private PVector botLeft;
    private PVector botRight;
    private PVector size;
    private float angle;

    public BoundingBox(PApplet engine, PVector topLeft, PVector topRight, PVector botLeft) {
        super(engine, Shape.RECT);

        this.topLeft = topLeft.copy();
        this.topRight = topRight.copy();
        this.botLeft = botLeft.copy();

        this.botRight = new PVector(botLeft.x + (topRight.x - topLeft.x), botLeft.y + (topRight.y - topLeft.y));
        this.center = new PVector((topLeft.x + botRight.x) / 2, (topLeft.y + botRight.y) / 2);

        size = new PVector(PVector.dist(topLeft,topRight), PVector.dist(topLeft,botLeft));
    }

    public BoundingBox(PApplet engine, PVector center, float w, float h, float angle) {
        super(engine, Shape.RECT);

        size = new PVector(w, h);
        w = w/2;
        h = h/2;

        this.center = center.copy();

        this.topLeft = new PVector(center.x - w, center.y - h);
        this.topRight = new PVector(center.x + w, center.y - h);
        this.botLeft = new PVector(center.x - w, center.y + h);
        this.botRight = new PVector(center.x + w, center.y + h);

        rotate(angle);
    }

    public BoundingBox(PApplet engine, PVector center, float w, float h) {
        this(engine, center, w, h, 0);
    }

    @Override
    public void setSize(float x, float y) {
        size.set(x, y);
        float mag = PVector.div(size, 2).mag();

        PVector topLeftDir = PVector.sub(center, topLeft);
        topLeftDir.setMag(mag);

        PVector topRightDir = PVector.sub(center, topRight);
        topRightDir.setMag(mag);

        topLeft = PVector.add(center, topLeftDir);
        topRight = PVector.add(center, topRightDir);
        botLeft = PVector.add(center, topRightDir.mult(-1));
        botRight = PVector.add(center, topLeftDir.mult(-1));
    }

    @Override
    public void scale(float x, float y) {
        setSize(size.x * x, size.y * y);
    }

    @Override
    public void move(PVector position) {
        if (position.equals(center)) return;

        PVector topLeftDir = PVector.sub(center, topLeft);
        PVector topRightDir = PVector.sub(center, topRight);

        center = position.copy();

        topLeft = PVector.add(center, topLeftDir);
        topRight = PVector.add(center, topRightDir);
        botLeft = PVector.add(center, topRightDir.mult(-1));
        botRight = PVector.add(center, topLeftDir.mult(-1));
    }

    @Override
    public void rotate(float angle) {
        float angleDiff = angle - this.angle;
        if (angleDiff == 0) return;
        this.angle = angle;

        PVector topLeftDir = PVector.sub(center, topLeft);
        PVector topRightDir = PVector.sub(center, topRight);

        topLeftDir.rotate(angleDiff);
        topRightDir.rotate(angleDiff);

        topLeft = PVector.add(center, topLeftDir);
        topRight = PVector.add(center, topRightDir);
        botLeft = PVector.add(center, topRightDir.mult(-1));
        botRight = PVector.add(center, topLeftDir.mult(-1));
    }

//    @Override
//    public BoundingBox copyShape() {
//        return new BoundingBox(engine, center.copy(), w, h);
//
//    }

    @Override
    public AABB getAABB() {
        return new AABB(topLeft, botRight);
    }

    @Override
    public PVector getSize() {
        return size.copy();
    }

    @Override
    public PVector[] getVertices() {
        return new PVector[] {topLeft.copy(), topRight.copy(), botRight.copy(), botLeft.copy()};
    }

    @Override
    public boolean containsPoint(PVector p) {
        // A = botLeft
        // B = topLeft
        // C = topRight
        // P = point
        PVector AB = new PVector(botLeft.x - topLeft.x, botLeft.y - topLeft.y);
        PVector AP = new PVector(botLeft.x - p.x, botLeft.y - p.y);
        PVector BC = new PVector(topLeft.x - topRight.x, topLeft.y - topRight.y);
        PVector BP = new PVector(topLeft.x - p.x, topLeft.y - p.y);
        float ABdotAP = PVector.dot(AB,AP);
        float BCdotBP = PVector.dot(BC,BP);

        return 0 <= ABdotAP && ABdotAP <= PVector.dot(AB,AB) &&
                0 <= BCdotBP && BCdotBP <= PVector.dot(BC,BC);
    }

    @Override
    public boolean containsMouse() {
        return containsPoint(new PVector(engine.mouseX, engine.mouseY));
    }

    @Override
    public void draw() {
        // pushStyle();
        // stroke(0,255,0);
        // //strokeWeight(10);
        // line(topLeft.x, topLeft.y, topRight.x, topRight.y);
        // line(topRight.x, topRight.y, botRight.x, botRight.y);
        // line(botRight.x, botRight.y, botLeft.x, botLeft.y);
        // line(botLeft.x, botLeft.y, topLeft.x, topLeft.y);
        // popStyle();
    }

    public PVector getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(PVector topLeft) {
        this.topLeft = topLeft;
    }

    public PVector getTopRight() {
        return topRight;
    }

    public void setTopRight(PVector topRight) {
        this.topRight = topRight;
    }

    public PVector getBotLeft() {
        return botLeft;
    }

    public void setBotLeft(PVector botLeft) {
        this.botLeft = botLeft;
    }

    public PVector getBotRight() {
        return botRight;
    }

    public void setBotRight(PVector botRight) {
        this.botRight = botRight;
    }
}
