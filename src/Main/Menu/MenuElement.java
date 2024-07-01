package Main.Menu;

import Util.*;

public abstract class MenuElement extends MouseReactable implements Drawable, Updateable {
    protected Coordinate center;
    protected Sprite normal;

    public MenuElement(Coordinate center){
        this.center = center;
    }

    public double getX(){
        return center.getX() - getSprite().getWidth()/2.0;
    }
    public double getY(){
        return center.getY() - getSprite().getHeight()/2.0;
    }
    public double getCenterX(){
        return center.getX();
    }
    public double getCenterY(){
        return center.getY();
    }

    public int getWidth(){
        return getSprite().getWidth();
    }
    public int getHeight(){
        return getSprite().getHeight();
    }

    protected abstract Sprite getSprite();

    @Override
    protected void doClick() {

    }

    @Override
    protected void doUnclick() {

    }

    @Override
    protected void doOnCollision() {

    }

    @Override
    protected void doOnLeftCollision() {

    }

    protected void refresh(){
    }

}
