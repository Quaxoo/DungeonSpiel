package Main.UI.Inventory;

import Input.Keyboard;
import Main.Item.Equipment;
import Util.Coordinate;
import Util.Sound;

import java.awt.event.KeyEvent;

public class ResultSlot extends Slot{
    ResultSlot(Coordinate offset, Inventory inventory) {
        super(offset, inventory);
    }

    @Override
    protected void doClick() {
        if (Keyboard.isKeyDown(KeyEvent.VK_SHIFT) && !isEmpty()){
            Sound.play(Sound.Upgrade);
            transfer();
            refreshTag();
            inventory.removedResult();
            return;
        }
        if(!isEmpty() && inventory.getMouse().isEmpty()){
            Sound.play(Sound.Upgrade);
            swap(inventory.getMouse());
            inventory.removedResult();
            refreshTag();
        }
    }

    protected void refreshTag(){
        if(!isEmpty()) {
            inventoryTag = new InventoryTag(getItem().getId(), sprite, 3, getItem().getUpgradedDescription(3));
        }else{
            inventoryTag = null;
        }
    }

    public Equipment getItem(){
        return (Equipment) inventoryItem.getType();
    }

    @Override
    protected void doRightClick() {

    }

}
