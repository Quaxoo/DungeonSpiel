package Util;

import States.Settings;

public class Setting {
    private String name;
    private int value;
    private int standard;
    public Setting(String name, int standard){
        this.name = name;
        this.value = -1;
        this.standard = standard;
        Settings.add(this);
    }
    public void set(int value){
        this.value = value;
    }
    public void reset(){
        this.value = standard;
    }
    public int get(){
        return value;
    }
    public String getName(){
        return name;
    }

}