public class GameScene extends Scene {
    final String[] TILEMAP_FILES = {"Tilemaps/Test2.json"};
    final String[] TILESET_FILES = {"Tilesets/Tileset-001.png"};
    final PVector CENTERSCREEN = new PVector(width / 2, height / 2);
    
    Player player;
    //Camera camera;
    Level level;
    int levelNo;
    Scene startLevelScene;
    Scene pauseScene;
    Scene endLevelScene;

    MusicManager musicManager;
    String themeName;

    LevelManager levelManager;

    JSONReader tilemapReader;

    Tile[][] tilemap;

    public GameScene(SceneManager sm, MusicManager mm) {
        super(sm, false);
        musicManager = mm;
        player = new Player(width / 2, height / 2, musicManager);
        sceneManager.camera.setTarget(player.position);

        startLevelScene = new StartLevelScene(sceneManager, sceneManager.camera.position, musicManager);
        startLevelScene.activate();
        pauseScene = new PauseScene(sceneManager, sceneManager.camera.position);
        endLevelScene = new EndLevelScene(sceneManager, sceneManager.camera.position);

        sceneManager.levelStartTime = 0;
        sceneManager.levelEndTime = 0;

        levelNo = 0;

        levelManager = new LevelManager(musicManager, player);
        for (int i = 0; i < Names.TILEMAP_FILES.length; i++) {
            levelManager.addLevel(Names.LEVEL_NAMES[i], Names.TILEMAP_FILES[i], Names.TILESET_FILES[0]);
        }
        
        levelManager.changeLevel(Names.LEVEL_NAMES[levelNo]);
        themeName = Names.LEVEL_NAMES[levelNo];
    }

    public void reset() {
        System.out.println("Trying to reset");
        levelManager.reset();
        player.velocity.set(0, 0);
        player.pressed.clear();
        player.rotation = 0;

        try {
            tilemapReader = new JSONReader();
            levelManager.changeLevel(sceneManager.levelName);

            player.moveToStart(levelManager.currentLevel.startPosition);

            sceneManager.camera.position.set(levelManager.currentLevel.startPosition);
            sceneManager.camera.velocity.set(0, 0);
            sceneManager.camera.setCamera();
            sceneManager.camera.setTarget(player.position);

            startLevelScene.reset();
            startLevelScene.activate();
            pauseScene = new PauseScene(sceneManager, sceneManager.camera.position);
            endLevelScene = new EndLevelScene(sceneManager, sceneManager.camera.position);

            sceneManager.levelStartTime = 0;
            sceneManager.levelEndTime = 0;
        } catch(Exception e) {
            System.out.println("ERROR: FILE NOT FOUND - " + e);
        }
    }

    public void update() {
        if (levelManager.currentLevel.finished) {
            sceneManager.camera.setUI(endLevelScene.isUI);
            musicManager.playSFX(Names.SFX_NAMES[5]);
            sceneManager.changeScene(endLevelScene);
        }
        if (startLevelScene.active) {
            startLevelScene.update();
            return;
        }
        if (pauseScene.active) return;
        if (endLevelScene.active) endLevelScene.update();
        
        if (!endLevelScene.active) sceneManager.camera.update();
        player.update();
        levelManager.update();
    }

    public void draw() {
        levelManager.draw();
        player.draw();
        if (startLevelScene.active) startLevelScene.draw();
        else if (endLevelScene.active) endLevelScene.draw();
        else if (pauseScene.active) {
            pauseScene.draw();
        }
    }

    void keyPressed() {
        if (key == '0') {
            endLevelScene.activate();
            player.clearKeys();
        }

        if (startLevelScene.active) startLevelScene.keyPressed(); 
        else if (endLevelScene.active) endLevelScene.keyPressed();
        else if (pauseScene.active) pauseScene.keyPressed();
        else {
            player.keyPressed();
            if (key == ESC) {
                sceneManager.camera.setUI(pauseScene.isUI);
                //sceneManager.camera.position = CENTERSCREEN;
                pauseScene.activate();
                key = 0;
                player.clearKeys();
            }
        }
    }

    void keyReleased() {
        player.keyReleased();
    }

    public void mouseMoved() {
        if (endLevelScene.active) endLevelScene.mouseMoved();
        else if (pauseScene.active) pauseScene.mouseMoved();
    }

    public void mouseClicked() {
        if (endLevelScene.active) endLevelScene.mouseClicked();
        else if (pauseScene.active) pauseScene.mouseClicked();
    }
}
