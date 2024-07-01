package Main.Menu;

import Input.Mouse;
import States.Constants;
import States.Direction;
import States.Settings;
import Util.*;

import java.awt.*;

public class Scrollbar extends MenuElement{
    private static Sprite links, rechts, slice, button;
    private Sprite[] percantage = new Sprite[101];
    private Sprite[] text = new Sprite[101];
    private final float width;
    private int valueLength;
    private double percentPerPixel;
    private int value;
    private String des;
    private static final float scale = 6f * Constants.globalScale;
    private static final float scaledDimension = scale * 8;
    private final Button scrollbutton;
    private Tag description;
    private final Setting setting;

    public Scrollbar(Coordinate center, double width, String description, Setting setting) {
        super(center);
        this.width = (float) width;
        this.des = description;
        this.setting = setting;
        loadSprite();
        scrollbutton = new Button(center, (int) (getX() + scale));
    }

    public static void load() {
        links = new Sprite(Sprite.ScrollbarLeft, scale);
        rechts = new Sprite(Sprite.ScrollbarRight, scale);
        slice = new Sprite(Sprite.ScrollbarSlice, scale);
        button = new Sprite(Sprite.ScrollbarButton, scale);
    }

    private void loadSprite(){
        normal = new Sprite((width + 2) * scale, scaledDimension);

        links.drawOn(normal, 0,0);
        rechts.drawOn(normal, normal.getWidth() - scaledDimension, 0);

        normal.fillX(slice, scaledDimension, scaledDimension, 0);

        normal.setPosition(getX(),getY());

        for (int x = 0; x < percantage.length; x++){
            percantage[x] = Text.get(x + "%", (scale - 2 * Constants.globalScale));
            percantage[x].trim();
            percantage[x].setCenter(normal.getCenterX(), normal.getY() - percantage[x].getHeight()/2.0 - scale);
        }

        description = new Tag(des,scale - 2 * Constants.globalScale, normal, scale, Direction.Down);
    }

    @Override
    public void render(Graphics g) {
        normal.draw(g);
        description.render(g);
        scrollbutton.render(g);
        percantage[value].draw(g);
    }

    @Override
    public void update() {
        scrollbutton.update();
    }

    @Override
    protected void refresh() {
        description.refresh();
    }

    @Override
    protected Sprite getSprite() {
        return normal;
    }

    @Override
    protected Rectangle getBounds() {
        return null;
    }


    public class Button extends MenuElement{
        private final int start, end;

        public Button(Coordinate now, int start) {
            super(new Coordinate(now));

            normal = new Sprite(button);
            normal.setPositionY(getY());

            this.start = start + normal.getWidth()/2;
            this.end = (int)(start + width * scale - normal.getWidth()/2);

            valueLength = this.end - this.start;
            percentPerPixel = 100.0/valueLength;

            setValue(setting.get());
        }

        @Override
        protected Sprite getSprite() {
            return normal;
        }

        @Override
        public void render(Graphics g) {
            normal.draw(g);
        }

        @Override
        public void update() {
            checkClick();
            move();
        }

        private void move(){
            if (down){
                if (Mouse.get().getX() >= start && Mouse.get().getX() <= end){
                    normal.setCenterX(Mouse.get().getX());
                }else if(down && Mouse.get().getX() < start){
                    normal.setCenterX(start);
                }else if(down && Mouse.get().getX() > end){
                    normal.setCenterX(end);
                }
                int v = (int)((normal.getCenterX()-start)*percentPerPixel);
                if (value != v){
                    value = v;
                }
            }
        }

        @Override
        protected void doUnclick() {
            setting.set(value);
            Settings.save();
            if (setting == Settings.MUSIC){
                Music.get().changeVolume();
            }
        }

        private void setValue(int v){
            value = v;
            normal.setCenterX(start+(1/percentPerPixel)*v);
        }

        @Override
        protected Rectangle getBounds() {
            return normal.getBounds();
        }
    }
}