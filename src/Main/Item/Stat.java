package Main.Item;

import Util.GetableString;
import Util.Sprite;
import Util.Text;

import java.util.ArrayList;

import static States.Constants.globalScale;

public class Stat {
    private Sprite valueSprite;
    private final GetableString statValue;
    private final int stat;
    public static final int Damage = 0, Crit = 1, Attackspeed = 2, Health = 3,Vision = 4, Time = 5, Armor = 6;
    private static final ArrayList<Sprite> icons = new ArrayList<>();
    private static final double gap = globalScale;
    public Stat(int stat, GetableString statValue){
        this.stat = stat;
        this.statValue = statValue;
    }

    public String get(){
        String value = statValue.get();
        if (value.length() >= 3 && value.charAt(value.length() - 2) == '.' && value.charAt(value.length() - 1) == '0'){
            value = value.replace(".0", "");
        }
        return value;
    }

    public Sprite getSprite(float scale){
        valueSprite = Text.get(String.valueOf(get()), (scale - 1f) * globalScale);
        Sprite s = new Sprite(icons.get(stat), (scale - 0.5f) * globalScale);
        s.addAtRightCenter(valueSprite, 2 * gap);
        s.trim();
        return s;
    }
    public Sprite getSpriteMarked(float scale){
        valueSprite = Text.getMarked(String.valueOf(get()), (scale - 1f) * globalScale);
        Sprite s = new Sprite(icons.get(stat), (scale - 0.5f) * globalScale);
        s.addAtRightCenter(valueSprite, 2 * gap);
        s.trim();
        return s;
    }

    public static void load(){
        icons.add(new Sprite(Sprite.Damage));
        icons.add(new Sprite(Sprite.Crit));
        icons.add(new Sprite(Sprite.Speed));
        icons.add(new Sprite(Sprite.Health));
        icons.add(new Sprite(Sprite.Vision));
        icons.add(new Sprite(Sprite.Time));
    }
}