package Main;

import Main.Item.Item;
import Main.Item.Weapon;
import Main.Level.Collision;
import Main.Level.Level;
import Main.Thread.Render;
import Main.Thread.Update;
import States.Direction;
import Util.Coordinate;
import Util.Random;

import java.util.ArrayList;

import static States.Direction.*;
import static States.State.*;

public abstract class Enemy extends Entity{
    protected boolean moving;
    protected int movingDurationMin, movingDurationMax;
    protected int movingIntervalMin, movingIntervalMax;
    protected int movingTick;
    protected boolean hunting;
    protected double distance;
    protected float huntingSpeed;
    protected float damage;
    protected double huntingStart, huntingEnd;
    protected double huntX, huntY, huntConstant;
    protected double attackDistance;
    protected int attackCooldown;
    protected ArrayList<Direction> blocked = new ArrayList<>();
    protected static final ArrayList<Item> loot = new ArrayList<>();
    protected static ArrayList<Float> probability = new ArrayList<>();

    public Enemy(Coordinate position, String id) {
        super(position, id);
        Level.addEntity(this);
    }

    protected void checkHit(){
        if (Player.get().attackhitbox.intersects(hitbox)){
            if(!hit) {
                hit = true;
                takeDamage();
            }
        }else{
            hit = false;
        }
    }

    protected void takeDamage(){
        int type = Damagenumber.Enemy;
        Weapon weapon = Player.get().getWeapon();
        int damage = weapon.getDamage();
        if (Math.random() <= weapon.getCritChance()){
            damage = weapon.getCritdamage();
            type = Damagenumber.Crit;
        }
        health -= damage;

        new Damagenumber(getCenterX() - Level.origin.getX(), position.getY(),damage,type);

        if(health <= 0){
            die();
        }
    }

    protected boolean canSee(){
        setHuntDirection();
        for (int i = 1; i <= (int) (distance/huntingSpeed); i += 5){
            if (!Collision.canMove(i*huntX, i*huntY, getCenter())){
                return false;
            }
        }
        return true;
    }

    protected void tryHunt(){
        if(state == MeleeAttack){return;}
        distance = Math.sqrt(Math.pow( Player.get().getCenterX()-getCenterX() ,2)
                + Math.pow( Player.get().getCenterY() - getCenterY() ,2) );
        if (!hunting && distance <= huntingStart && canSee()){
            hunting = true;
            playHuntSound();
            stopMovement();
        }else if(hunting){
            if (distance >= huntingEnd){
                setState(Standing);
                hunting = false;
            }else if(distance > 15 * scale){
                setState(Walking);
                if (distance < attackDistance && attackCooldown <= 0){
                    attack();
                }
                hunt();
            }else {
                setState(Standing);
                if (distance < attackDistance && attackCooldown <= 0){
                    attack();
                }
            }
        }
    }

    protected void hunt(){
        if(state == MeleeAttack){return;}
        double winkel = Math.toDegrees(Math.atan2(Player.get().getCenterY() - getCenterY(), Player.get().getCenterX() - getCenterX()));
        winkel += (winkel < 0 ? 360: 0);
        if(winkel > 315 || winkel <= 45){
            setDirection(Right);
        }
        if(winkel > 45 && winkel <= 135){
            setDirection(Down);
        }
        if(winkel > 135 && winkel <= 225){
            setDirection(Left);
        }
        if(winkel > 225 && winkel <= 315){
            setDirection(Up);
        }
        setHuntDirection();
        if (Collision.canMoveX(huntX, levelhitbox)) {
            position.change(huntX, 0);
        }
        if (Collision.canMoveY(huntY, levelhitbox)) {
            position.change(0, huntY);
        }
    }

    protected void setHuntDirection(){
        huntConstant = huntingSpeed/distance;
        huntX = (Player.get().getCenterX() - getCenterX()) * huntConstant;
        huntY = (Player.get().getCenterY() - getCenterY()) * huntConstant;
    }

    protected void randomMovement(){
        if(hunting || state == MeleeAttack){return;}
        if (movingTick <= 0){
            if (moving){
                stopMovement();
                blocked.clear();
            }else{
                startMovement();
            }
        }else{
            if (moving){
                movement();
            }
            movingTick--;
        }
    }
    protected void movement(){

            double xDelta =  speed * direction.getX();
            double yDelta =  speed * direction.getY();

            if (Collision.canMoveX(xDelta, levelhitbox)) {
                position.change(xDelta, 0);
            }else if (xDelta != 0){
                stopMovement();
                blocked.add(direction);
            }
            if (Collision.canMoveY(yDelta, levelhitbox)) {
                position.change(0, yDelta);
            }else if (yDelta != 0){
                stopMovement();
                blocked.add(direction);
            }

    }

    protected void stopMovement(){
        movingTick = Random.get(movingIntervalMin * Game.UPS, movingIntervalMax * Game.UPS);
        moving = false;
        setState(Standing);
    }
    protected void startMovement(){
        movingTick = Random.get(movingDurationMin * Game.UPS, movingDurationMax * Game.UPS);
        moving = true;
        if (blocked.size() >= 4){
            blocked.clear();
        }
        do {
            switch (Random.get(1, 4)) {
                case 1 -> setDirection(Right);
                case 2 -> setDirection(Down);
                case 3 -> setDirection(Left);
                case 4 -> setDirection(Up);
            }
        }while (blocked.contains(direction));
        setState(Walking);
    }

    protected void removeCooldown(){
        if(attackCooldown > 0){
            attackCooldown--;
        }
    }

    protected abstract void attack();
    protected abstract void drop();
    protected abstract void playHuntSound();


    public void delete(){
        attackhitbox.reset();
        Render.remove(Render.Entity, this);
        Update.remove(Update.Game, this);
    }
}