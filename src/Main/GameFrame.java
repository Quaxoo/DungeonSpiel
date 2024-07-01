package Main;

import Input.Keyboard;
import Input.Mouse;
import States.Settings;
import Util.Coordinate;
import Util.Files;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameFrame extends JFrame {

    private static GameFrame gameFrame;
    private boolean focused = true;
    private int vsync;

    public GameFrame(){
        super("Dungeon Spiel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //add(GamePanal.get());

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        //pack();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new Keyboard());
        setLocationRelativeTo(null);
        setResizable(false);
        ImageIcon icon = new ImageIcon(Files.getPath() + "assets/logo.png");
        setIconImage(icon.getImage());
        setupListener();

        getContentPane().add(GamePanal.get());

        setVisible(true);
        Settings.ScreenSize = new Dimension(getWidth(), getHeight());
        vsync = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate();
        gameFrame = this;
    }

    public boolean isOpen(){
        return true;
    }

    public int getVsync(){
        return vsync;
    }

    private void setupListener(){
        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                focused = true;
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                focused = false;
                Mouse.clear();
                Keyboard.clear();

            }
        });
    }

    public int getCenterX(){
        return getWidth()/2;
    }
    public int getCenterY(){
        return getHeight()/2;
    }

    public Coordinate getCenter(){
        return new Coordinate(getCenterX(),getCenterY());
    }

    public static GameFrame get(){
        return gameFrame;
    }

}