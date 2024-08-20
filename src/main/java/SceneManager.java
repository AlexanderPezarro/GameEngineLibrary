package main.java;

import processing.core.PVector;

import java.util.HashMap;
import java.util.Map;

public final class SceneManager {

    private GameEngine engine;
    private Scene currentScene;
    private HashMap<String, Scene> scenes;

    // Scene global vars

    public SceneManager(GameEngine engine) {
        scenes = new HashMap<>();
    }

    public void draw() {
        currentScene.draw();
    }

    public void update() {
        currentScene.update();
    }

    public void keyPressed() {
        currentScene.keyPressed();
    }

    public void keyReleased() {
        currentScene.keyReleased();
    }

    public void keyTyped() {
        currentScene.keyTyped();
    }

    public void mouseClicked() {
        currentScene.mouseClicked();
    }

    public void mouseDragged() {
        currentScene.mouseDragged();
    }

    public void mouseMoved() {
        currentScene.mouseMoved();
    }

    public void mousePressed() {
        currentScene.mousePressed();
    }

    public void mouseReleased() {
        currentScene.mouseReleased();
    }

    public void mouseWheel() {
        currentScene.mouseWheel();
    }

    public void onEvent(Event event) {
        currentScene.onEvent(event);
    }

    public void addScene(String sceneName, Scene scene) {
        scenes.put(sceneName, scene);
        if (currentScene == null) {
            changeScene(scene);
        }
    }

    // Change the current scene to the given scene and
    // calls its setup method.
    public void changeScene(Scene scene) {
        currentScene = scene;
        currentScene.reset();
        currentScene.activate();
    }

    // Change the current scene to the given sceneName.
    // If the sceneName doesn't exist, do nothing;
    public void changeScene(String sceneName) {
        changeScene(scenes.getOrDefault(sceneName, currentScene));
    }

    public void resetGameScene() {
        scenes.get("game").reset();
    }
}