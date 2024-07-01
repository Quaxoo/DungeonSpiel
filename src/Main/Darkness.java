package Main;

import Main.Thread.Render;
import Main.Thread.Update;
import States.Constants;
import Util.Drawable;
import Util.Sprite;
import Util.Updateable;

import java.awt.*;

public class Darkness implements Drawable, Updateable {
    private Sprite effect;
    private int duration,state;
    private static final int Increase = 1, Decrease = 2, Normal = 0;

    private float scale, scaleDif;
    private static Darkness darkness;
    private static boolean on = true;

    public Darkness(){
        load();
        darkness = this;
        Render.add(Render.Darkness, this);
        Update.add(Update.Game, this);
    }
    public void load(){
        effect = new Sprite(Sprite.Darkness, Constants.globalScale - 0.2f);
    }
    @Override
    public void render(Graphics g) {
        if(on){
            effect.drawCenter(GameFrame.get().getCenterX(),GameFrame.get().getCenterY(),g);
        }
    }



    public static Darkness get(){
        return darkness;
    }

    @Override
    public void update() {

        if(duration > 0){
            duration--;
        }else if(scale != 1){
            reset();
        }
    }

    public void changeSize(int duration, float scale){
        this.duration = duration * Game.UPS;
        this.scale = 1 + scale;
        effect.scale(this.scale);
        state = Increase;
    }

    public void reset(){
        effect.scale(1/scale);
        scale = 1;
    }
}