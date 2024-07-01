package CustomFiles.Inventory;

import Main.Item.Item;
import Main.Item.Itemstack;
import Main.Item.Weapon;
import Main.UI.Inventory.Inventory;
import Util.Files;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import static CustomFiles.Inventory.InventoryFile.format;

public class InventoryFileWriter {
    private final String path;

    public InventoryFileWriter(String path){
        this.path = path + format;
    }

    public void write(Inventory inventory){
        try {
            if (!Files.exists(path)){
                new File(path);
            }
            FileWriter writer = new FileWriter(path);

            StringBuilder content = new StringBuilder();
            content.append("size: " + inventory.getSize() + "\n");
            ArrayList<Itemstack> items = inventory.getItems();
            ArrayList<Itemstack> bonusitems = inventory.getBonusItems();
            for(int i = 0; i < items.size(); i++){
                Itemstack stack = items.get(i);
                if(stack.getType() instanceof Weapon weapon){
                    content.append("{\n");
                    content.append("  weapon: " + weapon.getId() + "\n");
                    content.append("  damage: " + weapon.getDamage() + "\n");
                    content.append("  factor: " + weapon.getCritFactor() + "\n");
                    content.append("  aps: " + weapon.getAPS() + "\n");
                    content.append("  position: " + i + "\n");
                    content.append("}\n");
                }else if(stack.getType() != Item.EMPTY){
                    content.append("{\n");
                    content.append("  item: " + stack.getType().getId() + "\n");
                    content.append("  amount: " + stack.getSize() + "\n");
                    content.append("  position: " + i + "\n");
                    content.append("}\n");
                }
            }
            for(int i = 0; i < bonusitems.size(); i++){
                Itemstack stack = bonusitems.get(i);
                if(stack.getType() instanceof Weapon weapon){
                    content.append("{\n");
                    content.append("  weapon: " + weapon.getId() + "\n");
                    content.append("  damage: " + weapon.getDamage() + "\n");
                    content.append("  factor: " + weapon.getCritFactor() + "\n");
                    content.append("  aps: " + weapon.getAPS() + "\n");
                    content.append("  position: " + -(i + 1) + "\n");
                    content.append("}\n");
                }else if(stack.getType() != Item.EMPTY){
                    content.append("{\n");
                    content.append("  item: " + stack.getType().getId() + "\n");
                    content.append("  amount: " + stack.getSize() + "\n");
                    content.append("  position: " + -(i + 1) + "\n");
                    content.append("}\n");
                }
            }
            writer.write(content.toString());
            writer.close();
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
