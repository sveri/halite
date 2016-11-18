import java.util.Comparator;

class PieceDistanceComparator implements Comparator<Piece> {

    private final GameMap gameMap;
    private final Piece startingPiece;

    public PieceDistanceComparator(GameMap gameMap, Piece startingPiece) {
        this.gameMap = gameMap;
        this.startingPiece = startingPiece;
    }

    @Override
    public int compare(Piece p1, Piece p2) {
        int fromStartToP1 = (int) gameMap.getDistance(startingPiece.getLoc(), p1.getLoc());
        int fromStartToP2 = (int) gameMap.getDistance(startingPiece.getLoc(), p2.getLoc());

        if(fromStartToP1 == fromStartToP2) return 0;
        if(fromStartToP1 < fromStartToP2) return -1;
        return 1;
    }
}
