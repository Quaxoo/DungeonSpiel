package Main.Thread;

import Main.Game;
import Main.GameFrame;
import Main.GamePanal;
import States.Settings;
import Util.Drawable;
import Util.DrawableEntity;
import Util.Loop;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

import static Main.Game.status;


public class Render extends Loop {

    public static Render render;

    public static final int Level = 0;

    public static final int Entity = 1;
    public static final int LevelLayerUp = 2;
    public static final int Effect = 3;
    public static final int Darkness = 4;

    public static final int UI = 5;

    public static final int Inventory = 6;

    public static final int Menu = 7;
    private static final int numberOfLevels = 8;
    public static ArrayList<CopyOnWriteArrayList<Drawable>> levels = new ArrayList<>(numberOfLevels);
    public static CopyOnWriteArrayList<DrawableEntity> entities = new CopyOnWriteArrayList<>();
    private static final int[][] renderGroups = {
            {Level, Entity, LevelLayerUp, Effect, Darkness, UI,  Inventory},
            {Level, Entity, LevelLayerUp, Effect, Darkness, UI, Menu}
    };

    public Render(){
        super(Render::render, Settings.FPS.get());
        render = this;
    }
    public static void render(){
        if(!GameFrame.get().isOpen() || Game.paused()){
            return;
        }
        GamePanal.get().repaint();
    }
    public static void renderLevels(Graphics g){
        int[] group = renderGroups[status.getIndex()];
        sort();
        for (int groupIndex: group){
            for(Drawable d: groupIndex != Entity ? levels.get(groupIndex) : entities){
                if(Game.paused()){
                    return;
                }
                d.render(g);
            }
        }
    }
    public static void load(){
        for (int x = 0; x < numberOfLevels; x++){
            levels.add(new CopyOnWriteArrayList<>());
        }
    }
    public static void sort(){
        entities.sort(Comparator.comparingDouble(DrawableEntity::getZ));
    }
    public static void add(int level, Drawable drawable){
        levels.get(level).add(drawable);
    }
    public static void add(int level, DrawableEntity drawableEntity){
        entities.add(drawableEntity);
    }
    public static void remove(int level, Drawable drawable){
        levels.get(level).remove(drawable);
    }
    public static void remove(int level, DrawableEntity drawableEntity){
        entities.remove(drawableEntity);
    }
    public static Render get(){
        return render;
    }
    public void refreshFPS(){
        setPS(Settings.FPS.get());
    }
    public void restart(){
        restartLoop();
    }
}
