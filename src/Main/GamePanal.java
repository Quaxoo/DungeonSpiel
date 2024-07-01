package Main;

import Input.Keyboard;
import Input.Mouse;
import Main.Thread.Render;
import Util.Sprite;

import javax.swing.*;
import java.awt.*;

public class GamePanal extends JPanel {

    private static GamePanal gamePanal;
    private Sprite startingLogo;

    public GamePanal(){
        super();
        setOpaque(false);
        setLayout(new BorderLayout());
        setupInputs();
        startingLogo = new Sprite("assets/logo.png");
        repaint();
        gamePanal = this;
    }

    private void setupInputs(){
        Mouse mouse = new Mouse();
        addKeyListener(new Keyboard());
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
        addMouseWheelListener(mouse);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(Game.paused()){
            startingLogo.drawCenter(GameFrame.get().getCenterX(), GameFrame.get().getCenterY(), g);
        }else{
            Render.renderLevels(g);
        }
    }

    public static GamePanal get(){
        return gamePanal;
    }
}