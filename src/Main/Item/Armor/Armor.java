package Main.Item.Armor;

import Main.Item.Equipment;
import Util.Sprite;

public class Armor extends Equipment {
    public Armor(String id) {
        super(id, 1);
    }

    @Override
    public Sprite getUpgradedDescription(float scale) {
        return null;
    }
}
