package States;

public enum Direction {

    Right(0, 1, 0),
    Down(1, 0,1),
    Left(2, -1, 0),
    Up(3, 0, -1);


    Direction(int animationIndex, int xDirection, int yDirection){
        this.animationIndex = animationIndex;
        this.xDirection = xDirection;
        this.yDirection = yDirection;
    }
    private final int animationIndex;
    private final int xDirection;
    private final int yDirection;

    public int getIndex() {
        return animationIndex;
    }
    public int getX() {
        return xDirection;
    }
    public int getY() {
        return yDirection;
    }


}
