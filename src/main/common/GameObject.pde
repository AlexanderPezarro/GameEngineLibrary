public abstract class GameObject {

    protected PVector position;

    public GameObject(PVector position) {
        this.position = position;
    }

    public GameObject(float x, float y) {
        this.position = new PVector(x, y);
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