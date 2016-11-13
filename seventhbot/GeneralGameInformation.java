import java.util.ArrayList;
import java.util.List;

public class GeneralGameInformation {

    private final List<Piece> owns = new ArrayList<>();
    private final List<Piece> npcs = new ArrayList<>();
    private final List<Piece> enemies = new ArrayList<>();

    public List<Piece> getOwns() {
        return owns;
    }

    public List<Piece> getNpcs() {
        return npcs;
    }

    public List<Piece> getEnemies() {
        return enemies;
    }

    void addToEnemies(Piece piece) {
        enemies.add(piece);
    }

    void addToNpcs(Piece piece) {
        npcs.add(piece);
    }

    void addToOwn(Piece piece) {
        owns.add(piece);
    }

    static Piece findNextFreePieceWithHighestProduction(GameMap gameMap, int myID) {
        Piece highPiece = findNextFreePieceWithProductionX(gameMap, myID, 6);
        if(highPiece != null) return highPiece;
        highPiece = findNextFreePieceWithProductionX(gameMap, myID, 5);
        if(highPiece != null) return highPiece;
        highPiece = findNextFreePieceWithProductionX(gameMap, myID, 5);
        if(highPiece != null) return highPiece;
        highPiece = findNextFreePieceWithProductionX(gameMap, myID, 4);
        if(highPiece != null) return highPiece;
        highPiece = findNextFreePieceWithProductionX(gameMap, myID, 3);
        if(highPiece != null) return highPiece;
        highPiece = findNextFreePieceWithProductionX(gameMap, myID, 2);
        if(highPiece != null) return highPiece;
        return findNextFreePieceWithProductionX(gameMap, myID, 1);

    }

    private static Piece findNextFreePieceWithProductionX(GameMap gameMap, int myID, int production) {
        for (int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {
                Location curLoc = new Location(x, y);
                Site curSite = gameMap.getSite(curLoc);
                if (curSite.owner != myID && curSite.production == production) return new Piece(curLoc, curSite, gameMap);
            }
        }
        return null;
    }

    List<Piece> getOwnPiecesFromOuterToInner(int myID) {
        List<Piece> retPieces = new ArrayList<>();
        for (Piece piece : this.owns) {
            if(piece.hasNOtherNeigbhors(myID, 4)) {
                retPieces.add(piece);
            }
        }
        for (Piece piece : this.owns) {
            if(piece.hasNOtherNeigbhors(myID, 3)) {
                retPieces.add(piece);
            }
        }
        for (Piece piece : this.owns) {
            if(piece.hasNOtherNeigbhors(myID, 2)) {
                retPieces.add(piece);
            }
        }
        for (Piece piece : this.owns) {
            if(piece.hasNOtherNeigbhors(myID, 1)) {
                retPieces.add(piece);
            }
        }
        for (Piece piece : this.owns) {
            if(piece.hasNOtherNeigbhors(myID, 0)) {
                retPieces.add(piece);
            }
        }

        return retPieces;
    }
}
