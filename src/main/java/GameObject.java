package main.java;

import processing.core.PVector;

import java.io.Serializable;
import java.util.ArrayList;


public abstract class GameObject implements Serializable {
  protected final GameEngine engine;
  protected PVector position;

  public GameObject(GameEngine engine, PVector position) {
    this.engine = engine;
    this.position = position;
  }

  public GameObject(GameEngine engine, float x, float y) {
    this(engine, new PVector(x, y));
  }

  public abstract void draw();

  public void update() {}

  public void keyPressed() {}

  public void keyReleased() {}

  public void keyTyped() {}

  public void mouseClicked() {}

  public void mouseDragged() {}

  public void mouseMoved() {}

  public void mousePressed() {}

  public void mouseReleased() {}

  public void mouseWheel() {}

  public PVector getPosition() {
    return position;
  }

  public void setPosition(PVector position) {
    this.position = position;
  }
}
