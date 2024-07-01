package Main;

import Main.Item.Item;
import Main.Item.WorldItem;
import Main.Level.Level;
import Main.Thread.Render;
import Main.Thread.Update;
import States.Constants;
import Util.Coordinate;
import Util.Files;
import Util.Sound;
import Util.Sprite;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;

import static States.State.*;

public class Goblin extends Enemy {
    private static final String classId = "goblin";
    public Goblin(int x, int y) {
        super(new Coordinate((x + 0.5) * Level.scaledDimension, (y + 0.5) * Level.scaledDimension), classId);

        setCenterX(position.getX());
        setCenterY(position.getY());

        levelhitbox = new Hitbox(this,0,1,2,22);
        hitbox = new Hitbox(this,0,0,10,10);
        attackhitbox = new Attackhitbox(this);

        Render.add(Render.Entity,this);
        Update.add(Update.Game,this);
    }

    @Override
    protected void die() {
        setState(Dying);
        attackhitbox.set(0,0,0);
        Player.get().addKill();
    }
    protected void drop(){
        for(int i = 0; i < probability.size(); i++){
            if(Math.random() < probability.get(i)){
                new WorldItem(loot.get(i), getCenter());
                return;
            }
        }
    }

    @Override
    protected void playHuntSound() {
        Sound.play(Sound.GoblinHunt);
    }

    @Override
    protected void loadConstants(){
        animations = new Sprite[20][17];
        animationIndexes = new int[20][20];
        spriteDimension = 32;
        scale = 4.25f * Constants.globalScale;
        scaledDimension = (int)(spriteDimension*scale);
        speed = 0.15f * scale;
        huntingSpeed = 0.3f * scale * (float) (Math.random() * 0.6 + 0.5);
        protectionTime = 1;
        maxhealth = 35 + 5 * Level.level;
        health = maxhealth;
        movingDurationMin = 1;
        movingDurationMax = 5;
        movingIntervalMin = 2;
        movingIntervalMax = 9;
        huntingStart = 75*scale;
        huntingEnd = 110*scale;
        attackDistance = 17*scale;
        angriffsCooldown = 400;
        damage = 13 + 2 * Level.level;
        stopMovement();
        animate();
    }
    public static void load(){
        JSONArray json = Files.loadJSONArray("data/loot/" + classId + ".json");

        for (Object obj : json) {
            JSONObject jsonObj = (JSONObject) obj;

            float prob = ((Number) jsonObj.get("probability")).floatValue();
            String item = (String) jsonObj.get("item");

            probability.add(prob);
            loot.add(Item.getItem(item));
        }


        for (int i = 1; i < probability.size(); i++){
            float p = probability.get(i);
            for(int n = 0; n < i; n++){
                p /= 1 - probability.get(n);
            }
            probability.set(i, p);
        }

    }

    @Override
    public void update() {
        if (state != Dying && !Player.get().isDying()) {
            updateHitboxes();
            tryHunt();
            randomMovement();
            checkHit();
            removeCooldown();
            checkPlayerHit();
        }else if (Player.get().isDying() && state != Standing){
            setState(Standing);
        }
        animationTick();
        animate();
        skin.setPosition(getX(), getY());
    }


    @Override
    public void render(Graphics g) {
        skin.draw(g);
    }
    protected void attack(){
        setState(MeleeAttack);
        attackCooldown = angriffsCooldown;
    }

    protected void checkPlayerHit(){
        if (state == MeleeAttack) {
            attackhitbox.set(-25 * direction.getX(), - 25 * direction.getY(),130 + Math.abs(55* direction.getX()),130 + Math.abs(55* direction.getY()));
            attackhitbox.setSize(100);
            attackhitbox.update(animationIndex, getAnimationslength());
        }

        if (attackhitbox.intersects(Player.get().hitbox)){
            if(!Player.get().isHit()) {
                Player.get().takeDamage(damage);
            }
        }else{
            Player.get().setHit(false);
        }
    }

    protected int getAnimationslength(){
        switch (state){
            case Standing, Walking -> { return 8; }
            case MeleeAttack -> { return 10; }
            case Dying -> { return 20; }
            default -> { return 0; }
        }
    }
    protected int getAnimationSpeed(){
        switch (state){
            case Dying -> { return 14; }
            case MeleeAttack -> {return 22;}
            default -> { return 28; }
        }
    }

    protected void animationTick(){
        animationTick++;
        if (animationTick % getAnimationSpeed() == 0){
            animationIndex++;
            if(animationIndex >= getAnimationslength() - 1) {
                animationIndex = 0;
                if (state == MeleeAttack) {
                    setState(Standing);
                    attackhitbox.set(0,0,0);
                }
                if (state == Dying) {
                    drop();
                    delete();
                }
            }
        }
    }

    public double getX() {
        return position.getX() + Level.origin.getX();
    }

    public double getY() {
        return position.getY() + Level.origin.getY();
    }
    @Override
    public double getZ() {
        return getCenterY() + 12*scale;
    }

}