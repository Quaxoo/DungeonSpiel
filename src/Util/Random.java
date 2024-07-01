package Util;

public abstract class Random {
    public static int get(double min, double max){
        return (int) (Math.floor( Math.random() * ( max - min + 1 )) + min);
    }
}
