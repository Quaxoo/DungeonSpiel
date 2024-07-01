package Util;

import Input.Keyboard;

public class KeyTypedListener {
    private Executable e;
    private int key;
    private boolean down;

    public KeyTypedListener(Executable e, int key){
        this.e = e;
        this.key = key;
    }

    public void check(){
        if(Keyboard.isKeyDown(key) && !down){
            down = true;
            e.execute();
        }else if(!Keyboard.isKeyDown(key)){
            down = false;
        }
    }


}
