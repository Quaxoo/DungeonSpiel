package Main;

import CustomFiles.Inventory.InventoryFile;
import CustomFiles.Inventory.InventoryFileWriter;
import CustomFiles.Inventory.InventoryItemObject;
import Main.Level.Level;
import Main.Thread.Render;
import Main.Thread.Update;
import Main.UI.Inventory.UpgradetableInventory;
import States.Constants;
import Util.Coordinate;
import Util.Interactable;
import Util.NonCollidable;
import Util.Sprite;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;

public class UpgradeTable extends Entity implements Interactable, NonCollidable {
    public static final String classId = "upgradetable";
    private UpgradetableInventory inventory;
    private int state = 0;
    private static final int Normal = 0, Accessable = 1;
    private final int x,y;
    private int saveId;

    public UpgradeTable(int x, int y) {
        super(new Coordinate((x + 0.5) * Level.scaledDimension, (y + 0.4) * Level.scaledDimension), classId);
        this.x = x;
        this.y =y;
        setCenterX(position.getX());
        setCenterY(position.getY());
        setState(Normal);
        hitbox = new Hitbox(this,0,-0.8*scale,4.5*scale, 1.5*scale);

        saveId = Level.saveIds;
        Level.saveIds++;

        Render.add(Render.Entity,this);
        Update.add(Update.Game, this);
        Level.addInteractable(this);
        Level.addNonCollidable(this);
    }
    public UpgradeTable(String x, String y, InventoryFile file, String id) {
        super(new Coordinate((Integer.parseInt(x) + 0.5) * Level.scaledDimension, (Integer.parseInt(y) + 0.4) * Level.scaledDimension), "upgradetable");

        this.x = Integer.parseInt(x);
        this.y = Integer.parseInt(y);
        saveId = Integer.parseInt(id);

        setCenterX(position.getX());
        setCenterY(position.getY());

        setState(Normal);
        hitbox = new Hitbox(this,0,-0.8*scale,4.5*scale, 1.5*scale);

        addLoot(file);

        Render.add(Render.Entity,this);
        Update.add(Update.Game, this);

        Level.addInteractable(this);
        Level.addNonCollidable(this);
    }
    @Override
    protected void loadConstants() {
        animations = new Sprite[2][1];
        animationIndexes = new int[2][1];
        spriteDimension = 32;
        scale = 4.5f * Constants.globalScale;
        scaledDimension = (int)(spriteDimension*scale);
        inventory = new UpgradetableInventory();
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
            setState(Accessable);
        }
        if(state == Accessable && !isAccessable()){
            setState(Normal);
        }
    }

    protected void animate() {
        skin = animations[state][animationIndexes[state][animationIndex]];
    }

    @Override
    public void render(Graphics g) {
        skin.draw(getX(),getY(),g);
    }

    @Override
    protected void die() {}

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
        return 1;
    }

    @Override
    protected int getAnimationSpeed() {
        return 100000;
    }


    @Override
    public void interact() {

    }

    public double getDistance(){
        return getCenter().getDistance(Player.get().getCenter());
    }


    public boolean isAccessable(){
        return getDistance() <= 125 * Constants.globalScale;
    }

    @Override
    public boolean hasInventory() {
        return true;
    }

    public UpgradetableInventory getInventory(){
        return inventory;
    }

    private void setState(int state){
        this.state = state;
    }

    public void delete(){
        Level.removeInteractable(this);
        Level.removeNonCollidable(this);
        Render.remove(Render.Entity, this);
        Update.remove(Update.Game, this);
    }

    private void addLoot(InventoryFile file){
        for (InventoryItemObject item: file.getItems()){
            inventory.addAt(item.getItem(), item.getPosition(), item.getAmount());
        }
    }

    public void save(){
        String path = Level.getUpgradetablePath() + "/" + saveId;
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
