package Main.UI.Inventory;

import Main.Item.Item;
import Main.Item.Itemstack;
import States.Constants;
import Util.Drawable;
import Util.Sprite;
import Util.Updateable;

import java.awt.*;

public class InventoryItem implements Drawable, Updateable {

    private Itemstack itemstack;
    private Sprite icon, size;
    private Slot slot;
    public static final float sizescale = 3f * Constants.globalScale;

    public InventoryItem(Item item){
        this.itemstack = new Itemstack(item);
        icon = new Sprite(itemstack.getIcon());
        size = new Sprite(itemstack.getSizeSprite(), sizescale);
    }

    public InventoryItem(InventoryItem item){
        this.itemstack = new Itemstack(item.getType());
        icon = new Sprite(itemstack.getIcon());
        size = new Sprite(itemstack.getSizeSprite(), sizescale);
    }

    public InventoryItem(Item item, int amount){
        this.itemstack = new Itemstack(item, amount);
        icon = new Sprite(itemstack.getIcon());
        size = new Sprite(itemstack.getSizeSprite(), sizescale);
    }

    public void setSlot(Slot slot){
        this.slot = slot;
    }
    public Slot getSlot(){
        return slot;
    }
    @Override
    public void render(Graphics g) {
        icon.drawCenter(slot.getCenter().getX(), slot.getCenter().getY(), g);
        if (getSize() > 1){
            size.draw(slot.getCenter().getX() + Slot.scaledDimension / 2.0 - size.getWidth() - 3 * Slot.scale, slot.getCenter().getY() + Slot.scaledDimension / 2.0 - size.getHeight() - 3 * Slot.scale,g);
        }
    }

    public Itemstack getStack(){
        return itemstack;
    }
    public Item getType(){
        return itemstack.getType();
    }

    @Override
    public void update() {
        refresh();
    }

    public void refresh(){
        icon = new Sprite(itemstack.getIcon());
        size = new Sprite(itemstack.getSizeSprite(), sizescale);
    }

    public int add(int amount){
        int a = itemstack.canAdd(amount) ? amount : itemstack.getMaxSize() - itemstack.getSize();
        itemstack.add(a);
        return a;
    }
    public void remove(int amount){
        itemstack.remove(amount);
    }
    protected boolean sameType(InventoryItem type){
        return itemstack.equals(type.getStack());
    }
    protected boolean sameType(Item type){
        return getType().equals(type);
    }
    protected boolean canAdd(int amount){
        return itemstack.canAdd(amount);
    }
    protected int getSize(){
        return itemstack.getSize();
    }
    protected int getMaxSize(){
        return itemstack.getMaxSize();
    }

}
