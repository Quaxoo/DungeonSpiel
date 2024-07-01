package Main;

import Util.Coordinate;

import java.awt.*;

public class Hitbox extends Rectangle {

    private final Entity entity;
    private int offsetX, offsetY;
    boolean enabled;

    public Hitbox(Entity entity,double offsetX, double offsetY, double width, double height){
        super(0,0,0,0);
        this.entity = entity;
        set(width, height);
        setOffset(offsetX,offsetY);
        enabled = true;
        update();
    }
    public Hitbox(Entity entity){
        super((int)(entity.getCenterX()), (int)(entity.getCenterY()), 0, 0);
        this.entity = entity;
        enabled = true;
    }

    public void update() {
        if (enabled) {
            setBounds((int) (entity.getCenterX() - width / 2 + offsetX), (int) (entity.getCenterY() - height / 2 + offsetY), width, height);
        }
    }

    public void set(double width, double height){
        this.width = (int) (width*entity.scale);
        this.height = (int) (height*entity.scale);
    }
    public void setOffset(double offsetX, double offsetY){
        this.offsetX = (int) (offsetX * entity.scale);
        this.offsetY = (int) (offsetY * entity.scale);
    }

    public Coordinate getLeftTop(){
        return new Coordinate(x,y);
    }
    public Coordinate getLeftBottom(){
        return new Coordinate(x,y+height);
    }

    public Coordinate getRightTop(){
        return new Coordinate(x+width,y);
    }
    public Coordinate getRightBottom(){
        return new Coordinate(x+width,y+height);
    }

    public void disable(){
        enabled = false;
    }
    public void enable(){
        enabled = false;
    }

    public void draw(Graphics g){
        g.drawRect(x,y,width,height);
    }


    public boolean intersects(double x, double y, int w, int h){
        return enabled && super.intersects(x, y, w, h);
    }
}