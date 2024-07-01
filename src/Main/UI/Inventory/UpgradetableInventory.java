package Main.UI.Inventory;

import Main.GameFrame;
import Main.Item.Equipment;
import Main.Item.Ingredient;
import Main.Item.Item;
import States.Constants;
import Util.Coordinate;
import Util.Sprite;

import java.awt.*;

import static Main.UI.Inventory.Slot.scaledDimension;

public class UpgradetableInventory extends Inventory{
    private final FilteredSlot basic;
    private final FilteredSlot ingredient;
    private final ResultSlot upgraded;
    private final Sprite arrow;
    private String toolitemId = "", ingredientId = "";
    public UpgradetableInventory(){
        super();

        dimension = new Dimension((int) (5 * scaledDimension + gap * 4), (int) (2 * scaledDimension + gap));
        origin = new Coordinate(GameFrame.get().getCenterX() - getWidth()/2.0, GameFrame.get().getCenterY() - getHeight()/2.0);
        arrow = new Sprite(Sprite.Arrow, 6f * Constants.globalScale);
        arrow.trim();

        basic = new FilteredSlot(new Coordinate(0,dimension.getHeight() - Slot.slotNormal.getHeight()),this, Equipment.class);
        ingredient = new FilteredSlot(new Coordinate(dimension.getWidth()/2.0 - Slot.slotNormal.getWidth()/2.0,- Slot.slotNormal.getHeight() * 0.1f),this, Ingredient.class);
        upgraded = new ResultSlot(new Coordinate(dimension.getWidth() - Slot.slotNormal.getWidth(),dimension.getHeight() - Slot.slotNormal.getHeight()),this);

        setOrigin(origin);
        addInventorySlot(basic, ingredient, upgraded);
    }

    public void setOriginY(double y){
        origin.setY(y);
        arrow.setCenterX(origin.getX() + dimension.getWidth()/2.0);
        arrow.setPositionY(origin.getY() + dimension.getHeight() - arrow.getHeight());
    }
    public void setOrigin(Coordinate origin){
        this.origin.set(origin);
        arrow.setCenterX(origin.getX() + dimension.getWidth()/2.0);
        arrow.setPositionY(origin.getY() + dimension.getHeight() - arrow.getHeight());
    }

    public void drawSlots(Graphics g){
        arrow.draw(g);
        for(Slot s: slots){
            s.render(g);
        }
    }

    @Override
    public void update() {
        if (!isOpen()){return;}
        for (Slot s: slots){
            s.update();
        }
        getMouse().update();
        checkRecipe();
    }

    protected void checkRecipe(){
            if (!ingredient.isEmpty() && !basic.isEmpty()) {
                if(upgraded.isEmpty() || (!upgraded.isEmpty() && (basic.getInventoryItem().getType().getId() != toolitemId || ingredient.getInventoryItem().getType().getId() != ingredientId))) {
                    upgraded.add(new InventoryItem(Equipment.get(basic.getInventoryItem().getType())));
                    upgraded.getItem().upgrade((Ingredient) ingredient.getInventoryItem().getType());
                    toolitemId = basic.getInventoryItem().getType().getId();
                    ingredientId = ingredient.getInventoryItem().getType().getId();
                }
            }else if(!upgraded.isEmpty()){
                upgraded.add(new InventoryItem(Item.EMPTY));
            }
    }

    public void removedResult(){
        ingredient.remove(1);
        basic.remove();
    }

    public boolean add(InventoryItem inventoryItem) {
        if(basic.canAdd(inventoryItem.getType())){
            basic.add(inventoryItem);
            return true;
        }
        if(ingredient.canAdd(inventoryItem.getType())){
            ingredient.add(inventoryItem);
            return true;
        }
        return false;
    }
}
