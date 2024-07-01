package Main.UI.Inventory;

import Main.Item.Armor.Bottom;
import Main.Item.Armor.Chestplate;
import Main.Item.Armor.Helmet;
import Main.Item.Consumeable;
import Main.Item.Item;
import Main.Item.Weapon;
import Util.Coordinate;

import java.awt.*;

import static Main.UI.Inventory.Slot.scaledDimension;

public class PlayerInventory extends Inventory{
    private FilteredSlot weapon, helmet, chestplate, bottom;
    public PlayerInventory(int size) {
        super(size);
        helmet = new FilteredSlot(new Coordinate( - 9 * gap - scaledDimension,- (double) scaledDimension /2), this, Helmet.class);
        chestplate = new FilteredSlot(new Coordinate( - 9 * gap - scaledDimension,scaledDimension+2*gap-(double) scaledDimension /2), this, Chestplate.class);
        bottom = new FilteredSlot(new Coordinate( - 9 * gap - scaledDimension,(scaledDimension + 2*gap)*2-(double) scaledDimension /2), this, Bottom.class);
        weapon = new FilteredSlot(new Coordinate( - 9 * gap - scaledDimension,(scaledDimension + 4*gap)*3-(double) scaledDimension /2), this, Weapon.class);
        addBonusSlot(helmet, chestplate, bottom, weapon);
    }
    public Item getWeapon(){
        return weapon.getInventoryItem().getType();
    }
    public void render(Graphics g) {
        if (!isOpen() || isDoubleOpen()){return;}
        drawSlots(g);
        drawItems(g);
        drawMouse(g);
        drawTags(g);
    }

    public void drawMouse(Graphics g){
        getMouse().render(g);
    }

    public boolean containsFilteredSlot(Class<? extends Item> filter){
        for (Slot bonusslot : bonusslots) {
            FilteredSlot filteredSlot = (FilteredSlot) bonusslot;
            if (filteredSlot.getFilter().contains(filter)){
                return true;
            }
        }
        return false;
    }
    public FilteredSlot getFilteredSlot(Class<? extends Item> filter){
        for (Slot bonusslot : bonusslots) {
            FilteredSlot filteredSlot = (FilteredSlot) bonusslot;
            if (filteredSlot.getFilter().contains(filter)){
                return filteredSlot;
            }
        }
        return null;
    }

    public MouseSlot getMouse(){
        return mouse;
    }

    public boolean addAt(InventoryItem inventoryItem, int position) {
        if(position >= 0){
            for(int i = position; i < inventorySlots.size(); i++){
                if (inventorySlots.get(i).isEmpty()){
                    inventorySlots.get(i).add(inventoryItem);
                    return true;
                }
            }
        }else {
            for(int i = position * -1 - 1; i < bonusslots.size(); i++){
                if (bonusslots.get(i).isEmpty()){
                    bonusslots.get(i).add(inventoryItem);
                    return true;
                }
            }
        }
        return false;
    }
}
