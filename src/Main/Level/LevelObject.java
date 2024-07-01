package Main.Level;


import Main.Door;
import Main.Goblin;
import Main.Item.Key;
import Main.UpgradeTable;
import Util.Number;
import Util.Spawnable;

import java.awt.*;
import java.util.ArrayList;

public class LevelObject {
    //Tiles
    private static final ArrayList<LevelObject> levelObjects = new ArrayList<>();
    public static final LevelObject Void = new LevelObject(0,0, 0, 0, 0);
    public static final LevelObject Floor = new LevelObject(1,255,-1,-1, LevelReader::randomiseFloortype);
    public static final LevelObject Wall = new LevelObject(2,1,-1,-1, LevelReader::computeWallDirection);
    public static final LevelObject Relict = new LevelObject(4,254,-1,-1, 0);

    //Entities
    public static final LevelObject Null = new LevelObject(420,420,420, (x,y, ignored) -> {}, 0);
    public static final LevelObject Spawn = new LevelObject(-1,1,-1, (x,y, ignored) -> Level.setSpawn(x,y), 0);
    public static final LevelObject Goblin = new LevelObject(-1,2,-1, (x,y, ignored) -> new Goblin(x,y), 0);
    public static final LevelObject Chest = new LevelObject(-1, new int[]{99, 149},-1, Main.Chest::new, 1);
    public static final LevelObject Upgradetable = new LevelObject(-1, 154,-1, (x,y, ignored) -> new UpgradeTable(x,y), 2);
    public static final LevelObject Door = new LevelObject(-1,new int[]{150, 152},-1, (x, y, state) -> new Door(x, y, state <= 1, state >= 1), 0);
    public static final LevelObject Key = new LevelObject(-1,254,-1, (x, y, ignored) -> new Key(x,y), 0);
    private final int[] r,g,b;
    private final boolean isEntity;
    private int y;
    private Spawnable spawnable;
    private final Number variant;

    LevelObject(int y, int r, int g, int b, Number variant) {
        this.y = y;
        this.r = new int[]{r,r};
        this.g = new int[]{g,g};
        this.b = new int[]{b,b};
        this.isEntity = false;
        this.variant = variant;
        LevelObject.levelObjects.add(this);
    }
    LevelObject(int y, int r, int g, int b, int variant) {
        this.y = y;
        this.r = new int[]{r,r};
        this.g = new int[]{g,g};
        this.b = new int[]{b,b};
        this.isEntity = false;
        this.variant = () -> variant;
        LevelObject.levelObjects.add(this);
    }
    LevelObject(int r, int g, int b, Spawnable spawnable, int variant) {
        this.r = new int[]{r,r};
        this.g = new int[]{g,g};
        this.b = new int[]{b,b};
        this.isEntity = true;
        this.spawnable = spawnable;
        this.variant = () -> variant;
        LevelObject.levelObjects.add(this);
    }
    LevelObject(int r, int[] g, int b, Spawnable spawnable, int variant) {
        this.r = new int[]{r,r};
        this.g = g;
        this.b = new int[]{b,b};
        this.isEntity = true;
        this.spawnable = spawnable;
        this.variant = () -> variant;
        LevelObject.levelObjects.add(this);
    }

    public static LevelObject getLevelObject(int r, int g, int b, boolean entity){
        for(LevelObject lo: levelObjects){
            if (lo.isRGB(r,g,b) && lo.isEntity == entity){
                return lo;
            }
        }
        return entity ? Null : Floor;
    }
    public static LevelObject getLevelObject(Color rgb, boolean entity){
        return getLevelObject(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), entity);
    }
    public boolean isRGB(int r, int g, int b){
        return isR(r) && isG(g) && isB(b);
    }

    public int getR(int i) {
        return r[i];
    }
    public boolean isR(int r){
        return (getR(0) <= r && r <= getR(1)) || getR(0) == -1;
    }
    public int getG(int i) {
        return g[i];
    }
    public boolean isG(int g){
        return (getG(0) <= g && g <= getG(1)) || getG(0) == -1;
    }

    public int getB(int i) {
        return b[i];
    }
    public boolean isB(int b){
        return (getB(0) <= b && b <= getB(1)) || getB(0) == -1;
    }

    public void spawn(int x, int y, int z){
        if(isEntity && this != Null) {
            spawnable.spawn(x,y,z);
        }
    }
    public int getVariant(){
        return variant.get();
    }
    public int getY(){
        return y;
    }

    public static boolean isFloorObject(Color rgb){
        return getLevelObject(rgb, false).isFloorObject();
    }

    public boolean isFloorObject(){
        return this == Floor || this == Relict;
    }
    public boolean isWallObject(){
        return this == Wall;
    }

    public boolean isVoid(){
        return this == Void;
    }
    public boolean isEnemy(){
        return this == Goblin;
    }
    public boolean isKey(){
        return this == Key;
    }
    public boolean isSpawn(){
        return this == Spawn;
    }
    public boolean isDoor(){
        return this == Door;
    }

}
