package Main.Item;

import Main.Darkness;
import Main.Player;
import Util.Executable;
import Util.Sprite;

import java.util.ArrayList;
import java.util.HashMap;

import static States.Constants.globalScale;

public class Consumeable extends Item{

    protected ArrayList<Stat> stats = new ArrayList<>();
    private final int[] effects;
    private final HashMap<Integer, String> effectAmount = new HashMap<>();
    private final Executable executable;
    public static final Consumeable Healingpotion = new Consumeable("healingpotion",1.5f,
            () -> Player.get().heal(25), new String[]{"25"}, Stat.Health);
    public static final Consumeable NightvisionPotion = new Consumeable("nightvisionpotion",1.5f, () -> Darkness.get().changeSize(90, 0.25f), new String[]{"25%", "90S"},Stat.Vision, Stat.Time);
    public Consumeable(String id,float iconScale, Executable executable,
                       String[] amount, int... effects) {
        super(id,16, iconScale);
        this.effects = effects;
        int n = 0;
        for (int e: effects){
            effectAmount.put(e, amount[n]);
            n++;
        }
        this.executable = executable;
        setUpStats();
    }

    public void setUpStats(){
        for (int effect: effects){
            switch (effect){
                case Stat.Health -> {stats.add(new Stat(Stat.Health, this::getHealamountString));}
                case Stat.Vision -> {stats.add(new Stat(Stat.Vision, this::getVisionamountString));}
                case Stat.Time -> {stats.add(new Stat(Stat.Time, this::getDurationString));}
            }
        }
    }

    public Sprite getDescription(float scale) {
        Sprite stat = new Sprite(stats.get(0).getSprite(scale));
        for (int i = 1; i < stats.size(); i++){
            stat.addAtBottom(stats.get(i).getSprite(scale), 6 * globalScale);
        }
        return stat;
    }

    private int getEffect(){
        return 3;
    }

    private String getHealamountString(){
        return effectAmount.get(Stat.Health);
    }
    private String getVisionamountString(){
        return effectAmount.get(Stat.Vision);
    }
    private String getDurationString(){
        return effectAmount.get(Stat.Time);
    }

    public void consume(){
        executable.execute();
    }

    public static void load(){}
}