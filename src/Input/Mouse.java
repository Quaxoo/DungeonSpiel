package Input;

import Util.Coordinate;

import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {

    private static final Coordinate position = new Coordinate();
    private static final Set<Integer> buttonsDown = new HashSet<>();
    private static int scroll = 0;

    @Override
    public void mouseClicked(MouseEvent e) {

    }
    @Override
    public void mousePressed(MouseEvent e) {
        buttonsDown.add(e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttonsDown.remove(e.getButton());
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
       position.set(e.getX(), e.getY());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        scroll += e.getScrollAmount() * e.getWheelRotation();
    }

    public static Coordinate get() {
        return position;
    }

    public static  boolean isMouseDown(int button){
        return buttonsDown.contains(button);
    }

    public static void clear(){
        buttonsDown.clear();
    }

    public static int getScroll(){return scroll;}
    public static void setScroll(int s){scroll=s;}
    public static void clearScroll(){scroll=0;}

}
