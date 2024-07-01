package Main.Level;

import Util.Random;
import Util.Sprite;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;

import static Main.Level.Level.*;

public abstract class LevelReader {

    private static int x,y;
    private static Sprite bitmap;

    private static final int LeftWall = 0, TopWall = 1, TopPillarWall = 2, TopPortWall = 3, RightWall = 4, BottomWall = 5,
            InsideCornerWallTopLeft = 6, InsideCornerWallTopRight = 7, InsideCornerWallBottomLeft = 8, InsideCornerWallBottomRight = 9,
            OutsideCornerWallTopRight = 10, OutsideCornerWallTopLeft = 11, OutsideCornerWallBottomRight = 12, OutsideCornerWallBottomLeft = 13;

    public static void read() {
        bitmap = Level.getBitmap();

        Sprite map = new Sprite(bitmap.getWidth() * scaledDimension, bitmap.getHeight()*scaledDimension);
        if(levelhitboxes[level] == null){
            levelhitboxes[level] = new Path2D.Double();
        }

        for (y = 0; y < bitmap.getHeight(); y++){
            for (x = 0; x < bitmap.getWidth(); x++){

                Color rgb = new Color(bitmap.get().getRGB(x,y));

                LevelObject tile = LevelObject.getLevelObject(rgb, false);
                LevelObject entity = LevelObject.getLevelObject(rgb, true);

                entity.spawn(x, y, rgb.getGreen() - entity.getG(0));

                int variant = tile.getVariant() + entity.getVariant();

                if(tile.isWallObject()){
                    levelhitboxes[level].append(getHitbox(variant), false);
                    tiles[tile.getY()][variant].drawOn(map, x * scaledDimension,y * scaledDimension);
                    tiles[tile.getY() + 1][variant].drawOn(map, x * scaledDimension,y * scaledDimension);
                }else{
                    tiles[tile.getY()][variant].drawOn(map, x * scaledDimension,y * scaledDimension);
                }
            }
        }
        maps[level] = map;
    }
    public static void readMap() {
        bitmap = Level.getBitmap();

        Sprite map = new Sprite(bitmap.getWidth() * scaledDimension, bitmap.getHeight()*scaledDimension);
        if(levelhitboxes[level] == null){
            levelhitboxes[level] = new Path2D.Double();
        }

        for (y = 0; y < bitmap.getHeight(); y++){
            for (x = 0; x < bitmap.getWidth(); x++){

                Color rgb = new Color(bitmap.get().getRGB(x,y));

                LevelObject tile = LevelObject.getLevelObject(rgb, false);
                LevelObject entity = LevelObject.getLevelObject(rgb, true);

                int variant = tile.getVariant() + entity.getVariant();

                if(tile.isWallObject()){
                    levelhitboxes[level].append(getHitbox(variant), false);
                    tiles[tile.getY()][variant].drawOn(map, x * scaledDimension,y * scaledDimension);
                    tiles[tile.getY() + 1][variant].drawOn(map, x * scaledDimension,y * scaledDimension);
                }else{
                    tiles[tile.getY()][variant].drawOn(map, x * scaledDimension,y * scaledDimension);
                }
            }
        }
        maps[level] = map;
    }

    public static void readEntities() {

        bitmap = Level.getBitmap();

        for (y = 0; y < bitmap.getHeight(); y++){
            for (x = 0; x < bitmap.getWidth(); x++){

                Color rgb = new Color(bitmap.get().getRGB(x,y));
                LevelObject entity = LevelObject.getLevelObject(rgb, true);


                entity.spawn(x, y, rgb.getGreen() - entity.getG(0));

            }
        }
    }
    public static void readEntitiesFromFinishedLevel() {

        bitmap = Level.getBitmap();

        for (y = 0; y < bitmap.getHeight(); y++){
            for (x = 0; x < bitmap.getWidth(); x++){

                Color rgb = new Color(bitmap.get().getRGB(x,y));
                LevelObject entity = LevelObject.getLevelObject(rgb, true);

                if(!entity.isKey()) {
                    entity.spawn(x, y, !entity.isDoor() ? rgb.getGreen() - entity.getG(0) : rgb.getGreen() - entity.getG(0) + 1);
                }
            }
        }
    }
    public static void readEnemies() {

        bitmap = Level.getBitmap();

        for (y = 0; y < bitmap.getHeight(); y++){
            for (x = 0; x < bitmap.getWidth(); x++){

                Color rgb = new Color(bitmap.get().getRGB(x,y));
                LevelObject entity = LevelObject.getLevelObject(rgb, true);
                if(entity.isEnemy()){
                    entity.spawn(x, y, 0);
                }
            }
        }
    }

    public static void reset() {

        bitmap = Level.getBitmap();

        for (y = 0; y < bitmap.getHeight(); y++){
            for (x = 0; x < bitmap.getWidth(); x++){

                Color rgb = new Color(bitmap.get().getRGB(x,y));
                LevelObject entity = LevelObject.getLevelObject(rgb, true);
                if(entity.isEnemy() || entity.isKey() || entity.isSpawn()){
                    entity.spawn(x, y, 0);
                }
            }
        }
    }
    public static void resetFinishedLevel() {

        bitmap = Level.getBitmap();

        for (y = 0; y < bitmap.getHeight(); y++){
            for (x = 0; x < bitmap.getWidth(); x++){

                Color rgb = new Color(bitmap.get().getRGB(x,y));
                LevelObject entity = LevelObject.getLevelObject(rgb, true);
                if(entity.isEnemy() || entity.isSpawn()){
                    entity.spawn(x, y, 0);
                }
            }
        }
    }

    public static int randomiseFloortype() {
        int random = Random.get(0,100);
        if(random > 98){
            return 5;
        }else if (random > 96){
            return 4;
        }else if(random > 94){
            return 3;
        }else if(random > 80){
            return 2;
        }else if(random > 45){
            return 1;
        }else{
            return 0;
        }
    }

    public static int computeWallDirection(){


        boolean rightEdge = bitmap.getWidth() <= x + 1;
        boolean bottomEdge = bitmap.getHeight() <= y + 1;
        boolean leftEdge = 0 > x - 1;
        boolean topEdge = 0 > y - 1;

        boolean rightIsFloor = !rightEdge && LevelObject.isFloorObject(new Color(bitmap.get().getRGB(x + 1, y)));
        boolean bottomIsFloor = !bottomEdge && LevelObject.isFloorObject(new Color(bitmap.get().getRGB(x, y + 1)));
        boolean leftIsFloor = !leftEdge && LevelObject.isFloorObject(new Color(bitmap.get().getRGB(x - 1, y)));
        boolean topIsFloor = !topEdge && LevelObject.isFloorObject(new Color(bitmap.get().getRGB(x, y - 1)));

        boolean diagonalBottomRightIsFloor = !rightEdge && !bottomEdge && LevelObject.isFloorObject(new Color(bitmap.get().getRGB(x + 1, y + 1)));
        boolean diagonalBottomLeftIsFloor = !bottomEdge && !leftEdge && LevelObject.isFloorObject(new Color(bitmap.get().getRGB(x - 1, y + 1)));
        boolean diagonalTopLeftIsFloor = !leftEdge && !topEdge && LevelObject.isFloorObject(new Color(bitmap.get().getRGB(x - 1, y - 1)));
        boolean diagonalTopRightIsFloor = !topEdge && !rightEdge && LevelObject.isFloorObject(new Color(bitmap.get().getRGB(x + 1, y - 1)));


        //Corner Outside
        if (bottomIsFloor && leftIsFloor){
            return OutsideCornerWallTopRight;
        }
        if (rightIsFloor && bottomIsFloor){
            return OutsideCornerWallTopLeft;
        }
        if (leftIsFloor && topIsFloor){
            return OutsideCornerWallBottomRight;
        }
        if (rightIsFloor && topIsFloor){
            return OutsideCornerWallBottomLeft;
        }

       //Straight
        if (rightIsFloor){
            return LeftWall;
        }
        if (bottomIsFloor){
           if (Math.random() <= 0.4){
               return TopPillarWall;
           }else if(Math.random() <= 0.16){
               return TopPortWall;
           }else{
               return TopWall;
           }
        }
        if (leftIsFloor){
            return RightWall;
        }
        if (topIsFloor){
            return BottomWall;
        }

        //Corner Inside
        if (diagonalBottomRightIsFloor){
            return InsideCornerWallTopLeft;
        }
        if (diagonalBottomLeftIsFloor){
            return InsideCornerWallTopRight;
        }
        if (diagonalTopRightIsFloor){
            return InsideCornerWallBottomLeft;
        }
        if (diagonalTopLeftIsFloor){
            return InsideCornerWallBottomRight;
        }

        return LeftWall;
    }

    private static Shape getHitbox(int variant){
        double difX = - 5.2 * scale;
        double difY =  8 * scale;

        double arcWidth = 5 * scale;
        double arcHeight = 8 * scale;

        switch (variant){
            case LeftWall -> {return new Rectangle(x * scaledDimension,y * scaledDimension, (int) (scaledDimension - 13 * scale), scaledDimension);}
            case TopWall, TopPillarWall, TopPortWall -> {return new Rectangle(x * scaledDimension,y * scaledDimension, scaledDimension, (int) (scaledDimension - 20 * scale));}
            case RightWall -> {return new Rectangle((int) (x * scaledDimension + 13 * scale),y * scaledDimension, (int) (scaledDimension - 13 * scale), scaledDimension);}
            case BottomWall -> {return new Rectangle(x * scaledDimension, (int) (y * scaledDimension + 20 * scale), scaledDimension, (int) (scaledDimension - 20 * scale));}

            case InsideCornerWallTopLeft -> {
                Area a = new Area(new Rectangle(x * scaledDimension,y * scaledDimension, scaledDimension, scaledDimension));
                Area b = new Area(new Arc2D.Double((int) (x * scaledDimension + scaledDimension - (scaledDimension + difX)/2.0),y * scaledDimension + scaledDimension - (scaledDimension + difY)/2.0, (scaledDimension + difX), (int) (scaledDimension + difY),180,-90, Arc2D.PIE));
                a.subtract(b);
                return a;
            }
            case InsideCornerWallTopRight -> {
                Area a = new Area(new Rectangle(x * scaledDimension,y * scaledDimension, scaledDimension, scaledDimension));
                Area b = new Area(new Arc2D.Double((int) (x * scaledDimension - (scaledDimension + difX)/2.0),y * scaledDimension + scaledDimension - (scaledDimension + difY)/2.0, (scaledDimension + difX), (int) (scaledDimension + difY),0,90, Arc2D.PIE));
                a.subtract(b);
                return a;
            }
            case InsideCornerWallBottomLeft -> {
                Area a = new Area(new Rectangle(x * scaledDimension,y * scaledDimension, scaledDimension, scaledDimension));
                Area b = new Area(new Arc2D.Double((int) (x * scaledDimension + scaledDimension - (scaledDimension + difX)/2.0),y * scaledDimension  - (scaledDimension + difY)/2.0, (scaledDimension + difX), (int) (scaledDimension + difY),270,-90, Arc2D.PIE));
                a.subtract(b);
                return a;
            }
            case InsideCornerWallBottomRight -> {
                Area a = new Area(new Rectangle(x * scaledDimension,y * scaledDimension, scaledDimension, scaledDimension));
                Area b = new Area(new Arc2D.Double((int) (x * scaledDimension - (scaledDimension + difX)/2.0),y * scaledDimension  - (scaledDimension + difY)/2.0, (scaledDimension + difX), (int) (scaledDimension + difY),270,90, Arc2D.PIE));
                a.subtract(b);
                return a;
            }

            case OutsideCornerWallTopRight ->{return new Arc2D.Double((int) (x * scaledDimension + scaledDimension - (scaledDimension + arcWidth)/2.0),y * scaledDimension - (scaledDimension - arcHeight)/2.0, (scaledDimension + arcWidth), (int) (scaledDimension - arcHeight),180,90, Arc2D.PIE);}
            case OutsideCornerWallTopLeft ->{return new Arc2D.Double((int) (x * scaledDimension - (scaledDimension + arcWidth)/2.0),y * scaledDimension - (scaledDimension - arcHeight)/2.0, (scaledDimension + arcWidth), (int) (scaledDimension - arcHeight),0,-90, Arc2D.PIE);}
            case OutsideCornerWallBottomRight ->{return new Arc2D.Double((int) (x * scaledDimension + scaledDimension - (scaledDimension + arcWidth)/2.0),y * scaledDimension + scaledDimension - (scaledDimension - arcHeight)/2.0, (scaledDimension + arcWidth), (int) (scaledDimension - arcHeight),180,-90, Arc2D.PIE);}
            case OutsideCornerWallBottomLeft ->{return new Arc2D.Double((int) (x * scaledDimension - (scaledDimension + arcWidth)/2.0),y * scaledDimension + scaledDimension - (scaledDimension - arcHeight)/2.0, (scaledDimension + arcWidth), (int) (scaledDimension - arcHeight),0,90, Arc2D.PIE);}
        }
        return new Rectangle(x * scaledDimension,y * scaledDimension, scaledDimension, scaledDimension);
    }

}