package main.java;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.round;
import static processing.core.PApplet.sqrt;
import static processing.core.PVector.*;

public abstract class BoundingShape {

    public static int sign(float val) {
        return val < 0 ? -1 : (val > 0 ? 1 : 0);
    }

    public static CollisionResult isColliding(BoundingShape bs1, BoundingShape bs2, CollisionResult result) {
        int collisionType = bs1.type.getValue() + bs2.type.getValue();
        return switch (collisionType) {
            case 2 -> // Circle & circle
                    circleCircleIntersectsHelper(bs1, bs2, result);
            case 3 -> // Circle & box
                    circleRectangleIntersectsHelper(bs1, bs2, result);
            case 4 -> // Box & box
                    generalSAT(bs1, bs2, result);
            case 5 -> // Circle & triangle
                    circleTriangleIntersectsHelper(bs1, bs2, result);
            case 6 -> // Box & triangle
                    generalSAT(bs1, bs2, result);
            case 8 -> // Triangle & triangle
                    generalSAT(bs1, bs2, result);
            default -> null;
        };
    }

    public static CollisionResult isColliding(BoundingShape bs1, List<BoundingShape> bsList, CollisionResult result) {
        int bs1Typeval = bs1.type.getValue();
        int collisionType;
        for(BoundingShape bs2 : bsList) {
            collisionType = bs1Typeval + bs2.type.getValue();
            switch(collisionType) {
                case 2: // Circle & circle
                    return circleCircleIntersectsHelper(bs1, bs2, result);
                case 3: // Circle & box
                    return circleRectangleIntersectsHelper(bs1, bs2, result);
                case 4: // Box & box
                    return boxBoxIntersectsHelper(bs1, bs2, result);
            }
        }
        return null;
    }

    private static CollisionResult circleRectangleIntersectsHelper(BoundingShape bs1, BoundingShape bs2, CollisionResult result) {
        BoundingCircle c;
        BoundingBox b;
        if (bs1.type == Shape.CIRCLE) {
            c = (BoundingCircle)bs1;
            b = (BoundingBox)bs2;
        } else {
            c = (BoundingCircle)bs2;
            b = (BoundingBox)bs1;
        }
        return circleRectangleIntersects(c, b, result);
    }

    // Circle intersecting with general rectangle taken from:
    // https://stackoverflow.com/a/402019
    private static CollisionResult circleRectangleIntersects(BoundingCircle c, BoundingBox b, CollisionResult result) {
        if (b.containsPoint(c.center) ||
                c.containsPoint(b.center) ||
                lineSegmentIntersects(c.center, c.getRadius(), b.getTopLeft(), b.getTopRight()) ||
                lineSegmentIntersects(c.center, c.getRadius(), b.getTopRight(), b.getBotRight()) ||
                lineSegmentIntersects(c.center, c.getRadius(), b.getBotRight(), b.getBotLeft()) ||
                lineSegmentIntersects(c.center, c.getRadius(), b.getBotLeft(), b.getTopLeft())) {

            PVector diff = sub(c.center, b.center).sub(c.getRadius(), c.getRadius());
            PVector boxSize = b.getSize().div(2);
            PVector distFromEdge = new PVector();
            distFromEdge.x = boxSize.x - abs(diff.x);
            distFromEdge.y = boxSize.y - abs(diff.y);

            if (abs(distFromEdge.x) < abs(distFromEdge.y)) {
                int sign = -sign(diff.x);
                result.delta.x = sign*distFromEdge.x;
                result.normal.x = sign;
            }
            else {
                int sign = -sign(diff.y);
                result.delta.y = sign*distFromEdge.y;
                result.normal.y = sign;
            }
            result.pos = add(b.center, diff.copy().setMag(PVector.dist(c.center,b.center) - c.getRadius()));
            return result;
        }
        return null;
    }

    // Circle intersecting with a line segment taken from:
    // https://stackoverflow.com/a/1084899
    private static boolean lineSegmentIntersects(PVector center, float radius, PVector start, PVector end) {
        PVector d = sub(end, start);
        PVector f = sub(start, center);

        float a = dot(d, d);
        float b = 2* dot(f, d);
        float c = dot(f, f) - radius*radius;

        float discriminant = b*b-4*a*c;
        if (discriminant >= 0) {
            discriminant = sqrt(discriminant);

            float t1 = (-b - discriminant)/(2*a);
            float t2 = (-b + discriminant)/(2*a);

            if (t1 >= 0 && t1 <= 1) return true;

            return t2 >= 0 && t2 <= 1;
        }
        return false;
    }

    private static CollisionResult circleCircleIntersectsHelper(BoundingShape bs1, BoundingShape bs2, CollisionResult result) {
        BoundingCircle c1 = (BoundingCircle)bs1;
        BoundingCircle c2 = (BoundingCircle)bs2;
        return circleCircleIntersects(c1.center, c1.getRadius(), c2.center, c2.getRadius(), result);
    }

    private static CollisionResult circleCircleIntersects(PVector center1, float radius1, PVector center2, float radius2, CollisionResult result) {
        float centerDist = PVector.dist(center1, center2);
        boolean isColliding = centerDist < radius1 + radius2;
        if (isColliding) {
            result.normal = sub(center2,center1).normalize();
            result.delta = result.normal.copy().setMag((radius1 + radius2) - centerDist);
            result.pos = add(center1, result.delta);
            return result;
        } else return null;
    }

    private static CollisionResult boxBoxIntersectsHelper(BoundingShape bs1, BoundingShape bs2, CollisionResult result) {
        BoundingBox b1 = (BoundingBox)bs1;
        BoundingBox b2 = (BoundingBox)bs2;
        return boxBoxSAT(b1, b2, result);
    }

    private static CollisionResult boxBoxSAT(BoundingBox bs1, BoundingBox bs2, CollisionResult result) {
        float dot = 0;
        ArrayList<PVector> perpendicularStack = new ArrayList<>();

        perpendicularStack.addAll(getPerpendicularLines(bs1, true));
        perpendicularStack.addAll(getPerpendicularLines(bs2, true));

        PVector[] bs1Vertices = bs1.getVertices();
        PVector[] bs2Vertices = bs2.getVertices();

        float minOverlap = -1;
        PVector normal = new PVector();

        for(PVector perpendicularLine: perpendicularStack) {
            float amax = 0;
            float amin = 0;
            float bmax = 0;
            float bmin = 0;

            for (int i = 0; i < bs1Vertices.length; i++) {
                dot = dot(perpendicularLine, bs1Vertices[i]);
                if (i == 0 || dot > amax) {
                    amax = dot;
                }
                if (i == 0 || dot < amin) {
                    amin = dot;
                }
            }

            for (int i = 0; i < bs2Vertices.length; i++) {
                dot = dot(perpendicularLine, bs2Vertices[i]);
                if (i == 0 || dot > bmax) {
                    bmax = dot;
                }
                if (i == 0 || dot < bmin) {
                    bmin = dot;
                }
            }

            if ((amin < bmax && amin > bmin) ||
                    (bmin < amax && bmin > amin)) {
                float abOverlap = abs(bmax - amin);
                float baOverlap = abs(amax - bmin);
                float overlap = Math.min(abOverlap, baOverlap);
                if (overlap < minOverlap || minOverlap == -1) {
                    minOverlap = overlap;
                    normal = perpendicularLine;
                }
                continue;
            }
            else return null;
        }

        result.normal = normal;
        for (PVector vertex: bs1Vertices) {
            if (bs2.containsPoint(vertex)) {
                result.pos = vertex;
                break;
            }
        }

        return result;
    }

    private static ArrayList<PVector> getPerpendicularLines(BoundingShape bs, boolean removeParrallel) {
        PVector perpendicularLine = null;
        ArrayList<PVector> perpendicularStack = new ArrayList<>();
        PVector[] vertices;
        PVector lastVertex = null;
        PVector vertex = null;
        PVector edge = null;

        vertices = bs.getVertices();
        lastVertex = vertices[0];

        for(int i = 1; i < vertices.length;  i++) {
            vertex = vertices[i];
            edge = sub(lastVertex, vertex).mult(1000);
            lastVertex = vertex;
            edge.x = round(edge.x);
            edge.y = round(edge.y);
            edge.div(1000);
            perpendicularLine = new PVector(-edge.y, edge.x);
            perpendicularStack.add(perpendicularLine.normalize());
        }
        vertex = vertices[0];
        edge = sub(lastVertex, vertex);
        perpendicularLine = new PVector(-edge.y, edge.x);
        perpendicularStack.add(perpendicularLine.normalize());

        if (removeParrallel) {
            ArrayList<PVector> toRemove = new ArrayList<>();
            for(int i = 0; i < perpendicularStack.size(); i++) {
                PVector perpLine = perpendicularStack.get(i);
                for(int j = i+1; j < perpendicularStack.size(); j++) {
                    if (perpLine.cross(perpendicularStack.get(j)).magSq() == 0) {
                        toRemove.add(perpLine);
                        break;
                    }
                }
            }
            perpendicularStack.removeAll(toRemove);
        }
        return perpendicularStack;
    }

    private static CollisionResult circleTriangleIntersectsHelper(BoundingShape bs1, BoundingShape bs2, CollisionResult result) {
        BoundingCircle c;
        BoundingTriangle t;
        if (bs1.type == Shape.CIRCLE) {
            c = (BoundingCircle)bs1;
            t = (BoundingTriangle)bs2;
        } else {
            c = (BoundingCircle)bs2;
            t = (BoundingTriangle)bs1;
        }
        return circleTriangleIntersects(c, t, result);
    }

    private static CollisionResult circleTriangleIntersects(BoundingCircle c, BoundingTriangle t, CollisionResult result) {
        if(t.containsPoint(c.center) ||
                c.containsPoint(t.center) ||
                lineSegmentIntersects(c.center, c.getRadius(), t.top, t.botRight) ||
                lineSegmentIntersects(c.center, c.getRadius(), t.botRight, t.botLeft) ||
                lineSegmentIntersects(c.center, c.getRadius(), t.botLeft, t.top)){

            PVector diff = sub(t.center, c.center).sub(c.getRadius(), c.getRadius());;
            PVector boxSize = t.getSize().div(2);
            PVector distFromEdge = new PVector();
            distFromEdge.x = abs(boxSize.x - abs(diff.x));
            distFromEdge.y = abs(boxSize.y - abs(diff.y));

            if (distFromEdge.x < distFromEdge.y) {
                int sign = sign(diff.x);
                result.delta.x = sign*distFromEdge.x;
                result.normal.x = sign;
            }
            else {
                int sign = sign(diff.y);
                result.delta.y = sign*distFromEdge.y;
                result.normal.y = sign;
            }
            result.pos = add(t.center, diff.copy().setMag(PVector.dist(c.center, t.center) - c.getRadius()));

            return result;
        }
        return null;
    }

    private static CollisionResult generalSAT(BoundingShape bs1, BoundingShape bs2, CollisionResult result) {
        float dot = 0;
        ArrayList<PVector> bs1Normals;
        ArrayList<PVector> bs2Normals;

        bs1Normals = getPerpendicularLines(bs1, false);
        bs2Normals = getPerpendicularLines(bs2, false);

        PVector[] bs1Vertices = bs1.getVertices();

        PVector[] bs2Vertices = bs2.getVertices();

        float minOverlap = -1;
        PVector normal = new PVector();

        for(PVector perpendicularLine: bs1Normals) {
            float amax = 0;
            float amin = 0;
            float bmax = 0;
            float bmin = 0;

            for (int i = 0; i < bs1Vertices.length; i++) {
                dot = dot(perpendicularLine, bs1Vertices[i]);
                if (i == 0 || dot > amax) {
                    amax = dot;
                }
                if (i == 0 || dot < amin) {
                    amin = dot;
                }
            }

            for (int i = 0; i < bs2Vertices.length; i++) {
                dot = dot(perpendicularLine, bs2Vertices[i]);
                if (i == 0 || dot > bmax) {
                    bmax = dot;
                }
                if (i == 0 || dot < bmin) {
                    bmin = dot;
                }
            }

            if ((amin <= bmax && amin >= bmin) ||
                    (bmin <= amax && bmin >= amin)) {
                continue;
            }
            else return null;
        }

        PVector bsToPlayer = sub(bs1.center, bs2.center).normalize();

        for(PVector perpendicularLine: bs2Normals) {
            float amax = 0;
            float amin = 0;
            float bmax = 0;
            float bmin = 0;

            for (int i = 0; i < bs1Vertices.length; i++) {
                dot = dot(perpendicularLine, bs1Vertices[i]);
                if (i == 0 || dot > amax) {
                    amax = dot;
                }
                if (i == 0 || dot < amin) {
                    amin = dot;
                }
            }

            for (int i = 0; i < bs2Vertices.length; i++) {
                dot = dot(perpendicularLine, bs2Vertices[i]);
                if (i == 0 || dot > bmax) {
                    bmax = dot;
                }
                if (i == 0 || dot < bmin) {
                    bmin = dot;
                }
            }

            if ((amin <= bmax && amin >= bmin) ||
                    (bmin <= amax && bmin >= amin)) {
                float abOverlap = abs(bmax - amin);
                float baOverlap = abs(amax - bmin);
                float overlap = Math.min(abOverlap, baOverlap);
                float angleToPlayer = perpendicularLine.dot(bsToPlayer);
                if (angleToPlayer >= 0.45 && (minOverlap == -1 || overlap < minOverlap)) {
                    minOverlap = overlap;
                    normal = perpendicularLine;
                }
                continue;
            }
            else return null;
        }

        if (bs2.normal != null) result.normal = bs2.normal;
        else result.normal = normal.normalize();
        result.delta = result.normal.copy().setMag(minOverlap);
        for (PVector vertex: bs1Vertices) {
            if (bs2.containsPoint(vertex)) {
                result.pos = vertex;
                break;
            }
        }

        return result;
    }

    protected final Shape type;
    protected PVector center;
    protected PVector normal;
    protected PApplet engine;

    public BoundingShape(PApplet engine, Shape type) {
        this.engine = engine;
        this.type = type;
    }


    public abstract boolean containsMouse();

    public abstract boolean containsPoint(PVector p);

    public abstract void setSize(float x, float y);

    public void setSize(float b) {
        setSize(b,b);
    };

    public abstract void scale(float x, float y);

    public void scale(float b) {
        scale(b,b);
    };

    public abstract void move(PVector position);

    public abstract void rotate(float angle);

    public abstract PVector getSize();

    public abstract AABB getAABB();

    public abstract PVector[] getVertices();

    public abstract void draw();
}