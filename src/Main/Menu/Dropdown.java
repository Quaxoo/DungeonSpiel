package Main.Menu;

import Input.Mouse;
import Main.Thread.Render;
import States.Constants;
import States.Direction;
import States.Settings;
import Util.Coordinate;
import Util.Setting;
import Util.Sprite;
import Util.Text;

import java.awt.*;

public class Dropdown extends MenuElement{
    private static Sprite links,rechts,slice,linkshovered;
    private static Sprite optionsCornerLU,optionsCornerLO,optionsCornerRU,optionsCornerRO, optionsSideL,optionsSideR,optionsSideO,optionsSideU,optionsPixel;
    private static final float scale = 6f * Constants.globalScale;
    private final Option[] options;
    private final int o;
    private Option selectedOption;
    private float paddingX;
    private static final float paddingY = (2f * scale);
    private static final float optionPaddingY = (2f * scale);
    private boolean open;
    private Sprite optionsBackground, optionsTextfield, hovered, clicked, normalDefault, hoveredDefault;
    private int scroll = 0;
    public static int Language = 0, FPS = 1;
    public final Option[][] allOptions = {
            {new Option("DEUTSCH",0,0), new Option("ENGLISH",1,1), new Option("NEDERLANDS",2,2), new Option("FRANCAIS",3,3), new Option("ITALIANO",4,4), new Option("ESPANOL",5,5), new Option("BOARISCH",6,6)},
            {new Option("30",0,30), new Option("60",1,60), new Option("90",2,90), new Option("120",3,120), new Option("240",4,240), new Option("360",5,360)}
    };
    public final Setting[] settings = {
            Settings.LANGUAGE,
            Settings.FPS
    };
    public final String[] descriptions = {
            Text.Language,
            Text.Fps
    };
    public final float[] paddings = {
            6f*scale,
            12f*scale
    };

    private final int optionsLength = 4;
    private final float spriteGapL = 14 * scale;
    private final float spriteGapR = 7 * scale;
    private final float spriteGapO = 5 * scale;
    private final float spriteGapU = 4 * scale;
    private final float optionSpriteGap = scale;
    private float cornerDimension = 8 * scale;
    private int highest, lowest;
    private final float scrollspeed = scale * 1f;
    private Tag description;


    public Dropdown(Coordinate center, int options) {
        super(center);
        o = options;
        this.options = allOptions[options];
        int index = 0;
        for (Option o: this.options){
            if (o.get() == settings[options].get()){
                index = o.getIndex();
            }
        }
        selectedOption = this.options[index];
        loadSprite();
    }
    public static void load(){
        links = new Sprite(Sprite.DropdownLeft, scale);
        linkshovered = new Sprite(Sprite.DropdownLeftHovered, scale);
        rechts = new Sprite(Sprite.DropdownRight, scale);
        slice = new Sprite(Sprite.DropdownSlice, scale);

        optionsCornerLO = new Sprite(Sprite.OptionsCornerLinksOben, scale);
        optionsCornerRO = new Sprite(Sprite.OptionsCornerRechtsOben, scale);
        optionsCornerRU = new Sprite(Sprite.OptionsCornerRechtsUnten, scale);
        optionsCornerLU = new Sprite(Sprite.OptionsCornerLinksUnten, scale);

        optionsSideL = new Sprite(Sprite.OptionsSideLinks, scale);
        optionsSideR = new Sprite(Sprite.OptionsSideRechts, scale);
        optionsSideU = new Sprite(Sprite.OptionsSideUnten, scale);
        optionsSideO = new Sprite(Sprite.OptionsSideOben, scale);

        optionsPixel = new Sprite(Sprite.OptionsBackground, scale);
    }

    private void loadSprite(){
        paddingX = paddings[o];
        Option option = getLongest();

        float width = option.getWidth() + 2 * paddingX + spriteGapL + spriteGapR;
        float height = option.getHeight() + 2 * paddingY + spriteGapO + spriteGapU;

        normal = new Sprite(width, height);
        hovered = new Sprite(width, height);

        Sprite l = links, r = rechts, s = slice, lh = linkshovered;

        l.scaleY(height);
        lh.scaleY(height);
        r.scaleY(height);
        s.scaleY(height);

        l.drawTopLeftOn(normal);
        lh.drawTopLeftOn(hovered);
        r.drawTopRightOn(normal);
        r.drawTopRightOn(hovered);

        for (float n = 0; n < (normal.getWidth() - 2 * l.getWidth()); n += s.getWidth()){
            s.drawOn(normal,l.getWidth() + n, 0);
            s.drawOn(hovered,l.getWidth() + n, 0);
        }

        width = option.getWidth() + 2 * paddingX + 2 * optionSpriteGap;
        height = (option.getHeight() + 2 * optionPaddingY) * optionsLength + 2 * optionSpriteGap + optionPaddingY;

        optionsBackground = new Sprite(width, height);

        Sprite LO = optionsCornerLO, RO = optionsCornerRO, LU = optionsCornerLU, RU = optionsCornerRU, L = optionsSideL, R = optionsSideR, O = optionsSideO, U = optionsSideU, B = optionsPixel;

        LO.drawTopLeftOn(optionsBackground);
        RO.drawTopRightOn(optionsBackground);
        LU.drawBottomLeftOn(optionsBackground);
        RU.drawBottomRightOn(optionsBackground);

        cornerDimension = LO.getWidth();

        for (float n = 0; n < (width - cornerDimension * 2); n += O.getWidth()){
            O.drawOn(optionsBackground, cornerDimension + n, 0);
            U.drawOn(optionsBackground, cornerDimension + n, height - U.getHeight());
        }
        for (float n = 0; n < (height - cornerDimension * 2); n += L.getHeight()){
            L.drawOn(optionsBackground,0, cornerDimension + n);
            R.drawOn(optionsBackground,width - L.getWidth(), cornerDimension + n);
        }

        B.scale(width - 2 * cornerDimension, height - 2 * cornerDimension);
        B.drawOn(optionsBackground, cornerDimension, cornerDimension);

        optionsTextfield = new Sprite(width - 10 * scale,height - 2 * scale);
        normalDefault = new Sprite(normal);
        hoveredDefault = new Sprite(hovered);
        close();
        optionsTextfield.setPosition(getTextfieldX(),getTextfieldY());
        optionsBackground.setPosition(getBackgroundX(),getBackgroundY());
        description = new Tag(descriptions[o], scale - 2 * Constants.globalScale, normal, scale, Direction.Up);
    }

    @Override
    public void render(Graphics g) {
        getSprite().draw(g);
        if(open){
            optionsBackground.draw(g);
            optionsTextfield.draw(g);
        }
        description.render(g);
    }

    @Override
    public void update() {
        checkClick();
        if(open) {
            scroll();
            for (Option o : options) {
                o.update();
            }
        }
        refreshTextfield();
    }
    private void scroll(){
        if((Mouse.getScroll() - scroll) != 0){
            if (options.length <= optionsLength){return;}

            scroll += (Mouse.getScroll() - scroll);
            refreshTextfield();

            if (scroll < lowest){
                scroll = lowest;
                Mouse.setScroll(scroll);
                refreshTextfield();
            }
            if (scroll > highest){
                scroll = highest;
                Mouse.setScroll(scroll);
                refreshTextfield();
            }

        }
    }

    @Override
    protected void refresh() {
        description.refresh();
    }

    private void refreshTextfield(){
        optionsTextfield.clear();
        for (Option o : options) {
            o.render(optionsTextfield.getGraphics());
        }
    }


    @Override
    protected Rectangle getBounds() {
        return down ? clicked.getBounds() : normal.getBounds();
    }


    @Override
    protected void doUnclick() {
        if (open){
            close();
        }else {
            open();
        }
    }

    public void open(){
        open = true;
        refreshTextfield();
        Mouse.clearScroll();
        lowest = 0;
        highest = (int) ((options[options.length-1].y - options[optionsLength-1].y)/scrollspeed);
    }
    public void close(){
        open = false;
        scroll = 0;
        for (Option o : options) {
            o.update();
        }
        normal = new Sprite(normalDefault);
        normal.setCenter(center);
        hovered = new Sprite(hoveredDefault);
        hovered.setCenter(center);
        selectedOption.getNormalSprite().trim();
        selectedOption.getNormalSprite().drawCenteredOn(normal, 3.5*scale,0.5*scale);
        selectedOption.getNormalSprite().drawCenteredOn(hovered, 3.5*scale,0.5*scale);
        clicked = new Sprite(hovered);
        clicked.scale(0.99f);
        clicked.setCenter(center);
    }

    private Option getLongest(){
        int longest = 0;
        int longestIndex = 0;
        for (int i = 0; i < options.length; i++){
            if (options[i].getWidth() > longest){
                longest = options[i].getWidth();
                longestIndex = i;
            }
        }
        return options[longestIndex];
    }
    protected Sprite getSprite(){
        if (down){
            return clicked;
        }else if (!isColliding()){
            return normal;
        }else {
            return hovered;
        }
    }

    public double getTextfieldX(){
        return getX() + 18 * scale;
    }

    public double getTextfieldY(){
        return getY() + getHeight() + 2 * scale;
    }

    public double getBackgroundX(){
        return getX() + 13 * scale;
    }

    public double getBackgroundY(){
        return getY() + getHeight() + scale;
    }

    public class Option extends MenuElement{

        private String name;
        private Sprite hovered;
        private double y;
        private int index, value;

        public Option(String name, int i, int value) {
            super(new Coordinate());
            this.name = name;
            this.value = value;
            index = i;
            loadSprite();
            y = (getHeight() + 2 * optionPaddingY) * i + optionPaddingY + scale;
        }

        public void loadSprite(){
            normal = Text.get(name, scale * 0.65f);
            hovered = Text.getHovered(name,scale * 0.65f);
        }

        public void render(Graphics g) {
            getSprite().drawCenteredXOn(optionsTextfield, y - scroll * scrollspeed);
        }
        @Override
        public void update() {
            checkClick();
            getSprite().setPosition(optionsTextfield.getWidth()/2.0 - getWidth()/2.0, y - scroll * scrollspeed);
            getSprite().setOrigin(optionsTextfield.getPosition());
        }
        public Sprite getNormalSprite(){
            return normal;
        }

        protected Sprite getSprite(){
            return isColliding() ? hovered : normal;
        }

        public String getName(){
            return name;
        }

        @Override
        protected Rectangle getBounds() {
            return normal.getBounds();
        }

        @Override
        protected void doClick() {
            if(new Rectangle((int) getTextfieldX(), (int) getTextfieldY(), optionsBackground.getWidth(), optionsBackground.getHeight()).contains(Mouse.get().getX(), Mouse.get().getY())){
                selectedOption = this;
                settings[o].set(get());
                if(settings[o] == Settings.LANGUAGE) {
                    Menu.get().refresh();
                }
                if(settings[o] == Settings.FPS) {
                    Render.get().refreshFPS();
                }
                Settings.save();
                close();
            }
        }

        @Override
        public double getX() {
            return getNormalSprite().getX();
        }
        @Override
        public double getY() {
            return getNormalSprite().getY();
        }

        public int get(){
            return value;
        }
        public int getIndex(){
            return index;
        }
    }
}
