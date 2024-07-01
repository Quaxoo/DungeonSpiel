package Main.UI.Inventory;

import Input.Mouse;
import States.Direction;
import Util.Drawable;
import Util.Sprite;
import Util.Text;
import Util.Updateable;

import java.awt.*;

import static States.Direction.Left;
import static States.Direction.Right;

public class InventoryTag implements Drawable, Updateable {

    private final String content;
    private final Sprite origin;
    private Sprite additonalInfo;
    private Direction dir;
    private Sprite normal, text;
    private static Sprite ST, SL, SB, SR, LO, RO, LU, RU, B;
    private int dimension, height;
    private float paddingX,paddingY,scale, gapY;

    public InventoryTag(String content, Sprite origin, float scale, Sprite additonalInfo){
        this.content = content;
        this.origin = origin;
        this.scale = scale;
        if (additonalInfo != null) {
            this.additonalInfo = new Sprite(additonalInfo);
        }
        loadSprite();
    }
    public static void load(){
        LO = new Sprite(Sprite.ButtonCornerTL);
        RO = new Sprite(Sprite.ButtonCornerTR);
        LU = new Sprite(Sprite.ButtonCornerBL);
        RU = new Sprite(Sprite.ButtonCornerBR);
        Sprite s = new Sprite(Sprite.ButtonSlice);
        s.cut(0,0,0,s.getHeight()/2.0);
        ST = new Sprite(s);
        s.rotate(90);
        SR = new Sprite(s);
        s.rotate(90);
        SB = new Sprite(s);
        s.rotate(90);
        SL = new Sprite(s);
        B = new Sprite(SL.getSubimage(s.getWidth()/2.0, 0,1,1));
    }
    private void loadSprite(){
        paddingX = 2f * scale;
        paddingY = 4f * scale;
        gapY = 5f * scale;

        text = Text.getTranslated(content, scale);
        text.trim();

        int infoHeight = additonalInfo != null ? additonalInfo.getHeight() : 0;
        int infoWidth = additonalInfo != null ? additonalInfo.getWidth() : 0;

        Sprite LO = new Sprite(InventoryTag.LO, scale), RO = new Sprite(InventoryTag.RO, scale), LU = new Sprite(InventoryTag.LU, scale), RU = new Sprite(InventoryTag.RU,scale), SL = new Sprite(InventoryTag.SL,scale), ST = new Sprite(InventoryTag.ST,scale), SR = new Sprite(InventoryTag.SR,scale), SB = new Sprite(InventoryTag.SB,scale), B = new Sprite(InventoryTag.B,scale);

        height = (int) (text.getHeight() + infoHeight + 2 * paddingY + (additonalInfo != null ? gapY : 0) + 4 *  scale);

        dimension = LO.getWidth();

        normal = new Sprite(Math.max(text.getWidth(), infoWidth) + 2 * paddingX + 4 * scale, height);

        LO.drawOn(normal,0,0);
        LU.drawOn(normal,0, height - dimension);
        RO.drawOn(normal,normal.getWidth() - dimension,0);
        RU.drawOn(normal,normal.getWidth() - dimension, height - dimension);

        normal.fillX(ST, dimension, dimension, 0);
        normal.fillY(SL, dimension, dimension, 0);
        normal.fillX(SB, dimension, dimension, height - dimension);
        normal.fillY(SR, dimension, dimension, normal.getWidth() - dimension);
        B.scale(normal.getWidth() - 2 * dimension, normal.getHeight() - 2 * dimension);
        B.drawOn(normal,dimension,dimension);

        text.drawCenteredXOn(normal, 0, paddingY + 2 * scale);
        if(additonalInfo != null) {
            additonalInfo.drawOn(normal, 2 * scale + paddingX, text.getHeight() + paddingY + gapY + 2 * scale);
        }
        normal.setPosition(Mouse.get());
        normal.alpha(0.85f);
    }

    @Override
    public void render(Graphics g) {
        normal.draw(g);
    }

    public void refresh(){
        loadSprite();
    }

    @Override
    public void update() {
        normal.setPosition(Mouse.get());
    }

    private void setRichtung(){
        double winkel = getWinkelzuCursor();

        if(winkel > 315 || winkel <= 135){
            dir = Right;
        }
        if(winkel > 135 && winkel <= 315){
            dir = Left;
        }
    }
    private double getWinkelzuCursor(){

        double gegen = Mouse.get().getX() - origin.getCenterX();
        double an = Mouse.get().getY() - origin.getCenterY();

        double winkel = Math.toDegrees(Math.atan2(an,gegen));

        return winkel + (winkel < 0 ? 360: 0);
    }
}
