package Main;

import java.awt.*;
import java.awt.geom.Arc2D;

import static States.Direction.Left;
import static States.Direction.Up;

public class Attackhitbox extends Arc2D.Float {

    private float ex;
    private final Entity entity;

    public Attackhitbox(Entity entity){
        super((float) (entity.getCenterX() + 0), (float)(entity.getCenterY() + 0), 0, 0, 0, 0, Arc2D.PIE);
        this.ex = 180;
        this.entity = entity;
    }

    public void update(int now, int max){
        extent = (float) ( (ex / (max/2.0) ) * (now >= max/2.0 ? (Math.abs(now-max)) : now));
        if(entity.getDirection() == Left || entity.getDirection() == Up){
            extent *= -1;
        }
        switch (entity.getDirection()){
            case Right -> start = (float) (270 + (180 - ex)/2.0);
            case Left, Down -> start = (float) (180 + (180 - ex)/2.0);
            case Up -> start = (float) (90 + (180 - ex)/2.0);
        }
    }
    public void set(double xOffset, double yOffset, double radius){
        x = (float) ((entity.getCenterX() + xOffset) - radius/2.0);
        y = (float) ((entity.getCenterY() + yOffset) - radius/2.0);
        this.width = (float) radius;
        this.height = (float) radius;
    }
    public void reset(){
        x = (float) ((entity.getCenterX() + 0) - 0/2.0);
        y = (float) ((entity.getCenterY() + 0) - 0/2.0);
        this.width = (float) 0;
        this.height = (float) 0;
    }
    public void set(double xOffset, double yOffset, double width, double height){
        x = (float) ((entity.getCenterX() + xOffset) - width/2.0);
        y = (float) ((entity.getCenterY() + yOffset) - height/2.0);
        this.width = (float) width;
        this.height = (float) height;
    }
    public void setSize(double extent){
        ex = (float) extent;
    }

    public void draw(Graphics g){
        g.drawArc((int) x,(int) y,(int) width,(int) height,(int) start,(int) extent);
    }
}