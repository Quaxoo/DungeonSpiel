package Main.UI;

import Main.GameFrame;
import Main.Item.Weapon;
import Main.Player;
import Main.Thread.Render;
import Main.Thread.Update;
import States.Constants;
import Util.Drawable;
import Util.Sprite;
import Util.Updateable;

import java.awt.*;

public class EquipedWeapon implements Drawable, Updateable {

    private static Sprite slot, item;
    private static Weapon weapon;

    public EquipedWeapon(){
        slot = new Sprite(Sprite.SlotUI, 3 * Constants.globalScale);
        slot.setCenter(GameFrame.get().getWidth()*0.25, Constants.uiBottom);
        weapon = Player.get().getWeapon();
        item = weapon.getIcon();
        Render.add(Render.UI, this);
        Update.add(Update.Always, this);
    }
    @Override
    public void render(Graphics g) {
        if (!Player.get().isInventoryOpen()){
            slot.draw(g);
            item.drawCenter(slot.getCenterX(), slot.getCenterY(),g);
        }
    }

    @Override
    public void update() {
        if(Player.get().isInventoryOpen() && Player.get().getWeapon() != weapon){
            refresh();
        }
    }

    public static void refresh(){
        weapon = Player.get().getWeapon();
        item = new Sprite(weapon.getIcon());
    }
}
