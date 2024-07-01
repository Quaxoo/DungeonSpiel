package Main.Item;

import Main.Item.Armor.Armor;
import Util.Sprite;

import java.util.ArrayList;

public class Equipment extends Item{

    public static Equipment EMPTY = new Equipment("empty", 1);
    protected ArrayList<Stat> stats = new ArrayList<>();
    protected ArrayList<Stat> upgradedStats = new ArrayList<>();

    public Equipment(String id, float iconScale) {
        super(id, 1, iconScale);
    }

    public static Equipment get(Item toolItem){
        if (toolItem instanceof Weapon){
            return new Weapon((Weapon) toolItem);
        }
        if (toolItem instanceof Armor){
            return null;
        }
        return Equipment.EMPTY;
    }

    public Sprite getUpgradedDescription(float scale){return null;}
}