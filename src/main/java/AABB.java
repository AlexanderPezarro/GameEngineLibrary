package main.java;

import processing.core.PVector;

public class AABB {
    private PVector topLeft;
    private PVector botRight;

    public AABB(PVector center, float w, float h) {
        topLeft = new PVector(center.x - w/2, center.y - h/2);
        botRight = new PVector(center.x + w/2, center.y + h/2);
    }

    public AABB(PVector topLeft, PVector botRight) {
        this.topLeft = topLeft.copy();
        this.botRight = botRight.copy();
    }

    public PVector getTopLeft() {
        return topLeft;
    }

    public PVector getBotRight() {
        return botRight;
    }
}
