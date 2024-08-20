final int framePerSec = 30;
final float millisecPerFrame = (1.0 / framePerSec) * 1000;

long lastFrameTime = 0;
long lastUpdateTime = 0;
long delta = 0;

SceneManager sceneManager;
MusicManager musicManager;

void setup() {
    ellipseMode(RADIUS);
    rectMode(CENTER);

    fullScreen(P3D, 2);
    musicManager = new MusicManager(this);
    sceneManager = new SceneManager(musicManager);
    Scene mainMenuScene = new MainMenuScene(sceneManager);
    Scene usernameScene = new UsernameScene(sceneManager);
    Scene gameScene = new GameScene(sceneManager, musicManager);
    Scene levelSelectorScene = new LevelSelectorScene(sceneManager);

    sceneManager.addScene("mainMenu", mainMenuScene);
    sceneManager.addScene("username", usernameScene);
    sceneManager.addScene("game", gameScene);
    sceneManager.addScene("levelSelector", levelSelectorScene);
    
    
    sceneManager.changeScene("mainMenu");
    musicManager.playTheme(Names.THEME_NAMES[0]);  
}

void draw() {
    background(0);
    
    //long drawTime = millis();
    //delta = drawTime - lastFrameTime;
    //lastFrameTime = drawTime;

    //lastUpdateTime += delta;

    //while (lastUpdateTime >= millisecPerFrame) {
    sceneManager.update();
    //lastUpdateTime -= millisecPerFrame;
    //}

    sceneManager.draw();
}

void keyPressed() {
    sceneManager.keyPressed();
}

void keyReleased() {
    sceneManager.keyReleased();
}

void keyTyped() {
    sceneManager.keyTyped();
}

void mouseClicked() {
    sceneManager.mouseClicked();
}

void mouseDragged() {
    sceneManager.mouseDragged();
}

void mouseMoved() {
    sceneManager.mouseMoved();
}

void mousePressed() {
    sceneManager.mousePressed();
}

void mouseReleased() {
    sceneManager.mouseReleased();
}

void mouseWheel() {
    sceneManager.mouseWheel();
}

void emitEvent(Event event) {
    sceneManager.onEvent(event);
}
