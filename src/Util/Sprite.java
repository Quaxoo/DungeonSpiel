package Util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Sprite{

    public static String Tiles = "assets/level/tiles.png";
    public static String Darkness = "assets/effects/darkness.png";
    public static String ButtonSlice = "assets/menu/button/S.png";
    public static String ButtonSliceHovered = "assets/menu/button/SH.png";
    public static String ButtonCornerTL = "assets/menu/button/LO.png";
    public static String ButtonCornerTR = "assets/menu/button/RO.png";
    public static String ButtonCornerBL = "assets/menu/button/LU.png";
    public static String ButtonCornerBR = "assets/menu/button/RU.png";
    public static String ButtonCornerTLHovered = "assets/menu/button/LOH.png";
    public static String ButtonCornerTRHovered = "assets/menu/button/ROH.png";
    public static String ButtonCornerBLHovered = "assets/menu/button/LUH.png";
    public static String ButtonCornerBRHovered = "assets/menu/button/RUH.png";
    public static String Chars = "assets/menu/chars.png";
    public static String CharsHovered = "assets/menu/charsHovered.png";
    public static String CharsMarked = "assets/menu/charsMarked.png";
    public static String DamageNumberEnemy = "assets/effects/enemy.png";
    public static String DamageNumberPlayer = "assets/effects/player.png";
    public static String DamageNumberCrit = "assets/effects/crit.png";
    public static final String Arrow = "assets/ui/arrow.png";
    public static final String InventoryIcon = "assets/ui/inventory.png";
    public static final String UseIcon = "assets/ui/use.png";

    public static final String Slot = "assets/ui/slot.png";
    public static final String SlotUI = "assets/ui/slotUI.png";
    public static final String WeaponSlot = "assets/ui/weaponslot.png";
    public static final String Key = "assets/items/icons/key.png";
    public static final String KeyMissing = "assets/ui/keymissing.png";
    public static final String Damage = "assets/items/tag/damage.png";
    public static final String Crit = "assets/items/tag/crit.png";
    public static final String Speed = "assets/items/tag/speed.png";
    public static final String Health = "assets/items/tag/health.png";
    public static final String Vision = "assets/items/tag/vision.png";
    public static final String Time = "assets/items/tag/time.png";
    public static String Healthbar = "assets/ui/healthbar.png";
    public static String Redbar = "assets/ui/barRed.png";
    public static String Shadow = "assets/items/world/shadow.png";
    public static String ScrollbarLeft = "assets/menu/scrollbar/endeLinks.png";
    public static String ScrollbarRight = "assets/menu/scrollbar/endeRechts.png";
    public static String ScrollbarSlice= "assets/menu/scrollbar/slice.png";
    public static String ScrollbarButton = "assets/menu/scrollbar/button.png";
    public static String DropdownLeft = "assets/menu/dropdown/endeLinks.png";
    public static String DropdownLeftHovered = "assets/menu/dropdown/endeLinksHovered.png";
    public static String DropdownRight = "assets/menu/dropdown/endeRechts.png";
    public static String DropdownSlice= "assets/menu/dropdown/slice.png";
    public static String OptionsBackground = "assets/menu/dropdown/optionBackground.png";
    public static String OptionsCornerLinksUnten = "assets/menu/dropdown/optionCornerLU.png";
    public static String OptionsCornerLinksOben = "assets/menu/dropdown/optionCornerOL.png";
    public static String OptionsCornerRechtsOben = "assets/menu/dropdown/optionCornerRO.png";
    public static String OptionsCornerRechtsUnten = "assets/menu/dropdown/optionCornerRU.png";
    public static String OptionsSideLinks = "assets/menu/dropdown/optionSideLinks.png";
    public static String OptionsSideRechts = "assets/menu/dropdown/optionSideRechts.png";
    public static String OptionsSideOben = "assets/menu/dropdown/optionSideOben.png";
    public static String OptionsSideUnten = "assets/menu/dropdown/optionSideUnten.png";
    public static String MenuBackground = "assets/menu/background/background.png";
    public static String MenuCornerLinksUnten = "assets/menu/background/cornerLU.png";
    public static String MenuCornerLinksOben = "assets/menu/background/cornerLO.png";
    public static String MenuCornerRechtsOben = "assets/menu/background/cornerRO.png";
    public static String MenuCornerRechtsUnten = "assets/menu/background/cornerRU.png";
    public static String MenuSideLinks = "assets/menu/background/links.png";
    public static String MenuSideRechts = "assets/menu/background/rechts.png";
    public static String MenuSideOben = "assets/menu/background/oben.png";
    public static String MenuSideUnten = "assets/menu/background/unten.png";

    private BufferedImage sprite;
    private final Coordinate position = new Coordinate();
    private final Coordinate origin = new Coordinate();
    private final Rectangle bounds = new Rectangle();

    public Sprite() {
        sprite = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
    }
    public Sprite(BufferedImage image) {
        sprite = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        set(image);
    }
    public Sprite(Sprite sprite) {
        this.sprite = new BufferedImage(sprite.getWidth(), sprite.getHeight(), BufferedImage.TYPE_INT_ARGB);
        sprite.drawOn(this,0,0);
    }
    public Sprite(Sprite sprite, Coordinate p, Coordinate o) {
        this.sprite = new BufferedImage(sprite.getWidth(), sprite.getHeight(), BufferedImage.TYPE_INT_ARGB);
        sprite.drawOn(this,0,0);
        setPosition(p);
        setOrigin(o);
    }
    public Sprite(Sprite sprite, Coordinate p) {
        this.sprite = new BufferedImage(sprite.getWidth(), sprite.getHeight(), BufferedImage.TYPE_INT_ARGB);
        sprite.drawOn(this,0,0);
        setPosition(p);
    }
    public Sprite(Sprite sprite, double x, double y) {
        this.sprite = new BufferedImage(sprite.getWidth(), sprite.getHeight(), BufferedImage.TYPE_INT_ARGB);
        sprite.drawOn(this,0,0);
        setPosition(x,y);
    }
    public Sprite(String image) {
        load(image);
    }
    public Sprite(String image, float scale) {
        load(image);
        scale(scale);
    }
    public Sprite(String image, double width, double height) {
        load(image);
        scale((int) width, (int) height);
    }
    public Sprite(double width, double height) {
        sprite = new BufferedImage((int) width, (int) height,BufferedImage.TYPE_INT_ARGB);
    }

    public Sprite(Sprite sprite, float scale) {
        this.sprite = new BufferedImage(sprite.getWidth(), sprite.getHeight(), BufferedImage.TYPE_INT_ARGB);
        sprite.drawOn(this,0,0);
        scale(scale);
    }

    public BufferedImage get(){
        return sprite;
    }
    public BufferedImage getSubimage(double x, double y, double w, double h){
        return sprite.getSubimage((int) x, (int) y, (int) w, (int) h);
    }
    public void set(BufferedImage image){
        clear();
        getGraphics().drawImage(image,0,0,null);
    }
    public void setBounds(){
        bounds.setBounds((int) getX(), (int) getY(), getWidth(),getHeight());
    }

    public void load(String image){
        try {
            File datei = new File(Files.getPath() + image);
            sprite = ImageIO.read(datei);
        } catch(IOException e){
            try {
                InputStream inputStream = Sprite.class.getResourceAsStream( "/res/" + image);
                sprite = ImageIO.read(inputStream);
            }catch (IOException g) {
                System.out.println(e.getMessage() + " - ( " + Files.getPath() + image + " )");
            }catch (IllegalArgumentException f){
                System.out.println("Exception: " + f.getMessage() + " " + Files.getPath() + image);
            }
        }

    }

    public void draw(double x, double y, Graphics g){
        g.drawImage( sprite, (int) x, (int) y,null);
    }
    public void fillShapeWithThis(Shape s){
        getGraphics2D().fill(s);
    }
    public void draw(Graphics g){
        g.drawImage(sprite, (int) getX(), (int) getY(),null);
    }
    public void drawFrom(Coordinate origin, Graphics g){
        g.drawImage(sprite, (int) (getX() + origin.getX()), (int) (getY() + origin.getY()),null);
    }
    public void drawCenter(double x, double y, Graphics g){
        g.drawImage( sprite, (int) (x-getWidth()/2.0), (int) (y-getHeight()/2.0),null);
    }
    public void drawCenterX(double x, double y, Graphics g){
        g.drawImage( sprite, (int) (x - getWidth()/2.0), (int) (y),null);
    }
    public void drawCenterY(double x, double y, Graphics g){
        g.drawImage(sprite, (int) x, (int) (y-getHeight()/2.0),null);
    }
    public void drawBorder(Graphics g){
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }
    public void drawOn(Sprite s,double x, double y){
        draw(x,y,s.getGraphics());
    }
    public void drawCenteredOn(Sprite s){
        draw(s.getWidth()/2.0 - getWidth()/2.0,s.getHeight()/2.0 - getHeight()/2.0,s.getGraphics());
    }
    public void drawCentered(Graphics g){
        draw(getX() - getWidth()/2.0,getY() - getHeight()/2.0,g);
    }
    public void drawCenteredOn(Sprite s, double x, double y){
        draw(s.getWidth()/2.0 - getWidth()/2.0 + x,s.getHeight()/2.0 - getHeight()/2.0 + y,s.getGraphics());
    }
    public void drawCenteredXOn(Sprite s, double y){
        draw(s.getWidth()/2.0 - getWidth()/2.0,y,s.getGraphics());
    }
    public void drawCenteredXOn(Sprite s,double x, double y){
        draw(s.getWidth()/2.0 - getWidth()/2.0 + x,y,s.getGraphics());
    }
    public void drawCenteredYOn(Sprite s, double x){
        draw(x,s.getHeight()/2.0 - getHeight()/2.0,s.getGraphics());
    }
    public void drawTopLeftOn(Sprite s){
        drawOn(s, 0 , 0);
    }
    public void drawTopRightOn(Sprite s){
        drawOn(s, s.getWidth() - getWidth(), 0);
    }
    public void drawBottomLeftOn(Sprite s){
        drawOn(s, 0 , s.getHeight() - getHeight());
    }
    public void drawBottomRightOn(Sprite s){
        drawOn(s, s.getWidth() - getWidth(), s.getHeight() - getHeight());
    }
    public void drawCenteredTo(Sprite s, Graphics g){
        draw(s.getWidth()/2.0 - getWidth()/2.0,s.getHeight()/2.0 - getHeight()/2.0,g);
    }
    public void fillX(Sprite s, double relativeStart, double relativeEnd, double relativeY){
        for (double x = relativeStart; x < getWidth() - relativeEnd; x += s.getWidth()){
            s.drawOn(this, x, relativeY);
        }
    }
    public void fillY(Sprite s, double relativeStart, double relativeEnd, double relativeX){
        for (double y = relativeStart; y < getHeight() - relativeEnd; y += s.getHeight()){
            s.drawOn(this, relativeX, y);
        }
    }
    public void add(double x, double y){
        BufferedImage img = new BufferedImage((int) (getWidth() + x), (int) (getHeight() + y),sprite.getType());
        Graphics g = img.createGraphics();
        g.drawImage(sprite,0,0,null);
        g.dispose();
        sprite = img;
    }
    public void addFromBottom(double x, double y){
        BufferedImage img = new BufferedImage((int) (getWidth() + x), (int) (getHeight() + y),sprite.getType());
        Graphics g = img.createGraphics();
        g.drawImage(sprite,0, (int) y,null);
        g.dispose();
        sprite = img;
    }
    public void addFromCenter(double x, double y){
        BufferedImage img = new BufferedImage((int) (getWidth() + x), (int) (getHeight() + y),sprite.getType());
        Graphics g = img.createGraphics();
        g.drawImage(sprite, (int) ((img.getWidth() - getWidth())/2.0),(int) ((img.getHeight() - getHeight())/2.0),null);
        g.dispose();
        sprite = img;
    }
    public void addAtRightCenter(Sprite s, double gap){
        addFromCenter(0, Math.max(0, s.getHeight() - getHeight()));
        add(s.getWidth() + gap, 0);
        s.drawCenteredYOn(this, getWidth() - s.getWidth());
    }
    public void addAtRightBottom(Sprite s, double gap){
        addFromBottom(s.getWidth() + gap, Math.max(0, s.getHeight() - getHeight()));
        s.drawOn(this, getWidth() - s.getWidth(), getHeight() - s.getHeight());
    }
    public void addAtBottomCenter(Sprite s, double gap){
        addFromCenter(Math.max(0, s.getWidth() - getWidth()),0);
        add(0, s.getHeight() + gap);
        s.drawCenteredXOn(this, getHeight() - s.getHeight());
    }
    public void addAtBottom(Sprite s, double gap){
        add(Math.max(0, s.getWidth() - getWidth()), s.getHeight() + gap);
        s.drawOn(this,0, getHeight() - s.getHeight());
    }
    public int getHeight(){
        return sprite.getHeight();
    }
    public int getWidth(){
        return sprite.getWidth();
    }
    public double getOriginX(){
        return origin.getX();
    }
    public double getOriginY(){
        return origin.getY();
    }
    public Coordinate getOrigin(){
        return origin;
    }
    public void setOrigin(Coordinate o){
        origin.set(o);
        setBounds();
    }
    public void setOrigin(double x, double y){
        origin.set(x,y);
        setBounds();
    }
    public void setOriginX(double x) {
        origin.setX(x);
        setBounds();
    }
    public void setOriginY(double y) {
        origin.setY(y);
        setBounds();
    }
    public double getX(){
        return position.getX() + getOriginX();
    }
    public double getY(){
        return position.getY() + getOriginY();
    }
    public Coordinate getPosition(){
        return position;
    }

    public void moveY(double y){
        position.changeY(y);
    }
    public void setPosition(Coordinate p){
        position.set(p);
        setBounds();
    }
    public void setPositionX(double x){
        position.setX(x);
        setBounds();
    }
    public void setPositionY(double y){
        position.setY(y);
        setBounds();
    }
    public void setPosition(double x, double y){
        position.set(x,y);
        setBounds();
    }
    public double getCenterX(){
        return getX() + getWidth()/2.0;
    }
    public double getCenterY(){
        return getY() + getHeight()/2.0;
    }
    public Coordinate getCenter(){
        return new Coordinate(getCenterX(), getCenterY());
    }
    public void setCenter(Coordinate c){
        setPosition(new Coordinate(c.getX() - getWidth()/2.0, c.getY() - getHeight()/2.0));
    }
    public void setCenter(double x, double y) {
        setPosition(x - getWidth()/2.0, y - getHeight()/2.0);
    }
    public void setCenterX(double x){
        position.setX(x - getWidth()/2.0);
        setBounds();
    }
    public void setCenterY(double y){
        position.setY(y - getHeight()/2.0);
        setBounds();
    }

    public Rectangle getBounds(){
        return bounds;
    }
    public Rectangle getBounds(double x, double y){
        return new Rectangle((int) (getX() + x), (int) (getY() + y), getWidth(), getHeight());
    }
    public Graphics getGraphics(){
        return sprite.getGraphics();
    }
    public Graphics2D getGraphics2D(){
        return (Graphics2D) sprite.getGraphics();
    }
    public void clear(){
        sprite = new BufferedImage(sprite.getWidth(),sprite.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }
    public void cut(double x1, double y1, double x2, double y2){
        sprite = getSubimage(x1, y1,getWidth() - x2 - x1,getHeight() - y2 - y1);
        setBounds();
    }
    public void cutX(double x1, double x2){
        cut(x1,0,x2,0);
    }
    public void trim(){
        trimLeft();
        trimRight();
        trimUp();
        trimDown();
    }
    public void trimLeft(){
        int x1=0,x2=0,y1=0,y2=0;
        boolean Break = false;
        for (int x = 0; x < getWidth(); x++){
            for (int y = 0; y < getHeight(); y++){
                if (((sprite.getRGB(x, y) >> 24) & 0xFF) != 0){
                    x1 = x;
                    Break = true;
                    break;
                }
            }
            if (Break){
                break;
            }
        }
        cut(x1, y1, x2, y2);
    }
    public void trimRight(){
        int x1=0,x2=0,y1=0,y2=0;
        boolean Break = false;
        for (int x = getWidth()-1; x > 0; x--){
            for (int y = 0; y < getHeight(); y++){
                if (((sprite.getRGB(x, y) >> 24) & 0xFF) != 0){
                    x2 = getWidth() - x - 1;
                    Break = true;
                    break;
                }
            }
            if (Break){
                break;
            }
        }
        cut(x1, y1, x2, y2);
    }
    public void trimUp(){
        int x1=0,x2=0,y1=0,y2=0;
        boolean Break = false;
        for (int y = 0; y < getHeight(); y++){
            for (int x = 0; x < getWidth(); x++){
                if (((sprite.getRGB(x, y) >> 24) & 0xFF) != 0){
                    y1 = y;
                    Break = true;
                    break;
                }
            }
            if (Break){
                break;
            }
        }
        cut(x1, y1, x2, y2);
    }
    public void trimDown(){
        int x1=0,x2=0,y1=0,y2=0;
        boolean Break = false;
        for (int y = getHeight()-1; y > 0; y--){
            for (int x = 0; x < getWidth(); x++){
                if (((sprite.getRGB(x, y) >> 24) & 0xFF) != 0){
                    y2 = getHeight() - y - 1;
                    Break = true;
                    break;
                }
            }
            if (Break){
                break;
            }
        }
        cut(x1, y1, x2, y2);
    }

    public void scale(double width, double height) {
        try{
            BufferedImage scaledImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = scaledImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g.drawImage( sprite, 0, 0, (int) width, (int) height, null);
            g.dispose();
            sprite = scaledImage;
            setBounds();
        }catch (Exception e){
            System.out.println();
        }
    }
    public void scaleY(double height) {
        BufferedImage scaledImage = new BufferedImage((int) (sprite.getWidth() * height/sprite.getHeight()), (int) height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaledImage.createGraphics();
        g.drawImage( sprite, 0, 0, (int) (sprite.getWidth() * height/sprite.getHeight()), (int) height, null);
        g.dispose();
        sprite = scaledImage;
        setBounds();
    }
    public void scale(float scale) {
        scale(getWidth()*scale, getHeight()*scale);
    }

    public void alpha(float value){
        AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, value);
        BufferedImage New = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = New.createGraphics();
        g.setComposite(alpha);
        g.drawImage(sprite, 0, 0, null);
        g.dispose();
        sprite = New;
    }

    public void rotate(float angle){
        double radians = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int width = sprite.getWidth();
        int height = sprite.getHeight();
        int newWidth = (int) Math.round(width * cos + height * sin);
        int newHeight = (int) Math.round(height * cos + width * sin);

        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, sprite.getType());
        Graphics2D g2d = rotatedImage.createGraphics();
        AffineTransform transform = new AffineTransform();
        transform.translate((double) (newWidth - width) / 2, (double) (newHeight - height) / 2);
        transform.rotate(radians, (double) width / 2, (double) height / 2);
        g2d.setTransform(transform);
        draw(0,0,g2d);
        g2d.dispose();
        sprite = rotatedImage;
    }


}
