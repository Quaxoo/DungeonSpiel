package Main.Item;

import Main.Game;
import Main.Player;
import States.State;
import Util.Files;
import Util.Sprite;
import org.json.simple.JSONArray;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static States.Constants.globalScale;

public class Weapon extends Equipment {
    public static final Weapon EMPTY = new Weapon("empty",1, 5, 1f, 0.0f, 1.5f,120,100, 7);
    public static final Weapon Dagger = new Weapon("dagger",3.5f,15, 1.4f, 0.2f, 1.5f,135,120, 9);
    public static final Weapon Sword = new Weapon("sword",5.25f,19, 1.45f, 0.3f, 1.1f,155,120, 11);
    public static final Weapon Destroyer = new Weapon("destroyer",3.25f,22, 2.0f, 0.18f, 0.9f,160,120, 13);
    public static final Weapon Wand = new Weapon("wand",3f,9, 7.0f, 0.1f, 4f,150,120, 9);
    private int damage;
    private float critFactor;
    private final float critChance;
    private final float APS;
    private final double radius, hitareaLength;
    private final float attackspeed;
    private final int animationspeed;
    private int[][] index = new int[4][10];
    private Sprite[][] sprites;
    private float cooldown;
    private Weapon(String name, float iconScale, int damage, float critFactor,
                   float critChance, float APS, double radius, double hitareaLength,
                   int animationspeed) {
        super(name, iconScale);
        this.damage = damage;
        this.critFactor = critFactor;
        this.critChance = critChance;
        this.attackspeed = Game.UPS/(APS*1);
        this.APS = APS;
        this.radius = radius;
        this.hitareaLength = hitareaLength;
        this.animationspeed = animationspeed;
        if (!id.equals("empty")){
            loadSprites();
        }
        reset();
        setUpStats();
    }

    public Weapon(Weapon type){
        super(type.id, type.iconScale);
        this.damage = type.damage;
        this.critFactor = type.critFactor;
        this.critChance = type.critChance;
        this.attackspeed = Game.UPS/(type.APS*1);
        this.APS = type.APS;
        this.radius = type.radius;
        this.hitareaLength = type.hitareaLength;
        this.animationspeed = type.animationspeed;
        if (!id.equals("empty")){
            loadSprites();
        }
        reset();
        setUpStats();
    }
    public Weapon(Weapon type, int damage, float critFactor, float APS){
        super(type.id, type.iconScale);
        this.damage = damage;
        this.critFactor = critFactor;
        this.critChance = type.critChance;
        this.attackspeed = Game.UPS/(APS*1);
        this.APS = APS;
        this.radius = type.radius;
        this.hitareaLength = type.hitareaLength;
        this.animationspeed = type.animationspeed;
        if (!id.equals("empty")){
            loadSprites();
        }
        reset();
        setUpStats();
    }

    public static void load(){
    }

    public void setUpStats(){
        stats.add(new Stat(Stat.Damage, this::getDamageString));
        stats.add(new Stat(Stat.Crit, this::getCritdamageString));
        stats.add(new Stat(Stat.Attackspeed, this::getAPSString));
    }

    public void update() {
        if(getCooldown() > 0) {
            cooldown--;
        }
    }

    public Sprite getDescription(float scale) {
        Sprite stat = new Sprite(stats.get(0).getSprite(scale));
        for (int i = 1; i < stats.size(); i++){
            stat.addAtBottomCenter(stats.get(i).getSprite(scale), 2 * globalScale);
        }
        return stat;
    }
    public Sprite getUpgradedDescription(float scale) {
        Sprite stat = new Sprite(upgradedStats.contains(stats.get(0)) ? stats.get(0).getSpriteMarked(scale) : stats.get(0).getSprite(scale));
        for (int i = 1; i < stats.size(); i++){
            stat.addAtBottomCenter(upgradedStats.contains(stats.get(i)) ? stats.get(i).getSpriteMarked(scale) : stats.get(i).getSprite(scale), 2 * globalScale);
        }
        return stat;
    }

    public void reset() {
        cooldown = 0;
    }

    public float getCooldown() {
        return cooldown;
    }

    public void setCooldown() {
        cooldown = attackspeed;
    }

    public int getDamage() {
        return damage;
    }
    public String getDamageString() {
        return String.valueOf(damage);
    }

    public float getCritFactor() {
        return critFactor;
    }

    public float getCritChance() {
        return critChance;
    }

    public double getAttackspeed() {
        return attackspeed;
    }

    public float getAPS() {
        String s = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US)).format(APS);
        return Float.parseFloat(s);
    }
    public String getAPSString(){
        return String.valueOf(getAPS());
    }

    public int getCritdamage(){
        return (int)(damage*critFactor);
    }
    public String getCritdamageString(){
        return String.valueOf(getCritdamage());
    }

    private void loadSprites(){
        JSONArray json = Files.loadJSONArray("data/animation/" + id + ".json");
        for (int i = 0; i < index.length; i++){
            JSONArray a = (JSONArray) json.get(i);
            int x = 0;
            for (Object value: a){
                index[i][x] = (int)(long)value;
                x++;
            }
        }
        float scale = 4.2f * globalScale;
        float scaledDimension = scale * 64;
        Sprite attackImage = new Sprite("assets/items/weapon/" + id + ".png");
        attackImage.scale(scale);
        int length = (int) (attackImage.getWidth() / scaledDimension);
        sprites = new Sprite[4][length];
        for (int y = 0; y < sprites.length; y++){
            for (int x = 0; x < sprites[y].length; x++){
                sprites[y][x] = new Sprite(attackImage.getSubimage(x * scaledDimension, y * scaledDimension, scaledDimension, scaledDimension));
            }
        }
    }

    public void draw(int direction, int img, double x, double y, Graphics g) {
        if (Player.get().getStatus() != State.MeleeAttack || Player.get().getWeapon() == EMPTY){return;}
        sprites[direction][index[direction][img]].draw(x, y, g);
    }

    public double getRadius(){
        return radius;
    }
    public double getHitareaLength(){
        return hitareaLength;
    }

    public int getAnimationspeed() {
        return animationspeed;
    }

    public void setDamage(int damage){
        this.damage = damage;
    }

    public void upgrade(Ingredient ingredient){
        upgradedStats.clear();

        if (damage != damage + ingredient.getDamageBonus()){
            upgradedStats.add(stats.get(0));
            upgradedStats.add(stats.get(1));
        }
        damage += ingredient.getDamageBonus();

        if (critFactor != critFactor + ingredient.getCritfactorBonus()){
            upgradedStats.add(stats.get(1));
        }
        critFactor += ingredient.getCritfactorBonus();
    }

}