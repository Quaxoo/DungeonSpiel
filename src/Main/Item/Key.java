package Main.Item;

import Main.Level.Level;
import Main.Player;
import Main.Thread.Render;
import Main.Thread.Update;
import States.Constants;
import Util.Coordinate;
import Util.Sprite;

import java.awt.*;

public class Key extends WorldItem{

    public Key(int x, int y) {
        super(Item.Key , new Coordinate((x + 0.5) * Level.scaledDimension, (y + 0.4) * Level.scaledDimension));
    }

    protected void loadSprite(){
        sprite = new Sprite(item.getIcon());
        shadow = new Sprite(Sprite.Shadow, scale - 0.8f * Constants.globalScale);
        shadow.alpha(0.7f);
        hitbox = new Rectangle();
    }

    protected void collect(){
        if(collision()){
            Player.get().collectKey();
            Render.remove(Render.Entity, this);
            Update.remove(Update.Game, this);
        }
    }

    public void render(Graphics g) {
        shadow.drawCenterX(getCenterX(), getY() + move, g);
        sprite.draw(getX(),getY(),g);
    }
}
