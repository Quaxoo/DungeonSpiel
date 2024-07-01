package Main;

import Main.Level.Level;
import Main.Thread.Render;
import Main.Thread.Update;
import States.Constants;
import Util.Coordinate;
import Util.Drawable;
import Util.Sprite;
import Util.Updateable;

import java.awt.*;


public class Damagenumber implements Drawable, Updateable {

    public static int Enemy = 0;

    public static int Player = 1;
    public static int Crit = 2;
    private final Coordinate position;
    private final int type;
    private static final Sprite[] enemy = new Sprite[10], player = new Sprite[10], crit = new Sprite[10];
    private int lifeduration = 120;
    private Sprite sprite;
    private static final float scale = 4.2f * Constants.globalScale;

    private static final int scaledDimension = (int) (7 * scale);
    private static final int y = (int) (8 * scale);
    private final float speed = 0.04f*scale;
    private float alpha = 1.0f;
    private double offsetX;
    private final double offsetY = -3*scale;
    public Damagenumber(double x, double y, float damage, int type){
        this.type = type;
        this.position = new Coordinate(x,y);
        setSprite(damage);
        Render.add(Render.Effect,this);
        Update.add(Update.Game,this);
    }
    private void setSprite(float damage){
        int number = (int)damage;
        String z = String.valueOf(number);
        sprite = new Sprite(z.length()*scaledDimension, y);
        for(int x = 0;x < z.length(); x++){
            int digit = Character.getNumericValue(z.charAt(x));
            getArray()[digit].drawOn(sprite,(x*(scaledDimension-2*scale)), 0);
        }
        offsetX = (double) (-z.length() * scaledDimension) /2;
    }
    public static void load(){
        int l = 10;
        Sprite s = new Sprite(Sprite.DamageNumberEnemy, l * scaledDimension, y);
        for (int x = 0; x < l; x++){
            enemy[x] = new Sprite(s.getSubimage(x * scaledDimension, 0, scaledDimension, y));
        }
        s = new Sprite(Sprite.DamageNumberPlayer,l * scaledDimension, y);
        for (int x = 0; x < l; x++){
            player[x] = new Sprite(s.getSubimage(x * scaledDimension, 0, scaledDimension, y));
        }
        s = new Sprite(Sprite.DamageNumberCrit, l * scaledDimension, y);
        for (int x = 0; x < l; x++){
            crit[x] = new Sprite(s.getSubimage(x * scaledDimension, 0, scaledDimension, y));
        }
    }

    @Override
    public void render(Graphics g) {
        sprite.draw(
                (int) (position.getX()+ Level.origin.getX()+offsetX),
                (int) (position.getY() + Level.origin.getY()+offsetY),
                g
        );
    }

    @Override
    public void update() {
        position.changeY(-speed);
        sprite.alpha(alpha);
        if(alpha >= 0.0005){
            alpha -= 0.0005;
        }
        lifeduration--;
        if (lifeduration <= 0){
            Render.remove(Render.Effect,this);
            Update.remove(Update.Game,this);
        }
    }

    private Sprite[] getArray(){
        switch (type){
            case 1 -> {return player;}
            case 2 -> {return crit;}
            default -> {return enemy;}
        }
    }
}

