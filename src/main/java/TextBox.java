package main.java;

public class TextBox extends UI {
    final int KEY_DELAY = 150;

    StringBuilder text;
    int lastKey;
    long lastKeyPress;

    public TextBox(GameEngine engine, float x, float y, float w, float h, Shape shape) {
        super(engine, x, y, w, h, shape, true);
        text = new StringBuilder();
        lastKey = -1;
        lastKeyPress = 0;
    }

    public void update() {
        if (selected && lastKey != -1) {
            long time = engine.millis();
            if (engine.keyPressed && (time - lastKeyPress >= KEY_DELAY)) {
                lastKeyPress = time;
                if (lastKey == engine.BACKSPACE) {
                    if (!text.isEmpty()) text.deleteCharAt(text.length()-1);
                } else if(text.length() <= 8){
                    text.append((char)lastKey);
                }
            }
        }
    }

    public void reset() {
        text.setLength(0);
    }

    protected void onMouseEnter() {
        super.onMouseEnter();
        engine.cursor(engine.TEXT);
    }

    protected void onMouseExit() {
        super.onMouseExit();
        engine.cursor(engine.ARROW);
    }

    @Override
    public void mouseClicked() {
        if (bs.containsMouse()) {
            selected = true;
        }
    }

    @Override
    public void keyPressed() {
        if (selected) {
            if (engine.key == engine.BACKSPACE || Character.isLetterOrDigit(engine.key)) {
                lastKey = engine.key;
                lastKeyPress = engine.millis();
                if (engine.key == engine.BACKSPACE) {
                    if (!text.isEmpty()) text.deleteCharAt(text.length()-1);
                }
                else if(text.length() <= 8) text.append(engine.key);
            }
        }
    }

    @Override
    public void keyReleased() {
        if (engine.key == engine.BACKSPACE || Character.isLetterOrDigit(engine.key)) {
            lastKey = -1;
        }
    }

    public void draw() {
        engine.pushStyle();
        engine.stroke(lookAndFeel.backgroundOutlineColour, lookAndFeel.backgroundOpacity);
        if (isHovered || selected) engine.fill(lookAndFeel.backgroundHoverColour, lookAndFeel.backgroundOpacity);
        else engine.fill(lookAndFeel.backgroundColour, lookAndFeel.backgroundOpacity);
        engine.rect(position.x, position.y, w, h, lookAndFeel.cornerRadius);

        engine.stroke(lookAndFeel.foregroundOutlineColour, lookAndFeel.backgroundOpacity);
        if (isHovered || selected) engine.fill(lookAndFeel.foregroundHoverColour, lookAndFeel.backgroundOpacity);
        else engine.fill(lookAndFeel.foregroundColour, lookAndFeel.backgroundOpacity);
        engine.rect(position.x, position.y, w-10, h-10);

        engine.strokeWeight(lookAndFeel.strokeThickness);
        if (isHovered || selected) engine.fill(lookAndFeel.textHoverColour, lookAndFeel.textOpacity);
        else engine.fill(lookAndFeel.textColour, lookAndFeel.textOpacity);;

        engine.textSize(lookAndFeel.textSize);
        engine.textAlign(engine.LEFT, engine.CENTER);
        engine.text(text.toString(), position.x - w/2+10, (float)(position.y - engine.textAscent()*0.18));

        engine.popStyle();
    }
}