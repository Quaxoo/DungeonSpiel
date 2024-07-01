package Main.UI;

import Main.GameFrame;
import Main.Player;
import Main.Thread.Render;
import Main.Thread.Update;
import States.Constants;
import Util.Coordinate;
import Util.Drawable;
import Util.Sprite;
import Util.Updateable;

import java.awt.*;

public class KeyState implements Drawable, Updateable {
    private static Sprite available, unavailable;
    private static float scale = 4f * Constants.globalScale;

    public KeyState(){
        available.setCenter(new Coordinate(GameFrame.get().getWidth() * 0.75, Constants.uiBottom));
        unavailable.setCenter(new Coordinate(GameFrame.get().getWidth() * 0.75, Constants.uiBottom));
        Update.add(Update.Game,this);
        Render.add(Render.UI,this);
    }

    public static void load(){
        available = new Sprite(Sprite.Key, scale);
        available.trim();
        unavailable = new Sprite(Sprite.KeyMissing, scale);
        unavailable.trim();
    }

    @Override
    public void render(Graphics g) {
        getSprite().draw(g);
    }

    private Sprite getSprite(){
        return Player.get().hasKey() ? available : unavailable;
    }

    @Override
    public void update() {

    }
}
