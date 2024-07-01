package Util;

import Input.Mouse;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class MouseReactable {

    protected boolean down, downRight;
    protected boolean colliding;
    protected boolean downButNotMe, downButNotMeRight;


    protected void checkClick(){
        if (!down && !isColliding() && Mouse.isMouseDown(MouseEvent.BUTTON1)){
            downButNotMe = true;
        }else if (!down && downButNotMe && !Mouse.isMouseDown(MouseEvent.BUTTON1)){
            downButNotMe = false;
        }

        if (!down && isColliding() && Mouse.isMouseDown(MouseEvent.BUTTON1) && !downButNotMe){
            down = true;
            doClick();
        }else if(down && !Mouse.isMouseDown(MouseEvent.BUTTON1)){
            down = false;
            doUnclick();
        }

        if (!downRight && !isColliding() && Mouse.isMouseDown(MouseEvent.BUTTON3)){
            downButNotMeRight = true;
        }else if (!downRight && downButNotMeRight && !Mouse.isMouseDown(MouseEvent.BUTTON3)){
            downButNotMeRight = false;
        }

        if (!downRight && isColliding() && Mouse.isMouseDown(MouseEvent.BUTTON3)){
            downRight = true;
            doRightClick();
        }else if(downRight && !Mouse.isMouseDown(MouseEvent.BUTTON3)){
            downRight = false;
            doRightUnclick();
        }
    }

    protected void checkCollision(){
        if (!colliding && isColliding()){
            colliding = true;
            doOnCollision();
        }else if (!isColliding()){
            colliding = false;
            doOnLeftCollision();
        }
    }

    protected boolean isColliding(){
        try {
            Coordinate maus = Mouse.get();
            return getBounds().contains(maus.getX(), maus.getY());
        }catch (Exception e){
            return false;
        }

    }
    protected abstract Rectangle getBounds();
    protected void doClick() {

    }
    protected void doUnclick() {


    }
    protected void doRightClick() {

    }
    protected void doRightUnclick() {

    }
    protected void doOnCollision(){

    }
    protected void doOnLeftCollision(){

    }

}
