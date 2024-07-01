package Main.UI.Inventory;

import Input.Keyboard;
import Main.Item.Item;
import Main.Item.Weapon;
import Util.Coordinate;
import Util.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class FilteredSlot extends Slot{
    private ArrayList<Class<? extends Item>> filter = new ArrayList<>();
    private Coordinate offset = new Coordinate();
    private static Sprite Weaponslot;
    FilteredSlot(Coordinate offset, Inventory inventory, Class<? extends Item>... filter) {
        super(offset, inventory);
        this.filter.addAll(Arrays.asList(filter));
        this.offset.set(offset);
        setSprite();
    }
    public static void load(){
        Weaponslot = new Sprite(Sprite.WeaponSlot, scaledDimension,scaledDimension);
    }
    public ArrayList<Class<? extends Item>> getFilter(){
        return filter;
    }
    public void add(InventoryItem inventoryItem){
        this.inventoryItem = inventoryItem;
        inventoryItem.setSlot(this);
        setSprite();
    }

    @Override
    public void render(Graphics g) {
        sprite.drawFrom(inventory.getOrigin(), g);
        drawItem(g);
    }

    @Override
    protected void doClick() {
        if (inventory.isDoubleOpen() && Keyboard.isKeyDown(KeyEvent.VK_SHIFT) && !isEmpty()){
            transfer();
            setSprite();
            return;
        }
        if(isInstance(inventory.getMouse().getInventoryItem().getType()) || inventory.getMouse().isEmpty()){
            swap(inventory.getMouse());
            setSprite();
        }
        refreshTag();
    }
    protected boolean canAdd(Item item){
        return isInstance(item) && isEmpty();
    }

    @Override
    protected void doRightClick() {
        if(getMouse().isEmpty() && !isEmpty() && !downButNotMeRight && Keyboard.isKeyDown(KeyEvent.VK_SHIFT)){

            gather();

        }else if (canAdd(getMouse().getInventoryItem().getType()) && getInventoryItem().sameType(getMouse().getInventoryItem())){

            getMouse().remove(getInventoryItem().add(1));

        }else  if(getMouse().isEmpty() && !isEmpty() && !downButNotMeRight){

            int amount = (int) Math.ceil(getInventoryItem().getSize()/2.0);
            getMouse().add(new InventoryItem(inventoryItem.getType()));
            getMouse().getInventoryItem().add(amount-1);
            remove(amount);

        } else if(isEmpty() && canAdd(getMouse().getInventoryItem().getType())){
            add(new InventoryItem(getMouse().getInventoryItem()));
            getMouse().remove(1);
        }
        refreshTag();
    }

    private void setSprite(){
        if(isEmpty()){
            if (filter.size() == 1 && filter.contains(Weapon.class)) {
                sprite = new Sprite(Weaponslot, offset);
            }else{
                sprite = new Sprite(slotNormal, offset);
            }
        }else{
            sprite = new Sprite(slotNormal, offset);
        }
    }

    public boolean isInstance(Item item){
        for (Class<? extends Item> f: filter){
            if (f.isInstance(item)){
                return true;
            }
        }
        return false;
    }

    protected void equip(){}
}
