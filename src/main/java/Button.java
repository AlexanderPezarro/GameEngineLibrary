package main.java;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Button extends UI {

    protected String text;

    protected ArrayList<ClickListener> clickListeners = new ArrayList();

    public Button(GameEngine engine, float x, float y, float w, float h, String text, Shape shape) {
        super(engine, x, y, w, h, shape, true);
        this.text = text;
    }

    public Button(GameEngine engine, float x, float y, float w, float h, Shape shape) {
        this(engine, x, y, w, h, "", shape);
    }


    public void addClickListener(ClickListener listener) {
        clickListeners.add(listener);
    }

    protected void clicked() {
        for (ClickListener clickListener : clickListeners) {
            clickListener.onClick();
        }
    }

    @Override
    public void mouseClicked() {
        if (bs.containsMouse()) {
            clicked();
        }
    }

    @Override
    public void keyPressed() {
        if ((engine.key == engine.ENTER || engine.key == engine.RETURN) && selected) {
            clicked();
        }
    }

    @Override
    public void draw() {
        engine.pushStyle();
        // Draw background
        if (isHovered || selected) engine.fill(lookAndFeel.backgroundHoverColour, lookAndFeel.backgroundOpacity);
        else engine.fill(lookAndFeel.backgroundColour, lookAndFeel.backgroundOpacity);
        engine.stroke(lookAndFeel.backgroundOutlineColour, lookAndFeel.backgroundOpacity);

        switch(shape) {
            case RECT:
                engine.rect(position.x, position.y, w, h, lookAndFeel.cornerRadius);
                break;
            case CIRCLE:
                engine.circle(position.x, position.y, w);
                break;
            case TRIANGLE:
                engine.triangle(position.x, position.y-h/3,position.x-w/2, position.y+h/3,position.x+w/2, position.y+h/3);
                break;
        }

        if (isHovered || selected) engine.fill(lookAndFeel.textHoverColour, lookAndFeel.textOpacity);
        else engine.fill(lookAndFeel.textColour, lookAndFeel.textOpacity);
        engine.textAlign(engine.CENTER, engine.CENTER);
        engine.textSize(lookAndFeel.textSize);
        engine.text(text, position.x, (float)(position.y-0.17*engine.textAscent()), w, h);
        engine.popStyle();
    }
}