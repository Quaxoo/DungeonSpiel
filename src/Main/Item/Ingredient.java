package Main.Item;

public class Ingredient extends Item{

    public static Ingredient Artifact = new Ingredient("artifact",4.5f,64, 4, 4, 0.15f, 0.1f);
    public static Ingredient Gem = new Ingredient("gem",4.5f,64, 2,0, 0, 0);
    public static Ingredient Coins = new Ingredient("coins",4.5f,64, 0, 0, 0.1f, 0);
    public static Ingredient Scrap = new Ingredient("scrap",4.5f,64, 0, 0, 0, 0);

    private final int damageBonus, armorBonus;
    private final float critfactorBonus, apsBonus;

    private Ingredient(String id, float iconScale, int stacksize, int damageBonus,
                       int armorBonus, float critfactorBonus, float apsBonus) {
        super(id, stacksize, iconScale);
        this.damageBonus = damageBonus;
        this.armorBonus = armorBonus;
        this.critfactorBonus = critfactorBonus;
        this.apsBonus = apsBonus;
    }

    public int getDamageBonus() {
        return damageBonus;
    }

    public int getArmorBonus() {
        return armorBonus;
    }

    public float getCritfactorBonus() {
        return critfactorBonus;
    }

    public float getApsBonus() {
        return apsBonus;
    }

    public static void load(){}
}
