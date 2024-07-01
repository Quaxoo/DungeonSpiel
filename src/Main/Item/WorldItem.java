package Main.Item;

import Main.Level.Level;
import Main.Player;
import Main.Thread.Render;
import Main.Thread.Update;
import States.Constants;
import Util.Coordinate;
import Util.DrawableEntity;
import Util.Sprite;
import Util.Updateable;

import java.awt.*;
import java.util.ArrayList;

public class WorldItem implements DrawableEntity, Updateable{

    protected Item item;
    protected Sprite sprite, shadow;
    protected Coordinate center = new Coordinate();
    protected Rectangle hitbox;

    protected static float scale = 5f * Constants.globalScale;

    private static ArrayList<WorldItem> items = new ArrayList<>();
    protected double moveDistance, moveSpeed, moveTick = 0, move;
    boolean down;
    public WorldItem(Item item, Coordinate center){
        this.item = item;
        moveDistance = 7 * scale;
        moveSpeed = 4;
        loadSprite();

        this.center.set(center.getX() - Level.origin.getX(),center.getY() - Level.origin.getY());
        items.add(this);
        Update.add(Update.Game, this);
        Render.add(Render.Entity, this);
    }

    protected void loadSprite(){
        sprite = new Sprite(item.getIcon());
        shadow = new Sprite(Sprite.Shadow, scale);
        shadow.alpha(0.7f);
        hitbox = new Rectangle();
    }
    public Item getItem(){
        return item;
    }

    @Override
    public void render(Graphics g) {
        shadow.drawCenterX(getCenterX(), getY() + move, g);
        sprite.draw(getX(),getY(),g);
    }

    @Override
    public double getZ() {
        return getY() + sprite.getHeight()/2.0;
    }

    @Override
    public void update() {
        move();
        collect();
    }
    protected double getX(){
        return getCenterX() - sprite.getWidth()/2.0;
    }
    protected double getY(){
        return getCenterY() - sprite.getHeight()/2.0;
    }
    protected double getCenterX(){
        return center.getX()  + Level.origin.getX();
    }
    protected double getCenterY(){
        return center.getY() + Level.origin.getY() - move;
    }

    protected boolean collision(){
        return Player.get().hitbox.intersects(hitbox);
    }
    protected void move(){
        moveTick++;
        if (moveTick % moveSpeed == 0){
            if(move > moveDistance && down){
                down = false;
            }
            if(move < 0 && !down){
                down = true;
            }
            move += (down ? 1 : -1);
        }
        hitbox.setBounds((int) (getCenterX() - hitbox.width/2.0), (int) (getCenterY() - hitbox.height/2.0), (int) (8*scale), (int) (8*scale));
    }
    protected void collect(){
        if(collision() && Player.get().canCollect(item)){
            Player.get().collect(this);
            delete();
        }
    }
    protected void delete(){
        Render.remove(Render.Entity, this);
        Update.remove(Update.Game, this);
    }

    public static void deleteAll(){
        for (WorldItem w: items){
            w.delete();
        }
        items.clear();
    }
}
