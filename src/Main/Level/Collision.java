package Main.Level;

import Main.Hitbox;
import Util.Coordinate;
import Util.NonCollidable;

import java.awt.*;

import static Main.Level.Level.level;

public abstract class Collision {

    public static boolean canMoveX(double xDelta, Hitbox hitbox){

        boolean canX;
        if (xDelta == 0){
            return true;
        }

        if(xDelta < 0){
            canX = isNotColliding(hitbox.getLeftTop().changeX(xDelta)) && isNotColliding(hitbox.getLeftBottom().changeX(xDelta)) && isNotCollidingWithEntityX(xDelta, hitbox);
        }else{
            canX = isNotColliding(hitbox.getRightTop().changeX(xDelta)) && isNotColliding(hitbox.getRightBottom().changeX(xDelta)) && isNotCollidingWithEntityX(xDelta, hitbox);
        }

        return  canX;
    }
    public static boolean canMoveY(double yDelta, Hitbox hitbox){

        boolean canY;
        if (yDelta == 0){
            return true;
        }

        if(yDelta < 0){
            canY = isNotColliding(hitbox.getLeftTop().changeY(yDelta)) && isNotColliding(hitbox.getRightTop().changeY(yDelta)) && isNotCollidingWithEntityY(yDelta, hitbox);
        }else{
            canY = isNotColliding(hitbox.getLeftBottom().changeY(yDelta)) && isNotColliding(hitbox.getRightBottom().changeY(yDelta)) && isNotCollidingWithEntityY(yDelta, hitbox);
        }

        return canY;
    }
    public static boolean canMoveX(double xDelta, Coordinate k){

        boolean canX;
        if (xDelta == 0){
            return true;
        }

        canX = isNotColliding(k.changeX(xDelta));

        return  canX;
    }
    public static boolean canMoveY(double yDelta, Coordinate k){

        boolean canY;
        if (yDelta == 0){
            return true;
        }

        canY = isNotColliding(k.changeY(yDelta));

        return canY;
    }

    public static boolean canMove(double xDelta,double yDelta, Coordinate k){
        return canMoveX(xDelta,k) && canMoveY(yDelta, k);
    }
    private static boolean isNotColliding(Coordinate cord){
        return !Level.levelhitboxes[level].contains(new Point((int) (cord.getX() - Level.origin.getX()), (int) (cord.getY() - Level.origin.getY())));
    }

    private static boolean isNotCollidingWithEntityX(double xDelta, Hitbox hitbox){
        for (NonCollidable c: Level.nonCollidables){
            Rectangle h = new Rectangle(xDelta < 0 ? hitbox.x : hitbox.x + hitbox.width - 1 ,hitbox.y,1,hitbox.height);
            if (c.getHitbox().intersects(h.x + xDelta,h.y, h.width, h.height)){
                return false;
            }
        }
        return true;
    }

    private static boolean isNotCollidingWithEntityY(double yDelta, Hitbox hitbox){
        for (NonCollidable c: Level.nonCollidables){
            Rectangle h = new Rectangle(hitbox.x ,yDelta < 0 ? hitbox.y : hitbox.y + hitbox.height - 1,hitbox.width,1);
            if (c.getHitbox().intersects(h.x,h.y + yDelta,h.width,h.height)){
                return false;
            }
        }
        return true;
    }

}
