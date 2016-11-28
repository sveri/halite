import java.util.Comparator;

/**
 * Created by sveri on 25.11.16.
 */
public class PieceDistanceSorter implements Comparator<Piece> {

    private final Piece fromPiece;
    private final GameMap gameMap;

    public PieceDistanceSorter(Piece fromPiece, GameMap gameMap) {
        this.fromPiece = fromPiece;
        this.gameMap = gameMap;
    }

    @Override
    public int compare(Piece o1, Piece o2) {
        double fromToP1 = gameMap.getDistance(fromPiece.getLocation(), o1.getLocation());
        double fromToP2 = gameMap.getDistance(fromPiece.getLocation(), o2.getLocation());

        if(fromToP1 == fromToP2) return 0;
        if(fromToP1 < fromToP2) return -1;

        return 1;
    }
}
