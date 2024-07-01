package Main.Menu;

import Input.Mouse;
import Main.Game;
import Main.Player;
import Main.Thread.Render;
import Main.Thread.Update;
import Util.Drawable;
import Util.KeyTypedListener;
import Util.Language;
import Util.Updateable;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Stack;

import static States.Gamestate.PAUSED;
import static States.Gamestate.PLAYING;

public class Menu implements Updateable, Drawable {

    private static Menu menu;
    private static boolean open;
    private KeyTypedListener escape = new KeyTypedListener(this::openClose, KeyEvent.VK_ESCAPE);
    private MenuSeite openSite;
    private final Stack<Integer> siteHistory = new Stack<>();
    private boolean back;

    public Menu(){
        menu = this;
        openSite = MenuSeite.get(MenuSeite.Start);
        Update.add(Update.Always, this);
        Render.add(Render.Menu, this);
    }

    @Override
    public void update() {
        escape.check();
        if (open){
            openSite.update();
        }
        if(!back && Mouse.isMouseDown(4) && !siteHistory.isEmpty()){
            back = true;
            back();
        }else if(back && !Mouse.isMouseDown(4)){
            back = false;
        }
    }
    @Override
    public void render(Graphics g) {
        openSite.render(g);
    }

    public void openClose(){
        if (Player.get().isInventoryOpen()){return;}
        open = !open;
        if (!open){
            openSite.close();
            Mouse.clear();
            siteHistory.clear();
        }else{
            openSite = MenuSeite.get(MenuSeite.Start);
        }
        Game.set(open ? PAUSED : PLAYING);

    }
    public static Menu get(){
        return menu;
    }
    public void changeSeite(int i){
        siteHistory.push(MenuSeite.get(openSite));
        openSite = MenuSeite.get(i);
    }
    public void back(){
        openSite = MenuSeite.get(siteHistory.pop());
    }

    public void refresh(){
        Language.load();
        for (MenuSeite m: MenuSeite.get()){
            m.refresh();
        }
    }
}
