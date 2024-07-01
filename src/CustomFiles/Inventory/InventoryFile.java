package CustomFiles.Inventory;

import java.util.ArrayList;

public class InventoryFile {
    public final static String format = ".inv";
    private int size;
    private ArrayList<InventoryItemObject> items = new ArrayList<>();

    public int getSize() {
        return size;
    }
    public void setSize(int size){
        this.size = size;
    }

    public ArrayList<InventoryItemObject> getItems() {
        return items;
    }

    public void add(InventoryItemObject itemObject){
        items.add(itemObject);
    }

}

