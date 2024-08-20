package main.java;

public class MainMenuScene extends Scene {

    public MainMenuScene(GameEngine engine, SceneManager sm) {
        super(engine, sm, true);

        Label titleScreen =  new Label(engine, engine.width / 2f, engine.height / 2f - 300, 400, 60, "Just In Time", Shape.RECT);
        titleScreen.lookAndFeel.backgroundHoverColour = titleScreen.lookAndFeel.backgroundColour;
        titleScreen.lookAndFeel.textHoverColour = titleScreen.lookAndFeel.textColour;
        Button playBtn = new Button(engine, engine.width/2f, engine.height/2f - 100, 200, 60, "Play", Shape.RECT);
        playBtn.addClickListener(this::clearSelection);

        Button exitBtn = new Button(engine, engine.width/2f, engine.height/2f + 10, 200, 60, "Exit", Shape.RECT);
        exitBtn.addClickListener(engine::exit);
        addUIElement(titleScreen);
        addUIElement(playBtn);
        addUIElement(exitBtn);
    }

    public void draw() {
        drawUIElements();
    }

    public void keyPressed() {
        super.keyPressed();
        if (engine.key == engine.ESC) engine.key = 0;
    }
}