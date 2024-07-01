package Main.Item;

import Util.Sprite;
import Util.Text;

public class Itemstack{
    private Item type;
    private final int maxsize;
    private int size, displayedSize;
    private Sprite sizeSprite = new Sprite(), stackSprite = new Sprite();

    public Itemstack(Item type){
        this.maxsize = type.getStacksize();
        this.type = type;
        size = 1;
        displayedSize = 0;
    }
    public Itemstack(Item type, int size){
        this.maxsize = type.getStacksize();
        this.type = type;
        this.size = size;
        displayedSize = 0;
    }

    public void remove(int amount){
        size-=amount;
    }
    public void add(int amount){
        size += amount;
    }
    public boolean canAdd(int amount){
        return size + amount <= maxsize;
    }
    public Sprite getIcon(){
        if(size != displayedSize ){
            stackSprite = new Sprite(getItemIcon());
            if(size > 1) {
                sizeSprite = Text.get(String.valueOf(size),1);
            }
            displayedSize = size;
        }
        return stackSprite;
    }
    public Sprite getSizeSprite(){
        return sizeSprite;
    }

    public boolean equals(Itemstack stack){
        return getType() == stack.getType();
    }

    public Item getType(){
        return type;
    }

    public Sprite getItemIcon(){
        return type.getIcon();
    }

    public int getSize(){
        return size;
    }
    public int getMaxSize(){
        return maxsize;
    }
}
