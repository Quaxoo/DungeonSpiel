package Main.UI;

import Main.GameFrame;
import Main.Level.Level;
import Main.Thread.Render;
import Main.Thread.Update;
import States.Constants;
import Util.Drawable;
import Util.Sprite;
import Util.Updateable;

import java.awt.*;
import java.util.Objects;

public class ControlIcons implements Updateable, Drawable {

    private static Sprite inventory, use;
    public ControlIcons(){
        inventory.setCenter(GameFrame.get().getWidth() - inventory.getWidth()/2.0, Constants.uiBottom);
        use.setCenter(GameFrame.get().getWidth() - inventory.getWidth() - use.getWidth()/2.0, Constants.uiBottom);
        Render.add(Render.UI, this);
        Update.add(Update.Game, this);
    }

    public static void load(){
        inventory = new Sprite(Sprite.InventoryIcon, 4.2f * Constants.globalScale);
        use = new Sprite(Sprite.UseIcon, 4f * Constants.globalScale);
    }
    @Override
    public void render(Graphics g) {
        inventory.draw(g);
        if(Objects.requireNonNull(Level.getClosestInteractable()).isAccessable()) {
            use.draw(g);
        }
    }

    @Override
    public void update() {

    }
}
