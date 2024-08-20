public class CheckmarkButton extends Button {

    public boolean state = false;

    public CheckmarkButton(float x, float y, float w, float h, String text, Shape shape) {
        super(x, y, w, h, text, shape);
    }

    @Override
    public void draw() {
        pushStyle();
        fill(lookAndFeel.textColour, lookAndFeel.textOpacity);
        
        textSize(lookAndFeel.textSize);
        textAlign(RIGHT, CENTER);
        text(text, position.x- width/2 - 10, position.y - textAscent()*0.18);

        fill(lookAndFeel.backgroundColour, lookAndFeel.backgroundOpacity);
        rect(position.x - width/2, position.y - height/2, width, height, 20);

        fill(lookAndFeel.foregroundColour, lookAndFeel.backgroundOpacity);
        rect(position.x - (width/2 - 10), position.y - (height/2 - 10), width - 20, height - 20);
        
        if (state) {
            stroke(0);
            strokeWeight(2);
            line(position.x - (width/2 - 10), position.y - (height/2 - 10), position.x + (width/2 - 10), position.y + (height/2 - 10));
            line(position.x + (width/2 - 10), position.y - (height/2 - 10), position.x - (width/2 - 10), position.y + (height/2 - 10));

            strokeWeight(1);
        }
        popStyle();
    }

    @Override
    protected void clicked() {
        super.clicked();
        state = !state;
    }
}