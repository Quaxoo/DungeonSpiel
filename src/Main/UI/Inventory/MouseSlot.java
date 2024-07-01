package Main.UI.Inventory;

import Input.Mouse;
import Util.Coordinate;

import java.awt.*;

public class MouseSlot extends Slot{
    private Slot previous;
    MouseSlot(Inventory inventory) {
        super(inventory);
    }
    public void add(InventoryItem inventoryItem){
        previous = inventoryItem.getSlot();
        this.inventoryItem = inventoryItem;
        inventoryItem.setSlot(this);
    }
    @Override
    public void render(Graphics g) {
        drawItem(g);
    }
    @Override
    public void update(){
        if (isEmpty()){return;}
        inventoryItem.update();
    }

    public void returnItem(){
        if(previous.isEmpty()){
            previous.add(getInventoryItem());
        }else {
            previous.inventory.add(getInventoryItem());
        }
        remove();
    }

    public Coordinate getCenter(){
        return Mouse.get();
    }


}
