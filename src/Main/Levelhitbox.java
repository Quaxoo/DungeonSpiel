package Main;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import static Main.Level.Level.origin;

public class Levelhitbox extends Ellipse2D.Double {
    private final Entity entity;
    private int offsetX, offsetY;
    boolean enabled;
    public Levelhitbox(Entity entity, double offsetX, double offsetY, double width, double height){
        super();
        this.entity = entity;
        set(width, height);
        setOffset(offsetX, offsetY);
        enabled = true;
        update();
    }

    public void update() {
        if (enabled) {
            setFrame((int) (entity.getCenterX() - width / 2 + offsetX), (int) (entity.getCenterY() - height / 2 + offsetY), width, height);
        }
    }

    public void set(double width, double height){
        this.width = (int) (width * entity.scale);
        this.height = (int) (height * entity.scale);
    }
    public void setOffset(double offsetX, double offsetY){
        this.offsetX = (int) (offsetX * entity.scale);
        this.offsetY = (int) (offsetY * entity.scale);
    }

    public Ellipse2D getUp(double deltaY){
        return new Ellipse2D.Double(x - origin.getX(),y - deltaY - origin.getY(), width, height);
    }
    public Ellipse2D getDown(double deltaY){
        return new Ellipse2D.Double(x - origin.getX(),y + deltaY - origin.getY(), width, height);
    }

    public Ellipse2D getLeft(double deltaX){
        return new Ellipse2D.Double(x - deltaX - origin.getX(), y - origin.getY(), width, height);
    }
    public Ellipse2D getRight(double deltaX){
        return new Ellipse2D.Double(x + deltaX - origin.getX(), y - origin.getY(), width, height);
    }

    public void disable(){
        enabled = false;
    }
    public void enable(){
        enabled = true;
    }

    public void draw(Graphics g){
        g.drawOval((int)x,(int)y,(int)width,(int)height);
    }

}
