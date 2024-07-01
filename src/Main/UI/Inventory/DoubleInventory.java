package Main.UI.Inventory;
import Main.GameFrame;
import Util.Coordinate;

import java.awt.*;

import static Main.UI.Inventory.Slot.scale;

public class DoubleInventory extends Inventory{
    private final PlayerInventory playerInventory;
    private Inventory inventory;
    private final static float gap = 10 * scale;
    private double height;

    public DoubleInventory(PlayerInventory playerInventory, Inventory inventory){
        super();
        this.playerInventory = playerInventory;
        this.inventory = inventory;
        height = playerInventory.getHeight() + inventory.getHeight() + gap;
        origin = new Coordinate(GameFrame.get().getCenterX() - inventory.getWidth() /2, GameFrame.get().getCenterY() - height /2);
        inventory.setOriginY(origin.getY());
    }
    public DoubleInventory(PlayerInventory playerInventory){
        super();
        this.playerInventory = playerInventory;
    }

    public void setInventory(Inventory inventory){
        this.inventory = inventory;
        height = playerInventory.getHeight() + inventory.getHeight() + gap;
        origin = new Coordinate(GameFrame.get().getCenterX() - inventory.getWidth()/2.0, GameFrame.get().getCenterY() - height /2.0);
        inventory.setOriginY(origin.getY());
    }

    @Override
    public void render(Graphics g) {
        if (!isOpen()){return;}
        playerInventory.drawSlots(g);
        inventory.drawSlots(g);
        playerInventory.drawItems(g);
        inventory.drawItems(g);
        playerInventory.drawMouse(g);
        playerInventory.drawTags(g);
        inventory.drawTags(g);
    }

    public MouseSlot getMouse(){
        return playerInventory.getMouse();
    }

    @Override
    public void update() {
        if (!isOpen()){return;}
        playerInventory.update();
        inventory.update();
        mouse.update();
    }

    public void setOpen(boolean open){
        this.open = open;
        if (!open && !mouse.isEmpty()){
            mouse.returnItem();
        }
        playerInventory.setDoubleInventoryOpen(open,this);
        inventory.setDoubleInventoryOpen(open,this);
        if (open){
            playerInventory.setOriginY(origin.getY()+inventory.getHeight()+gap);
        }else {
            playerInventory.setOriginY(GameFrame.get().getCenterY() - playerInventory.getHeight() /2);
        }
    }

    public Inventory getOtherInventory(Inventory inventory){
        if (inventory instanceof PlayerInventory){
            return this.inventory;
        }else {
            return playerInventory;
        }
    }
}
