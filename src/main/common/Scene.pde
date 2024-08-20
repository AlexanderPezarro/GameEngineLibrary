public abstract class Scene {

    protected final SceneManager sceneManager;
    protected final List<UI> uiElements;
    protected final List<UI> uiSelectableElements;
    protected int selectionIndex;
    protected boolean controlPressed;
    protected boolean active;
    protected boolean isUI;

    protected Scene(SceneManager sceneManager, boolean isUI) {
        this.sceneManager = sceneManager;
        uiElements = new ArrayList();
        uiSelectableElements = new ArrayList();
        selectionIndex = -1;
        controlPressed = false;
        active = false;
        this.isUI = isUI;
    }

    public abstract void draw();

    public void reset() {
        active = false;
        clearSelection();
        controlPressed = false;
        uiElements.forEach(ui -> ui.reset());
    }

    public void update() { uiElements.forEach(ui -> ui.update()); }

    public void activate() { active = true; }

    public void deactivate() {
        active = false;
        clearSelection();
    }

    protected void addUIElement(UI uiElement) {
        uiElements.add(uiElement);
        if (uiElement.canBeSelected) uiSelectableElements.add(uiElement);
    }

    protected void previousSelection() {
        if (uiSelectableElements.isEmpty()) return;
        if (selectionIndex == -1) {
            selectionIndex = uiSelectableElements.size()-1;
            uiSelectableElements.get(selectionIndex).selected = true;
            return;
        }
        
        uiSelectableElements.get(selectionIndex).selected = false;
        if (--selectionIndex < 0) {
            selectionIndex = uiSelectableElements.size()-1;
        }
        uiSelectableElements.get(selectionIndex).selected = true;
    }

    protected void nextSelection() {
        if (uiSelectableElements.isEmpty()) return;
        if (selectionIndex == -1) {
            selectionIndex = 0;
            uiSelectableElements.get(selectionIndex).selected = true;
            return;
        }
        
        uiSelectableElements.get(selectionIndex).selected = false;
        if (++selectionIndex >= uiSelectableElements.size()) {
            selectionIndex = 0;
        }
        uiSelectableElements.get(selectionIndex).selected = true;
    }

    protected void clearSelection() {
        if (selectionIndex != -1) uiSelectableElements.get(selectionIndex).selected = false;
        selectionIndex = -1;
    }

    protected void drawUIElements() {
        uiElements.forEach(ui -> ui.draw());
    }

    public void keyPressed() {
        if (keyCode == KeyEvent.VK_CONTROL) controlPressed = true;
        
        if (key == TAB) {
            if (controlPressed) previousSelection();
            else nextSelection();
        }
        uiElements.forEach(ui -> ui.keyPressed());
    }

    public void keyReleased() {
        if (keyCode == KeyEvent.VK_CONTROL) controlPressed = false;
        uiElements.forEach(ui -> ui.keyReleased());
    }

    public void keyTyped() { uiElements.forEach(ui -> ui.keyTyped()); }

    public void mouseClicked() { uiElements.forEach(ui -> ui.mouseClicked()); }

    public void mouseDragged() { uiElements.forEach(ui -> ui.mouseDragged()); }

    public void mouseMoved() { uiElements.forEach(ui -> ui.mouseMoved()); }

    public void mousePressed() { uiElements.forEach(ui -> ui.mousePressed()); }

    public void mouseReleased() { uiElements.forEach(ui -> ui.mouseReleased()); }

    public void mouseWheel() { uiElements.forEach(ui -> ui.mouseWheel()); }

    public void onEvent(Event event) {}
}