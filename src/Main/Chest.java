package Main;

import CustomFiles.Inventory.InventoryFile;
import CustomFiles.Inventory.InventoryFileWriter;
import CustomFiles.Inventory.InventoryItemObject;
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


public class Chest extends Entity  implements Interactable, NonCollidable {

    public static final String classId = "chest";
    private Inventory inventory;
    private static final String basic = "data/loot/0";
    private int state = 0;
    private static final int Normal = 0, Accessable = 1, Opening = 2, Open = 3, Vanishing = 4, Closing = 5;
    private final int x,y;
    private boolean open;
    private int saveId;

    public Chest(int x, int y, int loot) {
        super(new Coordinate((x + 0.5) * Level.scaledDimension, (y + 0.4) * Level.scaledDimension), classId);

        this.x = x;
        this.y = y;

        setCenterX(position.getX());
        setCenterY(position.getY());

        addLoot(loot);
        set(Accessable);

        hitbox = new Hitbox(this,0,-3*scale,14*scale,scale);

        Render.add(Render.Entity,this);
        Update.add(Update.Game, this);

        saveId = Level.saveIds;
        Level.saveIds++;

        Level.addInteractable(this);
        Level.addNonCollidable(this);
    }
    public Chest(String x, String y, InventoryFile file, String id){
        super(new Coordinate((Integer.parseInt(x) + 0.5) * Level.scaledDimension, (Integer.parseInt(y) + 0.4) * Level.scaledDimension), "chest");

        this.x = Integer.parseInt(x);
        this.y = Integer.parseInt(y);
        saveId = Integer.parseInt(id);

        setCenterX(position.getX());
        setCenterY(position.getY());

        addLoot(file);
        set(Accessable);

        hitbox = new Hitbox(this,0,-3*scale,14*scale,scale);

        Render.add(Render.Entity,this);
        Update.add(Update.Game, this);

        Level.addInteractable(this);
        Level.addNonCollidable(this);
    }
    @Override
    protected void loadConstants() {
        animations = new Sprite[5][17];
        animationIndexes = new int[5][17];
        spriteDimension = 64;
        scale = 3.25f * Constants.globalScale;
        scaledDimension = (int)(spriteDimension*scale);
        inventory = new Inventory(14);
    }

    @Override
    public void update() {
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

    @Override
    public void render(Graphics g) {
        skin.draw(getX(),getY(),g);
    }

    private void addLoot(int loot){
        String location = "data/loot/" + loot;
        InventoryFile inv = Files.loadInventory(location);
        inv = inv == null ? Files.loadInventory(basic) : inv;
        for (InventoryItemObject item: inv.getItems()){
            inventory.addAt(item.getItem(), item.getPosition(), item.getAmount());
        }
    }
    private void addLoot(InventoryFile file){
        if (file != null) {
            for (InventoryItemObject item : file.getItems()) {
                inventory.addAt(item.getItem(), item.getPosition(), item.getAmount());
            }
        }
    }

    protected void animate() {
        skin = animations[state != Closing ? state : Opening][animationIndexes[state != Closing ? state : Opening][animationIndex]];
    }

    protected void animationTick(){
        animationTick++;
        if (animationTick % getAnimationSpeed() == 0){
            if(state == Closing){
                animationIndex--;
                if(animationIndex <= 0) {
                    animationIndex = 0;
                    set(Normal);
                }
            }
            else{
            animationIndex++;
            if(animationIndex >= getAnimationslength() - 1) {
                animationIndex = 0;
                if (state == Opening) {
                    set(Open);
                }
                if (state == Vanishing) {
                    delete();
                }
            }
            }
        }
    }

    @Override
    public void interact() {
        if (!open){
            Sound.play(Sound.ChestOpening);
            set(Opening);
        }else{
            if (!inventory.isEmpty()) {
                Sound.play(Sound.ChestClosing);
                set(Closing);
                animationIndex = getAnimationslength() - 1;
            }else{
                die();
            }
        }
        open = !open;
    }

    @Override
    protected void die() {
        set(Vanishing);
    }

    @Override
    public double getZ() {
        return getCenterY() + 15*scale;
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
            case Vanishing -> { return 17; }
            default -> { return 14; }
        }
    }

    @Override
    protected int getAnimationSpeed() {
        return 10;
    }

    public double getDistance(){
        return getCenter().getDistance(Player.get().getCenter());
    }

    public boolean isAccessable(){
        if(state == Vanishing){return false;}
        return getDistance() <= 125 * Constants.globalScale;
    }

    @Override
    public boolean hasInventory() {
        return true;
    }

    public Inventory getInventory(){
        return inventory;
    }

    private void set(int state){
        this.state = state;
    }

    @Override
    public Hitbox getHitbox() {
        return hitbox;
    }

    public void delete(){
        Render.remove(Render.Entity, this);
        Update.remove(Update.Game, this);
        Level.removeInteractable(this);
        Level.removeNonCollidable(this);
    }

    public void save(){
        String path = Level.getChestPath() + "/" + saveId;
        try {
            java.nio.file.Files.createDirectory(Path.of(path));
            File position = new File(path + "/info.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(position));
            writer.write(String.valueOf(x));
            writer.newLine();
            writer.write(String.valueOf(y));
            writer.newLine();
            writer.write(String.valueOf(saveId));
            writer.close();
            new InventoryFileWriter(path + "/" + id).write(inventory);
        }catch (Exception ignored){}
    }
}
