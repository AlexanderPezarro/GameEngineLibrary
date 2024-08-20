public class Label extends UI {
    private PVector offset = new PVector(5,5);
    private final boolean widthHeightSet;
    private String text;
    private int vertTextAlign;
    private int horrTextAlign;

    public Label(float x, float y, float w, float h, String text, Shape shape) {
        super(x, y, w, h, shape, false);
        this.text = text;
        textSize(lookAndFeel.textSize);
        if (w == 0 && h == 0) {
            widthHeightSet = false;
            PVector bsSize = bs.getSize();
            this.w = bsSize.x;
            this.h = bsSize.y;
        } else {
            widthHeightSet = true;
        }
        vertTextAlign = CENTER;
        horrTextAlign = CENTER;
    }

    public void setText(String text) {
        this.text = text;
        textSize(lookAndFeel.textSize);
        this.bs.setSize(textWidth(text) + offset.x*2, (lookAndFeel.textSize*0.8) + offset.y*2);
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
        pushStyle();

        if (isHovered) fill(lookAndFeel.backgroundHoverColour, lookAndFeel.backgroundOpacity);
        else fill(lookAndFeel.backgroundColour, lookAndFeel.backgroundOpacity);
        stroke(lookAndFeel.backgroundOutlineColour, lookAndFeel.backgroundOpacity);
        
        rect(position.x, position.y, w, h, lookAndFeel.cornerRadius);
        
        if (isHovered) fill(lookAndFeel.textHoverColour, lookAndFeel.textOpacity);
        else fill(lookAndFeel.textColour, lookAndFeel.textOpacity);
        textSize(lookAndFeel.textSize);
        textAlign(vertTextAlign, horrTextAlign);
        textFont(lookAndFeel.textFont);
        text(text, position.x, position.y-0.17*textAscent(), w, h);
        
        popStyle();
    }
}