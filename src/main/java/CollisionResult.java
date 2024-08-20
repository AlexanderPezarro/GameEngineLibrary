package main.java;

import processing.core.PVector;

public class CollisionResult {

    PVector delta;
    PVector normal;
    PVector pos;

    public CollisionResult() {
        this(new PVector(), new PVector(), new PVector());
    }
    
    public CollisionResult(PVector delta, PVector normal, PVector pos) {
        this.delta = delta;
        this.normal = normal;
        this.pos = pos;
    }
}