package States;

import Util.Files;
import Util.Language;
import Util.Setting;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public abstract class  Settings {

    private static final ArrayList<Setting> settings = new ArrayList<>();

    public static Setting UP = new Setting("up", KeyEvent.VK_W);
    public static Setting DOWN = new Setting("down", KeyEvent.VK_S);
    public static Setting LEFT = new Setting("left", KeyEvent.VK_A);
    public static Setting RIGHT = new Setting("right", KeyEvent.VK_D);
    public static Setting INVENTORY = new Setting("inventory", KeyEvent.VK_TAB);
    public static Setting ATTACK = new Setting("attack", MouseEvent.BUTTON1);
    public static Setting LANGUAGE = new Setting("language", Language.German.getIndex());
    public static Setting MUSIC = new Setting("music", 50);

    public static Setting SFX = new Setting("sfx", 50);

    public static Setting FPS = new Setting("fps", 120);
    public static Dimension ScreenSize;

    public static void load(){
        String setting = Files.load(Files.Settings);
        if(setting == null){
            setting = reset();
        }
        for (Setting s: settings){
            if (setting.contains(s.getName())){
                int index = setting.indexOf(s.getName())+s.getName().length()+1;
                String sub = setting.substring(index, setting.indexOf(";",index));
                int value = Integer.parseInt(sub);
                s.set(value);
            }
        }
        for (Setting s: settings){
            if (s.get() < 0){
                s.reset();
                save();
            }
        }
    }
    public static void save(){
        Files.save(Files.Settings, getString());
    }
    public static String reset(){
        for (Setting s: settings){
            s.reset();
        }
        save();
        return getString();
    }

    private static String getString(){
        StringBuilder content = new StringBuilder();
        for (Setting s: settings){
            content.append(s.getName() + "=" + s.get() + ";\n");
        }
        return content.toString();
    }

    public static void add(Setting setting){
        settings.add(setting);
    }
}
