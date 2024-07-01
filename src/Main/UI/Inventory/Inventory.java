package Main.UI.Inventory;

import CustomFiles.Inventory.InventoryFileWriter;
import Main.GameFrame;
import Main.Item.Item;
import Main.Item.Itemstack;
import Main.Thread.Render;
import Main.Thread.Update;
import Util.Coordinate;
import Util.Drawable;
import Util.Files;
import Util.Updateable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static Main.UI.Inventory.Slot.scale;
import static Main.UI.Inventory.Slot.scaledDimension;


public class Inventory implements Drawable, Updateable {
    protected ArrayList<Slot> inventorySlots = new ArrayList<>(), bonusslots = new ArrayList<>(), slots = new ArrayList<>();

    protected MouseSlot mouse;
    protected Dimension dimension;
    protected static final float gap = 1.5f * scale;
    private static final int slotsWidth = 7;

    protected Coordinate origin;
    protected boolean open;
    protected boolean doppelOpen;
    private DoubleInventory doubleInventory;

    protected final int size;


    public Inventory(int size){
        this.size = size;
        setupSlots();
        setOrigin(origin);
        Render.add(Render.Inventory, this);
        Update.add(Update.Game, this);
    }
    public Inventory(){
        size = 0;
        mouse = new MouseSlot(this);
        Render.add(Render.Inventory, this);
        Update.add(Update.Game, this);
    }

    private void setupSlots(){
        int height = (int) Math.ceil( (double) size / slotsWidth);
        int lastWidth = size % slotsWidth > 0 ? size % slotsWidth : slotsWidth;

        dimension = new Dimension((int) (slotsWidth * scaledDimension + gap * (slotsWidth-1)), (int) (height * scaledDimension + gap * (height-1)));
        origin = new Coordinate(GameFrame.get().getCenterX() - getWidth()/2.0, GameFrame.get().getCenterY() - getHeight()/2.0);

        for (int y = 0; y < height; y++){
            for (int x = 0; x < (y < height-1 ? slotsWidth : lastWidth); x++){
                addInventorySlot(new Slot(new Coordinate(x*(scaledDimension+gap), y*(scaledDimension+gap)), this));
            }
        }
        mouse = new MouseSlot(this);
    }
    @Override
    public void render(Graphics g) {
        if (!isOpen() || isDoubleOpen()){return;}
        draw(g);
    }

    public void draw(Graphics g){
        drawSlots(g);
        drawItems(g);
        drawTags(g);
    }

    public void drawSlots(Graphics g){
        for(Slot s: slots){
            s.render(g);
        }
    }

    public void drawTags(Graphics g){
        for(Slot s: slots){
            s.drawTag(g);
        }
    }
    public void drawItems(Graphics g){
        for(Slot s: slots){
            s.drawItem(g);
        }
    }
    public int getSize() {
        return size;
    }

    public double getHeight(){
        return dimension.getHeight();
    }
    public double getWidth(){
        return dimension.getWidth();
    }

    public Coordinate getOrigin(){
        return origin;
    }

    public void setOpen(boolean open){
        this.open = open;
        if (!open && !getMouse().isEmpty()){
            getMouse().returnItem();
        }
    }
    public void setDoubleInventoryOpen(boolean open, DoubleInventory doubleInventory){
        setOpen(open);
        this.doubleInventory = doubleInventory;
        doppelOpen = open;
    }
    public boolean isOpen(){
        return open;
    }
    public boolean isDoubleOpen(){
        return doppelOpen;
    }

    public boolean add(InventoryItem inventoryItem) {
        for(Slot s: inventorySlots){
            if(s.getInventoryItem().getStack().equals(inventoryItem.getStack())){
                inventoryItem.remove((s.getInventoryItem().add(inventoryItem.getStack().getSize())));
                if (inventoryItem.getStack().getSize() < 1){
                    return true;
                }
                s.getInventoryItem().refresh();
            }
        }
        for(Slot s: inventorySlots){
            if (s.isEmpty()){
                s.add(inventoryItem);
                return true;
            }
        }
        return false;
    }

    public boolean addAt(InventoryItem inventoryItem, int position) {
        for(int i = position; i < inventorySlots.size(); i++){
            if (inventorySlots.get(i).isEmpty()){
                inventorySlots.get(i).add(inventoryItem);
                return true;
            }
        }
        return false;
    }

    public boolean add(Item item) {
        return add(new InventoryItem(item));
    }
    public boolean addAt(Item item, int position) {
        return addAt(new InventoryItem(item),position);
    }
    public boolean addAt(Item item, int position, int amount) {
        return addAt(new InventoryItem(item, amount),position);
    }
    @Override
    public void update() {
        if (!isOpen()){return;}
        for (Slot s: slots){
            s.update();
        }
        getMouse().update();
    }

    public MouseSlot getMouse(){
        return !isDoubleOpen() ? mouse : doubleInventory.getMouse();
    }

    public void setOrigin(Coordinate origin){
        this.origin.set(origin);
    }
    public void setOriginY(double y){
        origin.setY(y);
    }

    public boolean isEmpty(){
        for (Slot s: inventorySlots){
            if (s.getInventoryItem().getType() != Item.EMPTY){
                return false;
            }
        }
        return true;
    }

    public DoubleInventory getDoppelInventory(){
        return doubleInventory;
    }

    public boolean containsFilteredSlot(Class<? extends Item> filter){
        return false;
    }
    public Slot getFilteredSlot(Class<? extends Item> filter){
        return null;
    }

    public void save(){
        InventoryFileWriter writer = new InventoryFileWriter(Files.getSavesFolder() + "inventory");
        writer.write(this);
    }
    public boolean isFull(){
        for (Slot s: inventorySlots){
            if (s.getInventoryItem().getType() == Item.EMPTY){
                return false;
            }
        }
        return true;
    }

    public void gather(InventoryItem type){
        for (Slot s: inventorySlots){
            if(s.getInventoryItem().sameType(type)){
                if(getMouse().getInventoryItem().getSize() == getMouse().getInventoryItem().getMaxSize()){
                    return;
                }
                s.remove(getMouse().getInventoryItem().add(s.getInventoryItem().getSize()));
                s.getInventoryItem().refresh();
            }
        }
    }

    public ArrayList<Itemstack> getItems(){
        ArrayList<Itemstack> items = new ArrayList<>();
        for (Slot s: inventorySlots){
            items.add(s.getInventoryItem().getStack());
        }
        return items;
    }

    public ArrayList<Itemstack> getBonusItems(){
        ArrayList<Itemstack> items = new ArrayList<>();
        for (Slot s: bonusslots){
            items.add(s.getInventoryItem().getStack());
        }
        return items;
    }

    public void removedResult(){}

    public void addInventorySlot(Slot... slot){
        inventorySlots.addAll(List.of(slot));
        slots.addAll(List.of(slot));
    }
    public void addBonusSlot(Slot... slot){
        bonusslots.addAll(List.of(slot));
        slots.addAll(List.of(slot));
    }

    public boolean contains(Item type){
        for (Slot s: inventorySlots){
            if(s.getInventoryItem().sameType(type) && s.getInventoryItem().canAdd(1)){
                return true;
            }
        }
        return false;
    }
}
