import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GeneralGameInformation {

    private final List<Piece> owns = new ArrayList<>();
    private final List<Piece> npcs = new ArrayList<>();
    private final List<Piece> enemies = new ArrayList<>();
    private final GameMap gameMap;
    private final int myID;

    private GeneralGameInformation(GameMap gameMap, int myID) {
        this.gameMap = gameMap;
        this.myID = myID;
    }

    static GeneralGameInformation fromGeneralGameInformation(GameMap gameMap, int myID) {
        GeneralGameInformation info = new GeneralGameInformation(gameMap, myID);
        for (int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {
                Piece piece = Piece.fromXY(x, y, gameMap);
                if (piece.getOwner() == 0) info.addToNpcs(piece);
                else if (piece.getOwner() == myID) info.addToOwn(piece);
                else info.addToEnemies(piece);
            }

        }

        return info;
    }

    List<Piece> getOwnPiecesFromOuterToInner(int myID) {
        List<Piece> retPieces = new ArrayList<>();
        for (Piece piece : this.owns) {
            if (piece.hasXOtherNeighbors(myID, 4)) {
                retPieces.add(piece);
            }
        }
        for (Piece piece : this.owns) {
            if (piece.hasXOtherNeighbors(myID, 3)) {
                retPieces.add(piece);
            }
        }
        for (Piece piece : this.owns) {
            if (piece.hasXOtherNeighbors(myID, 2)) {
                retPieces.add(piece);
            }
        }
        for (Piece piece : this.owns) {
            if (piece.hasXOtherNeighbors(myID, 1)) {
                retPieces.add(piece);
            }
        }
        for (Piece piece : this.owns) {
            if (piece.hasXOtherNeighbors(myID, 0)) {
                retPieces.add(piece);
            }
        }

        return retPieces;
    }

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

    List<Piece> getNpcsAndEnemies() {
        List<Piece> npcsAndEnemies = new ArrayList<>();
        npcsAndEnemies.addAll(getNpcs());
        npcsAndEnemies.addAll(getEnemies());
        return npcsAndEnemies;
    }


    int getTopNPercentEnemyProduction(int percent) {
        List<Integer> productions = new ArrayList<>();

        for (Piece piece : getNpcsAndEnemies()) {
            productions.add(piece.getProduction());
        }

        Comparator<Integer> natural = Comparator.<Integer>naturalOrder();
        productions.sort(natural);
        int n = (int) ((double) percent / 100 * productions.size());

        return productions.get(n);
    }

    void setNextPieceToConquer(Piece conquerFromHere) {
        if(MyBot.nextPieceToConquer.getOwner() != myID && MyBot.findNextPieceToConquerInXFrames == 0) {
            MyBot.nextPieceToConquer = findMostValuablePiece(getNpcsAndEnemies(), conquerFromHere);
            MyBot.findNextPieceToConquerInXFrames = 10;
        }
    }

    private Piece findMostValuablePiece(List<Piece> npcsAndEnemies, Piece startPiece) {

        int firstProductionGoal = getTopNPercentEnemyProduction(97);
        return findMostValuablePieceWitdhProduction(npcsAndEnemies, startPiece, firstProductionGoal);
    }


    private Piece findMostValuablePieceWitdhProduction(List<Piece> npcsAndEnemies, Piece startPiece, int production) {

        return npcsAndEnemies.stream()
                .filter(p -> p.getProduction() >= production)
                .reduce(new NullPiece(), (p1, p2) -> {
                    if (!p1.isNil()
                            && gameMap.getDistance(startPiece.getLoc(), p1.getLoc()) < gameMap.getDistance(startPiece.getLoc(), p2.getLoc()))
                        return p1;
                    else return p2;
                });
    }
}
