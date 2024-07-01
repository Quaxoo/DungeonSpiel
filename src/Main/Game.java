package Main;

import Main.Item.Consumeable;
import Main.Item.Ingredient;
import Main.Item.Stat;
import Main.Item.Weapon;
import Main.Level.Level;
import Main.Menu.*;
import Main.Thread.Render;
import Main.Thread.Update;
import Main.UI.Bar;
import Main.UI.ControlIcons;
import Main.UI.EquipedWeapon;
import Main.UI.Inventory.FilteredSlot;
import Main.UI.Inventory.InventoryTag;
import Main.UI.Inventory.Slot;
import Main.UI.KeyState;
import States.Constants;
import States.Gamestate;
import States.Settings;
import Util.*;

import java.net.URL;

import static States.Gamestate.PLAYING;

public class Game {
    public static Gamestate status = PLAYING;
    public static final int UPS = 200;
    public static URL resFolderInJarfile;
    private static boolean stopThread;

    public Game(){

        resFolderInJarfile = getClass().getClassLoader().getResource("res/");
        stop();

        new GamePanal();
        new GameFrame();

        onStart();

        new Player(new Coordinate(GameFrame.get().getCenterX(), GameFrame.get().getCenterY()));
        new Menu();

        new Bar(Sprite.Healthbar, Sprite.Redbar, new Coordinate(GameFrame.get().getCenterX(), Constants.uiBottom));
        new KeyState();
        new EquipedWeapon();
        new Darkness();
        new ControlIcons();
        new Music();

        Level.create();

        setupThread();
        start();
        GamePanal.get().requestFocus();
    }

    private void setupThread(){
        new Update().start();
        new Render().start();
    }

    private void onStart(){
        Render.load();
        Update.load();
        Level.loadSprites();
        Settings.load();
        Language.load();
        Damagenumber.load();
        Stat.load();
        Text.load();
        Slot.load();
        FilteredSlot.load();
        Button.load();
        Scrollbar.load();
        Dropdown.load();
        Tag.load();
        InventoryTag.load();
        MenuSeite.load();
        Weapon.load();
        Consumeable.load();
        Ingredient.load();
        KeyState.load();
        Goblin.load();
        ControlIcons.load();
        Music.load();
        Sound.load();
    }

    public static void set(Gamestate status){
        Game.status = status;
    }

    public static void close(){
        System.exit(0);
    }

    public static void stop(){
        stopThread  = true;
    }

    public static void start(){
        stopThread  = false;
        Update.get().restart();
        Render.get().restart();
    }

    public static boolean paused(){
        return stopThread;
    }
}