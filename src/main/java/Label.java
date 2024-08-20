package main.java;

import processing.core.PVector;

public class Label extends UI {
    private PVector offset = new PVector(5,5);
    private final boolean widthHeightSet;
    private String text;
    private int vertTextAlign;
    private int horrTextAlign;

    public Label(GameEngine engine, float x, float y, float w, float h, String text, Shape shape) {
        super(engine, x, y, w, h, shape, false);
        this.text = text;
        engine.textSize(lookAndFeel.textSize);
        if (w == 0 && h == 0) {
            widthHeightSet = false;
            PVector bsSize = bs.getSize();
            this.w = bsSize.x;
            this.h = bsSize.y;
        } else {
            widthHeightSet = true;
        }
        vertTextAlign = engine.CENTER;
        horrTextAlign = engine.CENTER;
    }

    public void setText(String text) {
        this.text = text;
        engine.textSize(lookAndFeel.textSize);
        this.bs.setSize(engine.textWidth(text) + offset.x*2, (float) ((lookAndFeel.textSize*0.8) + offset.y*2));
        if (!widthHeightSet) {
            PVector bsSize = bs.getSize();
            w = bsSize.x;
            h = bsSize.y;
        }
    }

    public void setTextAlignment(int vertTextAlign, int horrTextAlign) {
        this.vertTextAlign = vertTextAlign;
        this.horrTextAlign = horrTextAlign;
    }

    @Override
    public void draw() {
        engine.pushStyle();

        if (isHovered) engine.fill(lookAndFeel.backgroundHoverColour, lookAndFeel.backgroundOpacity);
        else engine.fill(lookAndFeel.backgroundColour, lookAndFeel.backgroundOpacity);
        engine.stroke(lookAndFeel.backgroundOutlineColour, lookAndFeel.backgroundOpacity);

        engine.rect(position.x, position.y, w, h, lookAndFeel.cornerRadius);
        
        if (isHovered) engine.fill(lookAndFeel.textHoverColour, lookAndFeel.textOpacity);
        else engine.fill(lookAndFeel.textColour, lookAndFeel.textOpacity);
        engine.textSize(lookAndFeel.textSize);
        engine.textAlign(vertTextAlign, horrTextAlign);
        engine.textFont(lookAndFeel.textFont);
        engine.text(text, position.x, (float)(position.y-0.17*engine.textAscent()), w, h);

        engine.popStyle();
    }
}