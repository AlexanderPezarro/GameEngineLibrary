public abstract class UI extends GameObject {
    protected float w = 0;
    protected float h = 0;
    protected BoundingShape bs;
    protected Shape shape;
    protected boolean isHovered = false;
    protected boolean selected = false;
    protected boolean canBeSelected = false;
    protected ArrayList<MouseListener> mouseListeners = new ArrayList<MouseListener>();
    protected LookAndFeel lookAndFeel = new LookAndFeel();

    /*-- CONSTRUCTORS --*/

    public UI(float x, float y, float w, float h, Shape shape, boolean canBeSelected) {
        super(x, y);
        this.canBeSelected = canBeSelected;
        this.w = w;
        this.h = h;
        this.shape = shape;
        switch(shape) {
            case CIRCLE:
                w = w > h ? w : h;
                h = w;
                bs = new BoundingCircle(new PVector(x, y), w);
                break;
            case RECT:
                bs = new BoundingBox(new PVector(x, y), w, h);
                break;
            case TRIANGLE:
                bs = new BoundingTriangle(new PVector(x, y), w, h);
                break;
        }
    }

    public UI(float x, float y, Shape shape, boolean canBeSelected) {
        this(x, y, 0, 0, shape, canBeSelected);
    }

    /*-- GETTERS & SETTERS --*/

    public float getWidth() {
        return w;
    }

    public void setWidth(float w) {
        this.w = w;
    }

    public float getHeight() {
        return h;
    }

    public void setHeight(float h) {
        this.h = h;
    }

    public void setZ(float z) {
        position.z = z;
    }

    public void addMouseListener(MouseListener mouse) {
        mouseListeners.add(mouse);
    }

    @Override
    public void setPosition(PVector position) {
        this.position = position;
        bs.move(position);
    }

    public void visualMove(PVector position) {
        this.position = position;
    }

    public void reset() {}

    protected void onMouseEnter() {
        mouseListeners.forEach((mouse) -> mouse.onMouseEnter());
    }

    protected void onMouseStay() {
        mouseListeners.forEach((mouse) -> mouse.onMouseStay());
    }

    protected void onMouseExit() {
        mouseListeners.forEach((mouse) -> mouse.onMouseExit());
    }

    public void keyPressed() {}

    public void keyReleased() {}

    public void keyTyped() {}

    public void mouseClicked() {}

    public void mouseDragged() {}

    public void mouseMoved() {
        boolean isHovering = bs.containsMouse();
        // Wasn't hovering and now is
        if(isHovering && !isHovered) {
            isHovered = true;
            onMouseEnter();
        // Was hovering and now isn't
        } else if (!isHovering && isHovered) {
            isHovered = false;
            onMouseExit();
        } 
        // Is still hovering
        else if (isHovering && isHovered) {
            onMouseStay();
        }
        // Wasn't hovering and still isn't
    }

    public void mousePressed() {}

    public void mouseReleased() {}

    public void mouseWheel() {}
}