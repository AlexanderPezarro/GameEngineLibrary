import java.io.*;

public final class SceneManager {

    Scene currentScene;
    HashMap<String, Scene> scenes;

    // Scene global vars
    long levelStartTime = 0;
    long levelEndTime = 0;
    String username = "";
    Map<String, Map<String, Float>> levelTimes = new HashMap();
    Level currentLevel = null;
    String levelName = "";

    MusicManager musicManager;

    Camera camera;

    public SceneManager(MusicManager musicManager) {
        this.musicManager = musicManager;
        scenes = new HashMap<>();
        camera = new Camera(new PVector(width / 2, height / 2), true);
        camera.setCamera();

        //initialise levelTimes
        for(int i = 0; i < Names.LEVEL_NAMES.length; i++) {
            levelTimes.put(Names.LEVEL_NAMES[i], new HashMap<String, Float>());
        }
    }

    // Reads the score file and stores the value (if any) into highscore
    // public void readHighScore() {
    //     try(BufferedReader reader = new BufferedReader(new InputStreamReader(
    //         new FileInputStream(SCORE_FILENAME), "utf-8"))) {
    //         String scoreLine = reader.readLine();
    //         int score = 0;
    //         if (scoreLine != null) score = Integer.parseInt(scoreLine.strip());
    //         if (score != 0) highscore = score;
    //     } catch (IOException e) {
    //         println("Score file could not be read from.");
    //     } catch (NumberFormatException e) {
    //         println("Score file doesn't have valid score");
    //     }
    // }

    // Writes highscore to the score file
    // public void writeHighScore() {
    //     try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
    //           new FileOutputStream(SCORE_FILENAME), "utf-8"))) {
    //         writer.write("" + highscore);
    //     } catch (IOException e) {
    //         println("Score file could not be written to.");
    //     }
    // }

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
    }

    // Change the current scene to the given scene and
    // calls its setup method.
    public void changeScene(Scene scene) {
        currentScene = scene;
        camera.setUI(currentScene.isUI);
        currentScene.reset();
        currentScene.activate();
        if("".equals(levelName)) musicManager.playTheme(Names.THEME_NAMES[0]);
        else musicManager.playTheme(levelName);
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