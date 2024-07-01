package States;

public enum Gamestate {
    PLAYING(0), PAUSED(1);

    Gamestate(int index){
        this.index = index;
    }

    private final int index;

    public int getIndex(){
        return index;
    }

}
