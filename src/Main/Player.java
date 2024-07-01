package Main;

import CustomFiles.Inventory.InventoryFile;
import CustomFiles.Inventory.InventoryFileReader;
import CustomFiles.Inventory.InventoryFileWriter;
import CustomFiles.Inventory.InventoryItemObject;
import Input.Keyboard;
import Input.Mouse;
import Main.Item.Item;
import Main.Item.Weapon;
import Main.Item.WorldItem;
import Main.Level.Collision;
import Main.Level.Level;
import Main.Thread.Render;
import Main.Thread.Update;
import Main.UI.EquipedWeapon;
import Main.UI.Inventory.DoubleInventory;
import Main.UI.Inventory.InventoryItem;
import Main.UI.Inventory.PlayerInventory;
import States.Constants;
import States.Settings;
import States.State;
import Util.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.nio.file.Path;

import static Main.Item.Weapon.EMPTY;
import static States.Direction.*;
import static States.State.*;


public class Player extends Entity {

    private static Player player;
    private Weapon weapon = EMPTY;
    private PlayerInventory inventory;
    private DoubleInventory doubleInventory;
    private boolean reversed;
    private boolean hasKey = false;
    private int kills;

    public Player(Coordinate position) {
        super(position, "player");

        setCenterX(position.getX());
        setCenterY(position.getY());
        Level.player.set(getCenterX(), getCenterY());

        levelhitbox = new Hitbox(this, 0, 2, 2, 17);
        hitbox = new Hitbox(this, 0, 2, 6, 17);
        attackhitbox = new Attackhitbox(this);
        hit = false;

        setState(Spawning);
        setDirection(Down);

        player = this;
        loadInventory();
        doubleInventory = new DoubleInventory(inventory);

        Render.add(Render.Entity, this);
        Update.add(Update.Game, this);
    }

    @Override
    protected void die() {
        setState(Dying);
    }

    @Override
    public double getZ() {
        return getCenterY() + 15 * scale;
    }

    @Override
    protected void loadConstants(){
        animations = new Sprite[24][39];
        animationIndexes = new int[24][46];
        spriteDimension = 64;
        scale = 4.2f * Constants.globalScale;
        scaledDimension = (int)(spriteDimension * scale);
        speed = 0.4f * scale;
        maxhealth = 100;
        health = maxhealth;
        kills = 0;
        addKeyTypedListener(new KeyTypedListener(this::opencloseInventory,Settings.INVENTORY.get()));
        addKeyTypedListener(new KeyTypedListener(this::interact,KeyEvent.VK_E));
        addKeyTypedListener(new KeyTypedListener(this::closeInventory,KeyEvent.VK_ESCAPE));
    }

    public static Player get(){
        return player;
    }
    public boolean hasKey(){
        return hasKey;
    }
    public void update(){
        if(!isDying() && !isSpawning()){
            movement();
            calculateDirection();
            updateKeyTypedListeners();
            setWeapon();
            weapon.update();
            attack();
        }
        animationTick();
        animate();
        protectionTick--;
    }

    private void movement(){

        if (isAttacking() || isInventoryOpen()) {
            return;
        }

        double moveX = 0, moveY = 0;

        if(Keyboard.isKeyDown(Settings.UP.get())){
            moveY -= speed;
        }
       if(Keyboard.isKeyDown(Settings.LEFT.get())){
            moveX -= speed;
        }
        if(Keyboard.isKeyDown(Settings.DOWN.get())){
            moveY += speed;
        }
        if(Keyboard.isKeyDown(Settings.RIGHT.get())){
            moveX += speed;
        }

        if (moveX != 0 && moveY != 0){
            double konstante = speed/Math.sqrt(Math.pow(moveX,2)+Math.pow(moveY,2));
            moveX *= konstante;
            moveY *= konstante;
        }

        if (Collision.canMoveX(moveX, levelhitbox)) {
            Level.move(-moveX, 0);
        }
        if (Collision.canMoveY(moveY, levelhitbox)) {
            Level.move(0, -moveY);
        }

        setState((moveX != 0 || moveY != 0) ? Walking : Standing);

        reverseAnimation(( (moveX/speed) != direction.getX() && moveX != 0 && direction.getX() != 0)||( (moveY/speed) != direction.getY() && moveY != 0  && direction.getY() != 0));
    }

    private void opencloseInventory(){
        if (!isDoubleInventoryOpen()){
            inventory.setOpen(!inventory.isOpen());
        }else {
            interact();
        }
    }
    public void closeInventory(){
        if (isInventoryOpen() || isDoubleInventoryOpen()){
            opencloseInventory();
        }
    }
    private void interact(){
        if (isInventoryOpen() && !isDoubleInventoryOpen()){
            return;
        }

        Interactable i = Level.getClosestInteractable();

        if(i == null || !i.isAccessable()){
            return;
        }

        if (!isDoubleInventoryOpen() && i.hasInventory()){
            doubleInventory.setInventory(i.getInventory());
        }
        if (i.hasInventory()){
            doubleInventory.setOpen(!doubleInventory.isOpen());
        }

        i.interact();
    }

    public void addKill(){
        kills++;
    }
    public void resetKills(){
        kills = 0;
    }
    public int getKills(){
        return kills;
    }

    public boolean isInventoryOpen(){
        return inventory.isOpen();
    }

    public boolean isDoubleInventoryOpen(){
        return doubleInventory != null && doubleInventory.isOpen();
    }

    private void attack(){
        if(isInventoryOpen()){return;}

        if (state == MeleeAttack) {
            attackhitbox.set(0,0, weapon.getRadius());
            attackhitbox.setSize(weapon.getHitareaLength());
            attackhitbox.update(animationIndex, getAnimationslength());
        }

        if (Mouse.isMouseDown(Settings.ATTACK.get()) && weapon.getCooldown() <= 0 && state != MeleeAttack){
            meleeAttack();
        }
    }


    private void meleeAttack(){
        setState(MeleeAttack);
        weapon.setCooldown();
    }

    private void calculateDirection(){
        if(isInventoryOpen() || isAttacking()){return;}
        double angle = getAngleToCursor();

        if(angle > 315 || angle <= 45){
            setDirection(Right);
        }
        if(angle > 45 && angle <= 135){
            setDirection(Down);
        }
        if(angle > 135 && angle <= 225){
            setDirection(Left);
        }
        if(angle > 225 && angle <= 315){
            setDirection(Up);
        }
    }

    private double getAngleToCursor(){

        double gegen = Mouse.get().getX() - getCenterX();
        double an = Mouse.get().getY() - getCenterY();

        double winkel = Math.toDegrees(Math.atan2(an,gegen));

        return winkel + (winkel < 0 ? 360: 0);
    }

    @Override
    public void render(Graphics g) {
            skin.draw(getX(),getY(),g);
            weapon.draw(direction.getIndex(),animationIndex,getX(),getY(),g);
    }

    protected int getAnimationslength(){
        switch (state){
            case Standing -> { return 23; }
            case Walking -> { return 8; }
            case MeleeAttack -> { return 10; }
            case Dying -> { return 46; }
            case Spawning -> {return 25;}
            default -> { return 0; }
        }
    }
    protected int getAnimationSpeed(){
        switch (state){
            case MeleeAttack -> { return weapon.getAnimationspeed(); }
            case Dying -> { return 10; }
            case Spawning -> { return 8; }
            default -> { return 16; }
        }
    }

    public Weapon getWeapon(){
        return weapon;
    }

    private void reverseAnimation(boolean reversed){
        if(this.reversed != reversed) {
            this.reversed = reversed;
            resetAnimation();
        }
    }
    protected void resetAnimation(){
        animationTick = 0;
        animationIndex = isReversed() ? getAnimationslength()-1 : 0;
    }

    protected boolean isReversed(){
        return reversed && state != MeleeAttack && state != Dying && state != Spawning;
    }

    protected void animationTick(){
        animationTick++;
        if (animationTick % getAnimationSpeed() == 0){

            if(!isReversed()) {
                animationIndex++;
                if (animationIndex >= getAnimationslength() - 1) {
                  animationIndex = 0;
                    if (isAttacking()) {
                        setState(Standing);
                        attackhitbox.reset();
                    }else
                    if (isDying()) {
                        setState(Spawning);
                        attackhitbox.reset();
                        Level.reset();
                        resetAnimation();
                    }else
                    if (isSpawning()) {
                        setState(Standing);
                        calculateDirection();
                    }
                }
            }else {
                animationIndex--;
                if (animationIndex < 0) {
                    animationIndex = getAnimationslength() - 1;
                    if (isAttacking()) {
                        setState(Standing);
                        attackhitbox.reset();
                    }else
                    if (isDying()) {
                        setState(Spawning);
                        attackhitbox.reset();
                        Level.reset();
                        resetAnimation();
                    }else
                    if (isSpawning()) {
                        setState(Standing);
                        calculateDirection();
                    }
                }
            }

        }
    }

    private void setWeapon(){
        if(isInventoryOpen()) {
            Item item = inventory.getWeapon();
            weapon = item != Item.EMPTY ? (Weapon) item : EMPTY;
        }
    }
    public void collect(WorldItem item){
        inventory.add(new InventoryItem(item.getItem()));
    }

    public void collectKey(){
        hasKey = true;
    }
    public void removeKey(){
        hasKey = false;
    }
    public boolean isDying(){
        return state == Dying;
    }
    public boolean isSpawning(){
        return state == Spawning;
    }
    public boolean isAttacking(){
        return state == MeleeAttack;
    }

    protected void takeDamage(float damage){
        setHit(true);
        health -= damage;
        new Damagenumber(getCenterX() - Level.origin.getX(), position.getY() - Level.origin.getY() + 10*scale,damage, Damagenumber.Player);
        protectionTick = 300;
        if(health <= 0){
            die();
        }
    }

    protected boolean isHit(){
        return !(protectionTick <= 0);
    }
    protected void setHit(boolean hit){
        this.hit = hit;
    }
    public float getScale(){
        return scale;
    }
    public State getStatus(){
        return state;
    }
    public int getHealth() {
        return Math.max(health, 0);
    }
    public int getMaxHealth() {
        return maxhealth;
    }

    public boolean canCollect(Item type) {
        return !inventory.isFull() || inventory.contains(type);
    }

    public void heal(int amount) {
        health = Math.min(health + amount, maxhealth);
    }

    public void resetHealth() {
        health = maxhealth;
    }

    public void saveInventory(){
        if(!Files.existsDirectory(Files.getSavesFolder() + "/game")){
            try {
                java.nio.file.Files.createDirectory(Path.of(Files.getSavesFolder() + "/game"));
            }catch (Exception ignored){}
        }
        InventoryFileWriter writer = new InventoryFileWriter(Files.getSavesFolder() + "/game/" + id);
        writer.write(inventory);
    }
    public void loadInventory(){
        InventoryFileReader reader  = new InventoryFileReader(Files.getSavesFolder()+ "/game/" + id);
        InventoryFile file = reader.read();
        if(file != null){
            inventory = new PlayerInventory(file.getSize());
            for (InventoryItemObject i: file.getItems()){
                inventory.addAt(i.getItem(), i.getPosition(), i.getAmount());
            }
        }else{
            inventory = new PlayerInventory(21);
            saveInventory();
        }
        Item item = inventory.getWeapon();
        weapon = item != Item.EMPTY ? (Weapon) item : EMPTY;
        EquipedWeapon.refresh();
        doubleInventory = new DoubleInventory(inventory);
    }
}
