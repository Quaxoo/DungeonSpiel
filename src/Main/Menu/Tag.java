package Main.Menu;

import States.Constants;
import States.Direction;
import Util.Drawable;
import Util.Sprite;
import Util.Text;

import java.awt.*;

public class Tag implements Drawable {

    private final String content;
    private final Sprite origin;
    private Sprite normal, text;
    private static Sprite S, LO, RO, LU, RU;
    private Direction dir;
    private int width, height;
    private float paddingX;
    private float paddingY;
    private float gap;
    private float scale = 5f * Constants.globalScale;
    public Tag(String content, float scale, Sprite origin, float gap, Direction dir){
        this.content = content;
        this.origin = origin;
        this.gap = gap;
        this.scale = scale;
        this.dir = dir;
        loadSprite();
    }
    public static void load(){
        LO = new Sprite(Sprite.ButtonCornerTL);
        RO = new Sprite(Sprite.ButtonCornerTR);
        LU = new Sprite(Sprite.ButtonCornerBL);
        RU = new Sprite(Sprite.ButtonCornerBR);
        S = new Sprite(Sprite.ButtonSlice);
    }
    private void loadSprite(){
        paddingX = 3 * scale;
        paddingY = 2 * scale;

        text = Text.getTranslated(content, scale);
        text.trim();

        Sprite LO = new Sprite(Tag.LO), RO = new Sprite(Tag.RO), LU = new Sprite(Tag.LU), RU = new Sprite(Tag.RU), S = new Sprite(Tag.S);

        height = (int) ((text.getHeight() + 2 * paddingY + 2 * scale)/2.0);

        LO.scaleY(height);
        RO.scaleY(height);
        LU.scaleY(height);
        RU.scaleY(height);
        S.scaleY(height * 2);

        width = LO.getWidth();

        normal = new Sprite(width * 2 + (text.getWidth() - 2 * width + 2 * paddingX) + scale,height * 2);

        LO.drawOn(normal,0,0);
        LU.drawOn(normal,0, height);
        RO.drawOn(normal,normal.getWidth() - width,0);
        RU.drawOn(normal,normal.getWidth() - width, height);

        for (float x = 0; x < (normal.getWidth() - 2 * width); x += S.getWidth()){
            S.drawOn(normal,x + width,0);
        }
        text.drawCenteredOn(normal, 0.5*scale, 0);
        switch (dir){
            case Up -> {
                normal.setCenter(origin.getCenterX(), origin.getY() - normal.getHeight()/2.0 - gap);
            }
            case Down -> {
                normal.setCenter(origin.getCenterX(), origin.getY() + origin.getHeight() + normal.getHeight()/2.0 + gap);
            }
            case Left -> {
                normal.setCenter(origin.getX() - normal.getWidth()/2.0 - gap, origin.getCenterY());
            }
            case Right -> {
                normal.setCenter(origin.getX() + origin.getWidth() + normal.getWidth()/2.0 + gap, origin.getCenterY());
            }
        }
        normal.alpha(0.85f);
    }

    @Override
    public void render(Graphics g) {
        normal.draw(g);
    }


    public void refresh(){
        loadSprite();
    }
}
