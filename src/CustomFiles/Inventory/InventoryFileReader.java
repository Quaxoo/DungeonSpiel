package CustomFiles.Inventory;

import Main.Item.Item;
import Main.Item.Weapon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import static CustomFiles.Inventory.InventoryFile.format;

public class InventoryFileReader {
    private final String path;
    public InventoryFileReader(String path){
        this.path = path + format;
    }

    public InventoryFile read() {
        InventoryFile file;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            file = new InventoryFile();
            ArrayList<String> lines = new ArrayList<>();
            while (reader.ready()) {
                lines.add(reader.readLine());
            }
            if (lines.get(0).contains("size: ")) {
                file.setSize(Integer.parseInt(lines.get(0).substring(lines.get(0).indexOf(":") + 2)));
            }
            Weapon type = null;
            Item item = null;
            int damage = 0, position = 0, amount = 0;
            float factor = 0, aps = 0;
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String value = line.substring(line.indexOf(":") + 2);

                if (line.contains("weapon:")) {
                    type = (Weapon) Item.getItem(value);
                    damage = type.getDamage();
                    factor = type.getCritFactor();
                    aps = type.getAPS();
                    position = 0;
                    item = null;
                }
                if (line.contains("item:")) {
                    item = Item.getItem(value);
                    position = 0;
                    amount = 1;
                    type = null;
                }
                if(type != null){
                    damage = line.contains("damage:") ? Integer.parseInt(value) : damage;
                    factor = line.contains("factor:") ? Float.parseFloat(value) : factor;
                    aps = line.contains("aps:") ? Float.parseFloat(value) : aps;
                    position = line.contains("position:") ? Integer.parseInt(value) : position;
                    if(line.contains("}")){
                        file.add(new InventoryItemObject(new Weapon(type, damage, factor, aps), position, 1));
                    }
                }
                if(item != null){
                    position = line.contains("position:") ? Integer.parseInt(value) : position;
                    amount = line.contains("amount:") ? Integer.parseInt(value) : amount;
                    if(line.contains("}")){
                        file.add(new InventoryItemObject(item, position, amount));
                    }
                }
            }
            return file;
        } catch (Exception e) {
            return null;
        }
    }
}
