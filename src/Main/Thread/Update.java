package Main.Thread;

import Main.GameFrame;
import Util.Loop;
import Util.Updateable;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static Main.Game.UPS;
import static Main.Game.status;
public class Update extends Loop {

    private static Update update;
    public static final int Always = 0;

    public static final int Game = 1;

    public static final int Menu = 2;
    private static final int numberOfLevels = 3;
    public static ArrayList<CopyOnWriteArrayList<Updateable>> updates = new ArrayList<>();

    private static final int[][] updateGroups = {
            {Always, Game},
            {Always, Menu}
    };

    public Update(){
        super(Update::update, UPS);
        update = this;
    }

    public static void update(){
        if(!GameFrame.get().isOpen() || Main.Game.paused()){
            return;
        }

        int[] group = updateGroups[status.getIndex()];
        for (int groupIndex: group){
            for(Updateable u: updates.get(groupIndex)){
                if(Main.Game.paused()){
                    return;
                }
                u.update();
            }
        }
    }
    public static void load(){
        for (int x = 0; x < numberOfLevels; x++){
            updates.add(new CopyOnWriteArrayList<>());
        }
    }
    private static void updateGroup(int typ){
        updates.get(typ).forEach(Updateable::update);
    }

    public static void add(int type,Updateable updateable){
        updates.get(type).add(updateable);
    }
    public static void remove(int type,Updateable updateable){
        updates.get(type).remove(updateable);
    }
    public static Update get(){
        return update;
    }

    public void restart(){
        restartLoop();
    }
}
