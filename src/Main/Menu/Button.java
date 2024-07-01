package Main.Menu;

import States.Constants;
import Util.Coordinate;
import Util.Executable;
import Util.Sprite;
import Util.Text;

import java.awt.*;

public class Button extends MenuElement {
    private final Executable e;
    private final String content;
    private Sprite text, hovered, clicked;
    private static Sprite S, SH, LO, RO, LU, RU, LOH, ROH, LUH, RUH;

    protected int width = 10;

    protected int height = 10;
    protected float paddingX = (8*scale);
    protected float paddingY = (10*scale);
    private static final float scale = 3f * Constants.globalScale;

    public Button(Coordinate center, String content, Executable e){
        super(center);
        this.content = content;
        this.e = e;
        loadSprite();
    }

    public static void load(){
        LO = new Sprite(Sprite.ButtonCornerTL);
        RO = new Sprite(Sprite.ButtonCornerTR);
        LU = new Sprite(Sprite.ButtonCornerBL);
        RU = new Sprite(Sprite.ButtonCornerBR);
        S = new Sprite(Sprite.ButtonSlice);

        LOH = new Sprite(Sprite.ButtonCornerTLHovered);
        ROH = new Sprite(Sprite.ButtonCornerTRHovered);
        LUH = new Sprite(Sprite.ButtonCornerBLHovered);
        RUH = new Sprite(Sprite.ButtonCornerBRHovered);
        SH = new Sprite(Sprite.ButtonSliceHovered);
    }

    private void loadSprite(){
        text = Text.getTranslated(content, scale);
        text.trim();

        Sprite LO = new Sprite(Button.LO), RO = new Sprite(Button.RO), LU = new Sprite(Button.LU), RU = new Sprite(Button.RU), S = new Sprite(Button.S);
        Sprite LOH = new Sprite(Button.LOH), ROH = new Sprite(Button.ROH), LUH = new Sprite(Button.LUH), RUH = new Sprite(Button.RUH), SH = new Sprite(Button.SH);

        height = (int) ((text.getHeight() + 2 * paddingY + 2 * scale)/2.0);

        LO.scaleY(height);
        RO.scaleY(height);
        LU.scaleY(height);
        RU.scaleY(height);

        LOH.scaleY(height);
        ROH.scaleY(height);
        LUH.scaleY(height);
        RUH.scaleY(height);

        width = LO.getWidth();

        S.scaleY(height * 2);
        SH.scaleY(height * 2);

        normal = new Sprite(width * 2 + (text.getWidth() - 2 * width + 2 * paddingX) + scale,height * 2);
        hovered = new Sprite(width * 2 + (text.getWidth() - 2 * width + 2 * paddingX) + scale,height * 2);

        LO.drawOn(normal,0,0);
        LU.drawOn(normal,0, height);
        RO.drawOn(normal,normal.getWidth() - width,0);
        RU.drawOn(normal,normal.getWidth() - width, height);

        LOH.drawOn(hovered,0,0);
        LUH.drawOn(hovered,0, height);
        ROH.drawOn(hovered,hovered.getWidth() - width, 0);
        RUH.drawOn(hovered,hovered.getWidth() - width, height);

        for (float x = 0; x < (normal.getWidth() - 2 * width); x += S.getWidth()){
            S.drawOn(normal,x + width,0);
            SH.drawOn(hovered,x + width,0);
        }
        text.drawCenteredOn(normal, 0.5*scale, 0);
        text.drawCenteredOn(hovered, 0.5*scale, 0);

        clicked = new Sprite(hovered);
        clicked.scale(0.98f);

        hovered.scale(1.02f);

        normal.setCenter(center);
        clicked.setCenter(center);
        hovered.setCenter(center);
    }
    @Override
    public void render(Graphics g) {
        getSprite().draw(g);
    }

    @Override
    public void update() {
        checkClick();
        checkCollision();
    }

    protected void refresh(){
        loadSprite();
    }

    @Override
    protected Rectangle getBounds() {
        return down ? clicked.getBounds() : normal.getBounds();
    }

    @Override
    protected void doUnclick(){
        e.execute();
    }

    @Override
    protected void doOnLeftCollision() {
        down = false;
    }

    protected Sprite getSprite(){
        if (!isColliding()){
            return normal;
        }else if (down){
            return clicked;
        }else {
            return hovered;
        }
    }
}