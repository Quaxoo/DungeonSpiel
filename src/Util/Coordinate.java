package Util;

public class Coordinate {

    private double x,y;
     public Coordinate(double x, double y){
         this.x = x;
         this.y = y;
     }
     public Coordinate(Coordinate k){
        this.x = k.getX();
        this.y = k.getY();
    }
    public Coordinate(){
        this.x = 0;
        this.y = 0;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Coordinate changeX(double x) {
        this.x += x;
        return this;
    }
    public Coordinate changeY(double y) {
        this.y += y;
        return this;
    }

    public void set(double x, double y){
        setX(x);
        setY(y);
    }
    public void set(Coordinate k){
        set(k.getX(),k.getY());
    }
    public Coordinate change(double x, double y){
        changeX(x);
        changeY(y);
        return this;
    }

    public double getDistance(Coordinate k){
        return Math.sqrt( Math.pow( k.getX()-getX() ,2) + Math.pow( k.getY() - getY() ,2) );
    }

    public boolean equals(Coordinate k){
         return k.getX() == getX() && k.getY() == getY();
    }

    public void print(){
         System.out.println("( x: " + getX() + " | y: " + getY() + " )");
    }
}
