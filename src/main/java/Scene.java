package main.java;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected final GameEngine engine;

    protected final SceneManager sceneManager;
    protected final List<UI> uiElements;
    protected final List<UI> uiSelectableElements;
    protected int selectionIndex;
    protected boolean controlPressed;
    protected boolean active;
    protected boolean isUI;

    protected Scene(GameEngine engine, SceneManager sceneManager, boolean isUI) {
        this.engine = engine;
        this.sceneManager = sceneManager;
        uiElements = new ArrayList<>();
        uiSelectableElements = new ArrayList<>();
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
        uiElements.forEach(UI::reset);
    }

    public void update() { uiElements.forEach(GameObject::update); }

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
        uiElements.forEach(GameObject::draw);
    }

    public void keyPressed() {
        if (engine.keyCode == KeyEvent.VK_CONTROL) controlPressed = true;
        
        if (engine.key == engine.TAB) {
            if (controlPressed) previousSelection();
            else nextSelection();
        }
        uiElements.forEach(UI::keyPressed);
    }

    public void keyReleased() {
        if (engine.keyCode == KeyEvent.VK_CONTROL) controlPressed = false;
        uiElements.forEach(UI::keyReleased);
    }

    public void keyTyped() { uiElements.forEach(UI::keyTyped); }

    public void mouseClicked() { uiElements.forEach(UI::mouseClicked); }

    public void mouseDragged() { uiElements.forEach(UI::mouseDragged); }

    public void mouseMoved() { uiElements.forEach(UI::mouseMoved); }

    public void mousePressed() { uiElements.forEach(UI::mousePressed); }

    public void mouseReleased() { uiElements.forEach(UI::mouseReleased); }

    public void mouseWheel() { uiElements.forEach(UI::mouseWheel); }

    public void onEvent(Event event) {}
}