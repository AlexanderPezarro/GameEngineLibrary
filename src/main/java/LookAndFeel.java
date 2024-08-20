package main.java;

import processing.core.PFont;
import processing.core.PGraphics;

import java.awt.*;

public final class LookAndFeel {
    private final PGraphics g = new PGraphics();
    public int backgroundColour = g.color(160);
    public int backgroundHoverColour = g.color(80);
    public int backgroundOutlineColour = g.color(0);
    public int foregroundColour = g.color(200);
    public int foregroundHoverColour = g.color(120);
    public int foregroundOutlineColour = g.color(0);
    public int backgroundOpacity = 255;
    public int textColour = g.color(0);
    public int textHoverColour = g.color(160);
    public int textSize = 40;
    public PFont textFont = new PFont(new Font("SansSerif", Font.PLAIN, textSize), true);
    public int textOpacity = 255;
    public int strokeThickness = 1;
    public int cornerRadius = 5;

    public LookAndFeel() {}

    public LookAndFeel(int backgroundColour, int backgroundOutlineColour, int foregroundColour, int foregroundOutlineColour, int textColour, int backgroundHoverColour, int textHoverColour, int textSize, PFont textFont, int strokeThickness) {
        this.backgroundColour = backgroundColour;
        this.backgroundOutlineColour = backgroundOutlineColour;
        this.foregroundColour = foregroundColour;
        this.foregroundOutlineColour = foregroundOutlineColour;
        this.textColour = textColour;
        this.backgroundHoverColour = backgroundHoverColour;
        this.textHoverColour = textHoverColour;
        this.textSize = textSize;
        this.textFont = textFont;
        this.strokeThickness = strokeThickness;
    }
}