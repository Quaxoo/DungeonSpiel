package Main.UI.Inventory;


import Input.Keyboard;
import Main.Item.Consumeable;
import Main.Item.Item;
import Util.*;

import java.awt.*;
import java.awt.event.KeyEvent;

import static States.Constants.globalScale;

public class Slot extends MouseReactable implements Drawable, Updateable {
    protected InventoryItem inventoryItem;

    protected Inventory inventory;
    protected static Sprite slotNormal;

    protected Sprite sprite;
    protected InventoryTag inventoryTag;

    public static final float scale = 3.5f * globalScale;

    public static final int scaledDimension = (int) (32 * scale);
    protected final KeyTypedListener listener = new KeyTypedListener(this::equip, KeyEvent.VK_E);

    Slot(Coordinate offset, Inventory inventory){
        super();
        sprite = new Sprite(slotNormal, offset);
        this.inventory = inventory;
        inventoryItem = new InventoryItem(Item.EMPTY);
    }
    Slot(Inventory inventory){
        super();
        this.inventory = inventory;
        inventoryItem = new InventoryItem(Item.EMPTY);
    }

    public void add(InventoryItem inventoryItem){
        this.inventoryItem = inventoryItem;
        inventoryItem.setSlot(this);
    }

    public void swap(Slot slot){
        InventoryItem item = this.inventoryItem;
        add(slot.inventoryItem);
        slot.add(item);
    }
    public void transfer(){
        if (inventory.getDoppelInventory().getOtherInventory(inventory).add(inventoryItem)){
            remove();
        }
    }
    public void remove(){
        inventoryItem = new InventoryItem(Item.EMPTY);
    }
    public void remove(int amount){
        inventoryItem.remove(amount);
        if (inventoryItem.getStack().getSize() < 1){
            remove();
        }
    }

    public boolean isEmpty(){
        return inventoryItem.sameType(Item.EMPTY);
    }

    @Override
    public void render(Graphics g) {
        sprite.drawFrom(inventory.getOrigin(), g);
    }
    public void drawItem(Graphics g) {
        if (isEmpty()){return;}
        inventoryItem.render(g);
    }
    public void drawTag(Graphics g) {
        if (inventoryTag == null){return;}
        inventoryTag.render(g);
    }

    public static void load(){
        slotNormal = new Sprite(Sprite.Slot, scaledDimension, scaledDimension);
    }

    @Override
    public void update() {
        checkClick();
        checkCollision();
        listener.check();
        if(inventoryTag != null){
            inventoryTag.update();
        }
        if (!isEmpty()){
            inventoryItem.update();
        }
    }

    @Override
    protected Rectangle getBounds() {
        return sprite.getBounds(inventory.getOrigin().getX(), inventory.getOrigin().getY());
    }

    @Override
    protected void doClick() {
        if (inventory.isDoubleOpen() && Keyboard.isKeyDown(KeyEvent.VK_SHIFT) && !isEmpty()){

            transfer();

        }else if(getInventoryItem().sameType(getMouse().getInventoryItem())){

            getMouse().remove(getInventoryItem().add(getMouse().getInventoryItem().getSize()));

        }else{

            swap(getMouse());

        }
        refreshTag();
    }

    @Override
    protected void doRightClick() {
        if(getMouse().isEmpty() && !isEmpty() && !downButNotMeRight && Keyboard.isKeyDown(KeyEvent.VK_SHIFT)){
            gather();
        }else if (getInventoryItem().sameType(getMouse().getInventoryItem())){

            getMouse().remove(getInventoryItem().add(1));

        }else  if(getMouse().isEmpty() && !isEmpty() && !downButNotMeRight){

            int amount = (int) Math.ceil(getInventoryItem().getSize()/2.0);
            getMouse().add(new InventoryItem(inventoryItem.getType()));
            getMouse().getInventoryItem().add(amount-1);
            remove(amount);

        } else if(isEmpty()){
            add(new InventoryItem(getMouse().getInventoryItem()));
            getMouse().remove(1);
        }
        refreshTag();
    }

    protected void gather(){
        getMouse().add(inventoryItem);
        inventory.gather(inventoryItem);
        if (inventory.isDoubleOpen()){
            inventory.getDoppelInventory().getOtherInventory(inventory).gather(inventoryItem);
        }
        remove();
    }

    @Override
    protected void doOnCollision() {
        refreshTag();
        getInventoryItem().refresh();
    }

    protected void refreshTag(){
        if(!isEmpty()) {
            inventoryTag = new InventoryTag(getInventoryItem().getType().getId(), sprite, 3, getInventoryItem().getType().getDescription(3));
        }else{
            inventoryTag = null;
        }
    }
    @Override
    protected void doOnLeftCollision() {
        inventoryTag = null;
    }

    protected void equip(){
        if(!isEmpty() && isColliding() && !inventory.isDoubleOpen()){
            if(inventoryItem.getType() instanceof Consumeable){
                ((Consumeable) inventoryItem.getType()).consume();
                remove(1);
                refreshTag();
            }else if(inventory.containsFilteredSlot(inventoryItem.getStack().getType().getClass())){
                swap(inventory.getFilteredSlot(inventoryItem.getStack().getType().getClass()));
                Sound.play(Sound.Equip);
            }
        }
    }

    protected MouseSlot getMouse(){
        return inventory.getMouse();
    }

    public Coordinate getCenter(){
        return new Coordinate(sprite.getCenterX() + inventory.getOrigin().getX(), sprite.getCenterY() + inventory.getOrigin().getY());
    }

    public InventoryItem getInventoryItem(){
        return inventoryItem;
    }
}
