package Input;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class Keyboard implements KeyEventDispatcher, KeyListener {

    private static final Set<Integer> pressedKeys = new HashSet<>();

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());

    }

    public static boolean isKeyDown(int key){
        return pressedKeys.contains(key);
    }

    public static void clear(){
        pressedKeys.clear();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if(e.getID() == KeyEvent.KEY_PRESSED) {
            keyPressed(e);
        }else if(e.getID() == KeyEvent.KEY_RELEASED){
            keyReleased(e);
        }
        return false;
    }
}
