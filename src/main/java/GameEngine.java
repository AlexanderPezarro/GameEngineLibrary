package main.java;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class GameEngine implements PConstants {
    PApplet applet;

    final int PHYSIC_FRAMES_PER_SEC = 60;
    final double PHYSIC_MILLISECOND_PER_FRAME = (1.0 / PHYSIC_FRAMES_PER_SEC) * 1000;
    long lastFrameTime = 0;
    long lastPhysicsTime = 0;
    long delta = 0;

    SceneManager sceneManager;
    int physicsCount = 0;

    public GameEngine(PApplet applet) {
        this.applet = applet;

        applet.registerMethod("draw", this);
        applet.registerMethod("mouseEvent", this);
        applet.registerMethod("keyEvent", this);

        setup();
    }

    public void setup() {
        applet.frameRate(60);
        applet.ellipseMode(RADIUS);
        applet.rectMode(CENTER);

//        fullScreen(P3D, 1);

        // Create the scenes
        sceneManager = new SceneManager(this);
        sceneManager.addScene("mainMenu", new MainMenuScene(this, sceneManager));

//        layers = new ArrayList<Layer>();
//        Layer uiLayer = new Layer();
//        Layer gameObjLayer = new Layer();
//
//        // Add things to the layers
//        HText fps;
//        HText text;
//        Button button;
//        Circle ball;
//
//        fps = new HText(this, 0, width-200, 40);
//        fps.setTextColour(71, 168, 189);
//        fps.setSize(32);
//        fps.addComponent(new Component() {
//            @Override
//            void run() {
//                fps.setText("" + physicsCount + " " + frameCount);
//            }
//        });
//
//        text = new HText(this, 0, width/2.0f + 150, height/2.0f);
//        text.setText("I change colour");
//        text.setTextColour(71, 168, 189);
//        text.setSize(32);
//        text.addMouseMotionListener(new MouseMotionAdapter() {
//            @Override
//            public void onMouseEnter(Event event) {
//                // println("text - onMouseEnter");
//                text.setTextColour(224, 130, 7);
//            }
//
//            @Override
//            public void onMouseExit(Event event) {
//                // println("text - onMouseExit");
//                text.setTextColour(71, 168, 189);
//            }
//        });
//        button = new Button(this, 0, width/2.0f - 200, height/2.0f);
//        button.setWidth(140);
//        button.setHeight(50);
//        button.setText("Click me!");
//        button.setTextColour(0, 82, 22);
//        button.setBackgroundColour(140, 140, 140);
//        button.setSize(28);
//        button.setCornerRadius(20);
//        button.addMouseMotionListener(new MouseMotionAdapter() {
//            @Override
//            public void onMouseEnter(Event event) {
//                // println("text - onMouseEnter");
//                button.setTextColour(23, 166, 54);
//                button.setBackgroundColour(50, 50, 50);
//            }
//
//            @Override
//            public void onMouseExit(Event event) {
//                // println("text - onMouseExit");
//                button.setTextColour(0, 82, 22);
//                button.setBackgroundColour(140, 140, 140);
//            }
//        });
//        button.addActionListener(new ActionListener() {
//            boolean clicked = false;
//            @Override
//            public void actionPerformed(Event event) {
//                clicked = !clicked;
//                if(clicked) {
//                    text.setTextColour(23, 166, 54);
//                } else {
//                    text.setTextColour(224, 130, 7);
//                }
//            }
//        });
//
//        uiLayer.addGameObject(fps);
//        uiLayer.addGameObject(text);
//        uiLayer.addGameObject(button);
//
//        ball = new Circle(this, 0, width/2.0f, height/2.0f, 20);
//        ball.setBackgroundColour(255, 255, 255);
//
//        PhysicsComponent comp = new PhysicsComponent();
//        comp.setVelocity(new PVector(10f, 0f));
//        comp.setGravity(new PVector(0, 1f));
//        comp.setPhysicsListener(new PhysicsAdapter() {
//            @Override
//            public void runPhysics() {
//                if (ball.position.y - ball.radius < 0 || ball.position.y + ball.radius > height) {
//                    if (Math.round(ball.position.y/height) == 0) {
//                        ball.position.y = ball.radius;
//                    } else {
//                        ball.position.y = height - ball.radius;
//                    }
//                    comp.getVelocity().y *= -1;
//                }
//                if (ball.position.x - ball.radius < 0 || ball.position.x + ball.radius > width) {
//                    if (Math.round(ball.position.x/width) == 0) {
//                        ball.position.x = ball.radius;
//                    } else {
//                        ball.position.x = width - ball.radius;
//                    }
//                    comp.getVelocity().x *= -1;
//                }
//            }
//        });
//        ball.addComponent(comp);
//
//        gameObjLayer.addGameObject(ball);
//
//        // Add layers to layer list
//        layers.add(uiLayer);
//        layers.add(gameObjLayer);

        lastFrameTime = applet.millis();
        System.out.println("Finished setup");
    }

    public void draw() {
        applet.background(0, 0, 0);

        long drawTime = applet.millis();
        delta = drawTime - lastFrameTime;
        lastFrameTime = drawTime;
        lastPhysicsTime += delta;

        while (lastPhysicsTime >= PHYSIC_MILLISECOND_PER_FRAME) {
            sceneManager.update();
            lastPhysicsTime -= PHYSIC_MILLISECOND_PER_FRAME;
            physicsCount++;
        }

        sceneManager.draw();
    }

    public void mouseEvent(MouseEvent event) {
        switch (event.getAction()) {
            case MouseEvent.PRESS -> sceneManager.mousePressed();
            case MouseEvent.RELEASE -> sceneManager.mouseReleased();
            case MouseEvent.CLICK -> sceneManager.mouseClicked();
            case MouseEvent.DRAG -> sceneManager.mouseDragged();
            case MouseEvent.MOVE -> sceneManager.mouseMoved();
        }
    }

    public void keyEvent(KeyEvent event) {

        switch (event.getAction()) {
            case KeyEvent.PRESS -> sceneManager.keyPressed();
            case KeyEvent.RELEASE -> sceneManager.keyReleased();
            case KeyEvent.TYPE -> sceneManager.keyTyped();
        }
    }
}
