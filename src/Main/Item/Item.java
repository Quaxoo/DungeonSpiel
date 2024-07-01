package Main.Item;

import States.Constants;
import Util.Language;
import Util.Sprite;

import java.awt.*;
import java.util.ArrayList;

public class Item {

    protected final String id;
    protected Sprite icon;
    protected int stacksize;
    protected float iconScale;
    private static final ArrayList<Item> items = new ArrayList<>();
    public static Item EMPTY = new Item("empty",1,1);
    public static Item Key = new Item("key",1,3.5f);

    public Item(String id, int stacksize, float iconScale) {
        this.id = id;
        this.stacksize = stacksize;
        this.iconScale = iconScale;
        loadIcon();
        items.add(this);
    }

    private void loadIcon(){
        icon = new Sprite("assets/items/icons/" + id + ".png", iconScale * Constants.globalScale);
        icon.trim();
    }
    public Sprite getIcon(){
        return icon;
    }
    public int getStacksize(){
       return stacksize ;
    }
    public String getId() {
        return id;
    }

    public Sprite getDescription(float scale) {
        return null;
    }

    public void draw(int direction, int img, double x, double y, Graphics g)  {}

    public String getTag() {
        return Language.get(id);
    }

    public static Item getItem(String id){
        for (Item i: items){
            if (i.getId().equals(id)){
                return i;
            }
        }
        return EMPTY;
    }

    public static void load(){}
    public void upgrade(Ingredient ingredient){}
}
