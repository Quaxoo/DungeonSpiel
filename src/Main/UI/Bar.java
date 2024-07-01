package Main.UI;

import Main.Player;
import Main.Thread.Render;
import Main.Thread.Update;
import States.Constants;
import Util.Coordinate;
import Util.Drawable;
import Util.Sprite;
import Util.Updateable;

import java.awt.*;

public class Bar implements Updateable, Drawable {
    private Sprite emptybar;
    private Sprite bar;
    private static float scale = 10f * Constants.globalScale;

    private Coordinate center = new Coordinate();
    private double width;

    public Bar(String type, String bartype, Coordinate center){
        this.center.set(center);
        loadSprite(type, bartype);
        Update.add(Update.Game,this);
        Render.add(Render.UI,this);
    }

    private void loadSprite(String sprite, String barSprite){
        bar = new Sprite(barSprite, scale);
        emptybar = new Sprite(sprite, scale);
        width = bar.getWidth();
        emptybar.setCenter(center);
        bar.setPosition(emptybar.getX() + 17 * scale, emptybar.getY());
    }

    @Override
    public void render(Graphics g) {
        bar.draw(g);
        emptybar.draw(g);
    }

    @Override
    public void update() {
        bar.scale(width / ((double) Player.get().getMaxHealth() / (Player.get().getHealth()+1)) , bar.getHeight());
    }
}
