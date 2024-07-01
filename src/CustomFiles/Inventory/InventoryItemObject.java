package CustomFiles.Inventory;

import Main.Item.Item;

public class InventoryItemObject {
    private Item item;
    private int position;
    private int amount;

    public InventoryItemObject(Item item, int position, int amount) {
        this.item = item;
        this.position = position;
        this.amount = amount;
    }

    public Item getItem() {
        return item;
    }

    public int getPosition() {
        return position;
    }

    public int getAmount() {
        return amount;
    }
}
