package Main.Level;

import CustomFiles.Inventory.InventoryFileReader;
import Main.*;
import Main.Item.WorldItem;
import Main.Thread.Render;
import States.Constants;
import Util.*;

import java.awt.geom.Path2D;
import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

public class Level {

    public static Coordinate origin = new Coordinate();

    public static Coordinate player = new Coordinate();
    public static Coordinate spawn = new Coordinate();

    public static int level = 1;
    private static int unlockedLevel = level;
    private static final int maxlevel = 4;
    public static Sprite[] maps = new Sprite[maxlevel + 1];
    public static Path2D.Double[] levelhitboxes = new Path2D.Double[maxlevel + 1];
    public static final int spriteDimension = 32;
    public static int[] killObjectives = {0, 1, 2, 5, 8};

    public static final float scale = 4.5f * Constants.globalScale;

    public static final int scaledDimension = (int) (spriteDimension*scale);
    public static Sprite[][] tiles = new Sprite[5][14];
    public static CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<Interactable> interactables = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<NonCollidable> nonCollidables = new CopyOnWriteArrayList<>();
    public static int saveIds = 0;

    public static void reset(){
        origin.set(0,0);
        Game.stop();
        Player.get().closeInventory();
        clearSaves();
        resetPlayer();
        Player.get().loadInventory();
        WorldItem.deleteAll();
        load();
        moveToSpawn();
        Game.start();
    }

    public static void goUp(){
        if(level == maxlevel){return;}
        origin.set(0,0);
        Game.stop();
        save();
        level++;
        if(level > unlockedLevel){
           unlockedLevel = level;
        }
        saveLevel();
        clearSaves();
        resetPlayer();
        WorldItem.deleteAll();
        if(maps[level] == null){
            LevelReader.readMap();
        }
        load();
        moveToSpawn();
        Game.start();
    }

    public static void goDown(){
        Game.stop();
        origin.set(0,0);
        save();
        level--;
        saveLevel();
        clearSaves();
        resetPlayer();
        WorldItem.deleteAll();
        if(maps[level] == null){
            LevelReader.readMap();
        }
        load();
        moveToSpawn();
        Game.start();
    }

    public static void create(){
        loadLevel();
        LevelReader.readMap();
        load();
        moveToSpawn();
        Render.add(Render.Level, g -> {
            maps[level].draw(origin.getX(), origin.getY(),g);
        });
    }

    public static void move(double x, double y){
        origin.change(x,y);
    }

    public static Sprite getBitmap(){
        return new Sprite("assets/level/" + level + ".png");
    }

    public static void loadSprites(){
        Sprite sprite = new Sprite(Sprite.Tiles, tiles[0].length*scaledDimension,tiles.length * scaledDimension);
        for (int y = 0; y < tiles.length; y++){
            for (int x = 0; x < tiles[y].length; x++){
                tiles[y][x] = new Sprite(sprite.getSubimage( x* scaledDimension, y* scaledDimension, scaledDimension, scaledDimension));
            }
        }
    }

    public static void setSpawn(int x, int y){
        spawn.set((x + 0.5) * Level.scaledDimension, (y + 0.5) * Level.scaledDimension);
    }

    public static void moveToSpawn(){
        origin.set(0,0);
        move(-spawn.getX() + player.getX(),-spawn.getY() + player.getY());
    }

    public static Interactable getClosestInteractable(){
        if (interactables.size() == 0){
            return null;
        }
        interactables.sort(Comparator.comparingDouble(Interactable::getDistance));
        return interactables.get(0);
    }

    public static void addNonCollidable(NonCollidable nonCollidable){
        nonCollidables.add(nonCollidable);
    }
    public static void addInteractable(Interactable interactable){
        interactables.add(interactable);
    }
    public static void addEntity(Entity entity){
        entities.add(entity);
    }

    public static void removeNonCollidable(NonCollidable nonCollidable){
        nonCollidables.remove(nonCollidable);
    }

    public static void removeInteractable(Interactable interactable){
        interactables.remove(interactable);
    }
    public static void save(){
        try{
            if(!Files.existsDirectory(Files.getSavesFolder() + "/game")){
                java.nio.file.Files.createDirectory(Path.of(Files.getSavesFolder() + "/game"));
            }
            if(!Files.existsDirectory(Files.getSavesFolder() + "/game/levels")){
                java.nio.file.Files.createDirectory(Path.of(Files.getSavesFolder() + "/game/levels"));
            }
            if(Files.existsDirectory(String.valueOf(getPath()))){
                try {
                    java.nio.file.Files.walkFileTree(getPath(), new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            java.nio.file.Files.delete(file);
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                            java.nio.file.Files.delete(dir);
                            return FileVisitResult.CONTINUE;
                        }
                    });
                } catch (IOException e) {}
            }
            java.nio.file.Files.createDirectory(getPath());
            java.nio.file.Files.createDirectory(getChestPath());
            java.nio.file.Files.createDirectory(getDoorPath());
            java.nio.file.Files.createDirectory(getUpgradetablePath());
        }catch (Exception ignored){}
        for (Interactable i: interactables){
            i.save();
        }
        Player.get().saveInventory();
        saveLevel();
    }

    public static void load(){
        if(!Files.existsDirectory(getPath().toString())){
            if(level < unlockedLevel){
                LevelReader.readEntitiesFromFinishedLevel();
            }else{
                LevelReader.readEntities();
            }
            save();
        }else{
            if(level < unlockedLevel){
                LevelReader.resetFinishedLevel();
            }else{
                LevelReader.reset();
            }
            loading();
        }
    }

    private static void loading(){
        try {
            DirectoryStream<Path> stream = java.nio.file.Files.newDirectoryStream(getChestPath());
            BufferedReader reader = null;
            for (Path sub: stream){
                reader = new BufferedReader(new FileReader(sub + "/info.txt"));
                new Chest(reader.readLine(), reader.readLine(), new InventoryFileReader(sub + "/" + Chest.classId).read(), reader.readLine());
                reader.close();
            }
            stream.close();
            stream = java.nio.file.Files.newDirectoryStream(getUpgradetablePath());
            for (Path sub: stream){
                reader = new BufferedReader(new FileReader(sub + "/info.txt"));
                new UpgradeTable(reader.readLine(), reader.readLine(),new InventoryFileReader(sub + "/" + UpgradeTable.classId).read(), reader.readLine());
                reader.close();
            }
            stream.close();
            stream = java.nio.file.Files.newDirectoryStream(getDoorPath());
            for (Path sub: stream){
                reader = new BufferedReader(new FileReader(sub + "/info.txt"));
                new Door(reader.readLine(), reader.readLine(), reader.readLine().equals("up"), reader.readLine().equals("unlocked"), reader.readLine());
                reader.close();
            }
            if (reader != null){
                reader.close();
            }
            stream.close();
        }catch (Exception ignored){
            System.out.println(ignored.getMessage());
        }
    }

    private static Path getPath(){
        return Path.of(Files.getSavesFolder() + "/game/levels/" + level);
    }
    public static Path getChestPath(){
        return Path.of(getPath() + "/Chests");
    }
    public static Path getUpgradetablePath(){
        return Path.of(getPath() +  "/Upgradetables");
    }
    public static Path getDoorPath(){
        return Path.of(getPath() + "/Doors");
    }

    private static void clearSaves(){
        for (Entity e: entities){
            e.delete();
        }
        for(Interactable i: interactables){
            i.delete();
        }
        entities.clear();
        interactables.clear();
        nonCollidables.clear();
    }

    private static void resetPlayer(){
        Player.get().resetHealth();
        Player.get().resetKills();
        Player.get().removeKey();
    }

    private static void saveLevel(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Files.getSavesFolder() + "/game/level.txt"));
            writer.write(String.valueOf(level));
            writer.newLine();
            writer.write(String.valueOf(unlockedLevel));
            writer.close();
        }catch (Exception ignored){}
    }
    private static void loadLevel(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(Files.getSavesFolder() + "/game/level.txt"));
            level = Integer.parseInt(reader.readLine());
            unlockedLevel = Integer.parseInt(reader.readLine());
            reader.close();
        }catch (Exception ignored){}
    }
}
