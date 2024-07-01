package Main;

import Main.Thread.Render;
import Main.Thread.Update;
import States.Direction;
import States.State;
import Util.*;
import org.json.simple.JSONArray;

import java.util.ArrayList;

import static States.Direction.Down;
import static States.State.MeleeAttack;
import static States.State.Standing;

public abstract class Entity implements DrawableEntity, Updateable {

    protected String id;
    protected Sprite skin;

    protected Coordinate position;
    protected Hitbox levelhitbox;

    public Hitbox hitbox;
    protected Attackhitbox attackhitbox;
    protected int angriffsCooldown;
    protected boolean hit;
    protected int health, maxhealth;
    protected int protectionTime;
    protected int protectionTick;

    protected Direction direction;

    protected State state;
    protected float speed;
    protected Sprite[][] animations;
    protected int[][] animationIndexes;
    protected int spriteDimension;

    protected float scale;

    protected int scaledDimension;
    protected int animationIndex, animationTick;
    protected ArrayList<KeyTypedListener> keyTypedListeners = new ArrayList<>();

    public Entity(Coordinate position, String id){
        this.position = position;
        this.id = id;
        setDirection(Down);
        setState(Standing);
        loadConstants();
        loadAnimations();
        loadAnimationIndexes();
    }


    protected void animate() {
        skin = animations[state.getIndex() + direction.getIndex()][animationIndexes[state.getIndex() + direction.getIndex()][animationIndex]];
    }

    protected void animationTick(){
        animationTick++;
        if (animationTick % getAnimationSpeed() == 0){
            animationIndex++;
            if(animationIndex >= getAnimationslength() - 1) {
                animationIndex = 0;
            }
        }
    }

    protected void updateHitboxes(){
        hitbox.update();
        levelhitbox.update();
    }

    protected int getHealth(){
        return health;
    }

    protected void resetAnimation(){
        animationTick = 0;
        animationIndex = 0;
    }

    protected void setState(State state){
        if (this.state != state){
            this.state = state;
            resetAnimation();
        }
    }
    protected void setDirection(Direction direction){
        if (this.direction != direction){
            this.direction = direction;
            resetAnimation();
        }
    }


    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public double getCenterX() {
        return getX()+(scaledDimension/2.0);
    }

    public double getCenterY() {
        return getY()+(scaledDimension/2.0);
    }
    public Direction getDirection(){return direction;}

    public void setCenterX(double x) {
        position.setX( x - (scaledDimension / 2.0) );
    }

    public void setCenterY(double y) {
        position.setY( y - (scaledDimension / 2.0) );
    }

    public Coordinate getCenter(){
        return new Coordinate(getCenterX(),getCenterY());
    }

    public void loadAnimationIndexes(){
        JSONArray json = Files.loadJSONArray("data/animation/" + id + ".json");
        for (int i = 0; i < animationIndexes.length; i++){
            JSONArray a = (JSONArray) json.get(i);
            int x = 0;
            for (Object value: a){
                animationIndexes[i][x] = (int)(long)value;
                x++;
            }
        }
    }

    public void loadAnimations() {

        Sprite sprites = new Sprite("assets/entities/" + id + ".png", animations[0].length * scaledDimension, animations.length * scaledDimension);

        for (int y = 0; y < animations.length; y++){
            for (int x = 0; x < animations[y].length; x++){
                animations[y][x] = new Sprite(sprites.getSubimage(x * scaledDimension, y * scaledDimension, scaledDimension, scaledDimension));
            }
        }
    }
    protected void addKeyTypedListener(KeyTypedListener keyTypedListener){
        keyTypedListeners.add(keyTypedListener);
    }
    protected void updateKeyTypedListeners(){
        for (KeyTypedListener k : keyTypedListeners){
            k.check();
        }
    }

    protected abstract int getAnimationslength();
    protected abstract int getAnimationSpeed();
    protected abstract void die();
    protected abstract void loadConstants();

    public void delete(){
        Render.remove(Render.Entity, this);
        Update.remove(Update.Game, this);
    }

    public Hitbox getHitbox(){
        return hitbox;
    }

}