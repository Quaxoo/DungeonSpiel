package Util;

import Main.UI.Inventory.Inventory;

public interface Interactable {
    void interact();
    double getDistance();
    void delete();

    boolean isAccessable();
    boolean hasInventory();
    Inventory getInventory();
    void save();
}
