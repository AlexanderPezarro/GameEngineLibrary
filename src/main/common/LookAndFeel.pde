public final class LookAndFeel {
    public color backgroundColour = color(160);
    public color backgroundHoverColour = color(80);
    public color backgroundOutlineColour = color(0);
    public color foregroundColour = color(200);
    public color foregroundHoverColour = color(120);
    public color foregroundOutlineColour = color(0);
    public int backgroundOpacity = 255;
    public color textColour = color(0);
    public color textHoverColour = color(160);
    public int textSize = 40;
    public PFont textFont = createFont("SansSerif.plain", textSize);
    public int textOpacity = 255;
    public int strokeThickness = 1;
    public int cornerRadius = 5;

    public LookAndFeel() {}

    public LookAndFeel(color backgroundColour, color backgroundOutlineColour, color foregroundColour, color foregroundOutlineColour, color textColour, color backgroundHoverColour, color textHoverColour, int textSize, PFont textFont, int strokeThickness) {
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