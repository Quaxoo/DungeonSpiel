package States;

public enum State {

    Standing(0),
    Walking(4),
    MeleeAttack(8),
    RangedAttack(12),
    Dying(16),
    Spawning(20);

    State(int animationsIndex){
        this.animationsIndex = animationsIndex;
    }
    private final int animationsIndex;

    public int getIndex() {
        return animationsIndex;
    }

    public static State get(int index){
        return switch (index){
            case 0,1,2,3  -> Standing;
            case 4,5,6,7  -> Walking;
            case 8,9,10,11  -> RangedAttack;
            case 12,13,14,15  -> MeleeAttack;
            default -> null;
        };
    }

}
