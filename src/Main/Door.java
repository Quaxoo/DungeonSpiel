package Main;

import Main.Level.Level;
import Main.Thread.Render;
import Main.Thread.Update;
import Main.UI.Inventory.Inventory;
import States.Constants;
import Util.*;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;

public class Door extends Entity implements Interactable, NonCollidable {
    private int state = 0;
    private static final int Normal = 0, Accessable = 1, Opening = 2, Open = 3;
    private boolean up;
    private Sprite objective;
    private float alpha = 1f;
    private int tick;
    private final int x,y;
    private int saveId;

    public Door(int x, int y, boolean up, boolean unlocked) {
        super(new Coordinate((x + 0.5) * Level.scaledDimension, (y + 0.5) * Level.scaledDimension), "door");

        this.x = x;
        this.y = y;
        this.up = up;

        setCenterX(position.getX());
        setCenterY(position.getY());

        set(unlocked ? Open : Normal);

        hitbox = new Hitbox(this,0,-1.5*scale,3 * scale, 3 * scale);

        saveId = Level.saveIds;
        Level.saveIds++;

        Render.add(Render.Entity,this);
        Update.add(Update.Game, this);

        animate();
        Level.addInteractable(this);
    }

    public Door(String x, String y, boolean up, boolean unlocked, String id) {
        super(new Coordinate((Integer.parseInt(x) + 0.5) * Level.scaledDimension, (Integer.parseInt(y) + 0.5) * Level.scaledDimension), "door");

        this.x = Integer.parseInt(x);
        this.y = Integer.parseInt(y);
        this.up = up;
        saveId = Integer.parseInt(id);

        setCenterX(position.getX());
        setCenterY(position.getY());

        set(unlocked ? Open : Normal);

        hitbox = new Hitbox(this,0,-1.5*scale,3 * scale, 3 * scale);

        Render.add(Render.Entity,this);
        Update.add(Update.Game, this);

        animate();
        Level.addInteractable(this);
    }


    @Override
    public void interact() {
        if (!Player.get().hasKey()){
            objective = Text.getTranslated(Text.KeyMissingText, scale - 2.5f * Constants.globalScale);
            objective.setCenterX(getCenterX());
            objective.setPositionY(getY());
            alpha = 1;
            tick = 0;
            return;
        }
        if (Level.killObjectives[Level.level] > Player.get().getKills()){
            objective = Text.getTranslated(Text.KillsMissing, String.valueOf(Level.killObjectives[Level.level] - Player.get().getKills()), scale - 2.5f * Constants.globalScale);
            objective.setCenterX(getCenterX());
            objective.setPositionY(getY());
            alpha = 1;
            tick = 0;
            return;
        }
        set(Opening);
        Player.get().removeKey();
    }


    @Override
    protected void loadConstants() {
        animations = new Sprite[4][19];
        animationIndexes = new int[4][19];
        spriteDimension = 32;
        scale = 5.5f * Constants.globalScale;
        scaledDimension = (int)(spriteDimension * scale);
    }

    @Override
    public void update() {
        if(state == Open){
            checkCollision();
            checkDistance();
            if (isInRange()){
                showLevelTransfer();
            }else if(objective != null){
                objective = null;
            }
            hitbox.update();
            return;
        }
        if(objective != null){
            objective.moveY( - Constants.globalScale/5.0);
            if(tick > Game.UPS/1.5){
                objective.alpha(alpha);
                if (alpha >= 0.1){
                    alpha -= 0.0013;
                }else {
                    objective = null;
                }
            }
            tick++;
        }
        checkDistance();
        animationTick();
        animate();
        hitbox.update();
    }

    private void checkDistance() {
        if (state == Normal && isAccessable()){
            set(Accessable);
        }
        if(state == Accessable && !isAccessable()){
            set(Normal);
        }
    }

    private void showLevelTransfer(){
        objective = Text.getTranslated(Text.EnteringLevel, String.valueOf((Level.level + (up ? 1 : -1))), scale - 2.5f * Constants.globalScale);
        objective.setCenterX(getCenterX());
        objective.setPositionY(getY() - 1.25 * objective.getHeight());
        alpha = 1;
    }

    private void checkCollision(){
        if(hitbox.intersects(Player.get().levelhitbox)){
            if(up){
                Level.goUp();
            }else {
                Level.goDown();
            }
        }
    }

    @Override
    public void render(Graphics g) {
        skin.draw(getX(),getY(),g);
        if(objective != null){
            objective.draw(g);
        }
    }

    protected void animate() {
        skin = animations[state][animationIndexes[state][animationIndex]];
    }

    protected void animationTick(){
        animationTick++;
        if (animationTick % getAnimationSpeed() == 0){
                animationIndex++;
                if(animationIndex >= getAnimationslength() - 1) {
                    animationIndex = 0;
                    if (state == Opening) {
                        set(Open);
                    }
                }
        }
    }

    @Override
    public double getZ() {
        return getCenterY() - 15 * scale;
    }
    public double getX() {
        return position.getX() + Level.origin.getX();
    }

    public double getY() {
        return position.getY() + Level.origin.getY();
    }

    @Override
    protected int getAnimationslength() {
        switch (state){
            case Normal, Accessable, Open -> { return 1; }
            default -> { return 14; }
        }
    }

    @Override
    protected int getAnimationSpeed() {
        switch (state){
            case Normal, Accessable, Open -> { return 999; }
            default -> { return 22; }
        }
    }

    @Override
    protected void die() {
    }

    public double getDistance(){
        return getCenter().getDistance(Player.get().getCenter());
    }

    public boolean isAccessable(){
        if(state == Open){
            return false;
        }
        return getDistance() <= 125 * Constants.globalScale;
    }
    public boolean isInRange(){
        return getDistance() <= 125 * Constants.globalScale;
    }

    @Override
    public boolean hasInventory() {
        return false;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    @Override
    public void save() {
        String path = Level.getDoorPath() + "/" + saveId;
        try {
            java.nio.file.Files.createDirectory(Path.of(path));
            File position = new File(path + "/info.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(position));
            writer.write(String.valueOf(x));
            writer.newLine();
            writer.write(String.valueOf(y));
            writer.newLine();
            writer.write(up ? "up" : "down");
            writer.newLine();
            writer.write(state == Open ? "unlocked" : "locked");
            writer.newLine();
            writer.write(String.valueOf(saveId));
            writer.close();
        }catch (Exception ignored){}
    }

    private void set(int state){
        this.state = state;
    }

    public void delete(){
        Level.removeInteractable(this);
        Level.removeNonCollidable(this);
        Render.remove(Render.Entity, this);
        Update.remove(Update.Game, this);
    }
}
