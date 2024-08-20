public class TextBox extends UI {
    final int KEY_DELAY = 150;

    StringBuilder text;
    int lastKey;
    long lastKeyPress;

    public TextBox(float x, float y, float w, float h, Shape shape) {
        super(x, y, w, h, shape, true);
        text = new StringBuilder();
        lastKey = -1;
        lastKeyPress = 0;
    }

    public void update() {
        if (selected && lastKey != -1) {
            long time = millis();
            if (keyPressed && (time - lastKeyPress >= KEY_DELAY)) {
                lastKeyPress = time;
                if (lastKey == BACKSPACE) {
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
        cursor(TEXT);
    }

    protected void onMouseExit() {
        super.onMouseExit();
        cursor(ARROW);
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
            if (key == BACKSPACE || Character.isLetterOrDigit(key)) {
                lastKey = key;
                lastKeyPress = millis();
                if (key == BACKSPACE) {
                    if (!text.isEmpty()) text.deleteCharAt(text.length()-1);
                }
                else if(text.length() <= 8) text.append(key);
            }
        }
    }

    @Override
    public void keyReleased() {
        if (key == BACKSPACE || Character.isLetterOrDigit(key)) {
            lastKey = -1;
        }
    }

    public void draw() {
        pushStyle();
        stroke(lookAndFeel.backgroundOutlineColour, lookAndFeel.backgroundOpacity);
        if (isHovered || selected) fill(lookAndFeel.backgroundHoverColour, lookAndFeel.backgroundOpacity);
        else fill(lookAndFeel.backgroundColour, lookAndFeel.backgroundOpacity);
        rect(position.x, position.y, w, h, lookAndFeel.cornerRadius);

        stroke(lookAndFeel.foregroundOutlineColour, lookAndFeel.backgroundOpacity);
        if (isHovered || selected) fill(lookAndFeel.foregroundHoverColour, lookAndFeel.backgroundOpacity);
        else fill(lookAndFeel.foregroundColour, lookAndFeel.backgroundOpacity);
        rect(position.x, position.y, w-10, h-10);

        strokeWeight(lookAndFeel.strokeThickness);
        if (isHovered || selected) fill(lookAndFeel.textHoverColour, lookAndFeel.textOpacity);
        else fill(lookAndFeel.textColour, lookAndFeel.textOpacity);;
        
        textSize(lookAndFeel.textSize);
        textAlign(LEFT, CENTER);
        text(text.toString(), position.x - w/2+10, position.y - textAscent()*0.18);
        
        popStyle();
    }
}