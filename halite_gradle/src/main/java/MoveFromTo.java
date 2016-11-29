import java.util.Comparator;

/**
 * Created by sveri on 29.11.16.
 */
public class MoveFromTo{
    private final Piece from;
    private final Piece to;

    public Direction getNextDirection() {
        return nextDirection;
    }

    private final Direction nextDirection;

    public int getMoveValue() {
        return moveValue;
    }

    private final int moveValue;

    public MoveFromTo(Piece from, Piece to, Direction nextDirection, int moveValue) {
        this.from = from;
        this.to = to;
        this.nextDirection = nextDirection;
        this.moveValue = moveValue;
    }

    @Override
    public String toString() {
        return "MoveFromTo{" +
                "from=" + from +
                ", to=" + to +
                ", nextDirection=" + nextDirection +
                ", moveValue=" + moveValue +
                '}';
    }
}
