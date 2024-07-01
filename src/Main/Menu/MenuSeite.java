package Main.Menu;

import Main.Game;
import Main.GameFrame;
import States.Constants;
import States.Settings;
import Util.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

public class MenuSeite implements Updateable, Drawable {
    private static Sprite normal, start;
    private static Coordinate render;
    private static final float scale = (1f * Constants.globalScale);
    private static final int width = 18, height = 11;
    private CopyOnWriteArrayList<MenuElement> elements = new CopyOnWriteArrayList<>();
    private static ArrayList<MenuSeite> seiten = new ArrayList<>();
    public static int Start = 0, Einstellungen = 1, Grafik = 2, Audio = 3, Language = 4;

    public MenuSeite() {
        seiten.add(this);
    }

    public void add(MenuElement... element){
        elements.addAll(Arrays.asList(element));
    }

    public static MenuSeite get(int seite){
        return seiten.get(seite);
    }
    public static int get(MenuSeite seite){
        return seiten.indexOf(seite);
    }
    public static ArrayList<MenuSeite> get(){
        return seiten;
    }

    public void close(){
        for (MenuElement e: elements){
            try {
                ((Dropdown) e).close();
            }catch (Exception ignored){
            }
        }
    }

    public static void loadn(){
        Sprite cornerLU = new Sprite(Sprite.MenuCornerLinksUnten, scale);
        Sprite cornerRU = new Sprite(Sprite.MenuCornerRechtsUnten, scale);
        Sprite cornerLO = new Sprite(Sprite.MenuCornerLinksOben, scale);
        Sprite cornerRO = new Sprite(Sprite.MenuCornerRechtsOben, scale);

        Sprite sideL = new Sprite(Sprite.MenuSideLinks, scale);
        Sprite sideR = new Sprite(Sprite.MenuSideRechts, scale);
        Sprite sideO = new Sprite(Sprite.MenuSideOben, scale);
        Sprite sideU = new Sprite(Sprite.MenuSideUnten, scale);

        Sprite background = new Sprite(Sprite.MenuBackground);

        int scaledDimension = cornerLU.getWidth();

        normal = new Sprite(scaledDimension  * width, scaledDimension*height);
        cornerLO.drawOn(normal, 0 , 0);
        cornerRO.drawOn(normal, normal.getWidth() - scaledDimension , 0);
        cornerLU.drawOn(normal, 0 , normal.getHeight() - scaledDimension);
        cornerRU.drawOn(normal, normal.getWidth() - scaledDimension, normal.getHeight() - scaledDimension);

        for (int i = scaledDimension; i < (width-1)*scaledDimension; i+=scaledDimension){
            sideO.drawOn(normal, i,0);
            sideU.drawOn(normal, i,normal.getHeight() - scaledDimension);
        }
        for (int i = scaledDimension; i < (height-1)*scaledDimension; i+=scaledDimension){
            sideL.drawOn(normal, 0,i);
            sideR.drawOn(normal, normal.getWidth() - scaledDimension,i);
        }
        background.scale(normal.getWidth()-2*scaledDimension, normal.getHeight()-2*scaledDimension);
        background.drawOn(normal, scaledDimension,scaledDimension);
        render = new Coordinate(GameFrame.get().getCenterX() - normal.getWidth()/2.0, GameFrame.get().getCenterY() - normal.getHeight()/2.0);
        normal.alpha(0.95f);
    }

    @Override
    public void render(Graphics g) {
        //normal.draw(render.getX(), render.getY(), g);
        for (MenuElement e: elements){
            e.render(g);
        }
    }

    @Override
    public void update() {
        for (MenuElement e: elements){
            e.update();
        }
    }

    public void refresh(){
        for(MenuElement m: elements){
            m.refresh();
        }
    }

    public static void load(){
        MenuSeite start = new MenuSeite();
        Button setting = new Button(new Coordinate(GameFrame.get().getCenterX(), GameFrame.get().getHeight()*0.35), Text.Settings, () -> Menu.get().changeSeite(Einstellungen));
        Button leaveGame = new Button(new Coordinate(GameFrame.get().getCenterX(), GameFrame.get().getHeight()*0.5),Text.Leave, Game::close);
        Button continueGame = new Button(new Coordinate(GameFrame.get().getCenterX(), GameFrame.get().getHeight()*0.65),Text.Continue, () -> Menu.get().openClose());
        start.add(setting, leaveGame, continueGame);

        MenuSeite settings = new MenuSeite();
        Button grafik = new Button(new Coordinate(GameFrame.get().getCenterX(), GameFrame.get().getHeight()*0.35),Text.Grafik, () -> Menu.get().changeSeite(Grafik));
        Button audio = new Button(new Coordinate(GameFrame.get().getCenterX(), GameFrame.get().getHeight()*0.5),Text.Audio, () -> Menu.get().changeSeite(Audio));
        Button sprache = new Button(new Coordinate(GameFrame.get().getCenterX(), GameFrame.get().getHeight()*0.65),Text.Language, () -> Menu.get().changeSeite(Language));
        settings.add(grafik, audio, sprache);

        MenuSeite graphics = new MenuSeite();
        Dropdown fps = new Dropdown(new Coordinate(GameFrame.get().getCenterX(), GameFrame.get().getHeight()*0.5), Dropdown.FPS);
        graphics.add(fps);

        MenuSeite audios = new MenuSeite();
        Scrollbar music = new Scrollbar(new Coordinate(GameFrame.get().getCenterX(), GameFrame.get().getHeight()*0.35), 100* Constants.globalScale, Text.Music, Settings.MUSIC);
        Scrollbar sfx = new Scrollbar(new Coordinate(GameFrame.get().getCenterX(), GameFrame.get().getHeight()*0.65), 100* Constants.globalScale, Text.SFX, Settings.SFX);
        audios.add(music, sfx);

        MenuSeite language = new MenuSeite();
        Dropdown languages = new Dropdown(new Coordinate(GameFrame.get().getCenterX(), GameFrame.get().getHeight()*0.5), Dropdown.Language);
        language.add(languages);
    }

}