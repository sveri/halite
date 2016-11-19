import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    private void addToEnemies(Piece piece) {
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

//    void setNextPieceToConquer(Piece conquerFromHere) {
//        if (Seventeenth.nextPieceToConquer.isNil()) {
//            Seventeenth.nextPieceToConquer = findMostValuablePiece(getNpcsAndEnemies(), conquerFromHere);
//            Seventeenth.logger.info("setting next piece to conquer to: " + Seventeenth.nextPieceToConquer);
//        }
//    }
//
//
//    private int getTopNPercentEnemyProduction(int percent) {
//        List<Integer> productions = new ArrayList<>();
//
//        for (Piece piece : getNpcsAndEnemies()) {
//            productions.add(piece.getProduction());
//        }
//
//        Comparator<Integer> natural = Comparator.<Integer>naturalOrder();
//        productions.sort(natural);
//        int n = (int) ((double) percent / 100 * productions.size());
//
//        return productions.get(n);
//    }
//
//    private Piece findMostValuablePiece(List<Piece> npcsAndEnemies, Piece startPiece) {
//        boolean searchNextPiece = true;
//        int startPercent = 99;
//        int productionGoal = getTopNPercentEnemyProduction(startPercent);
//
//
////        Collections.sort(npcsAndEnemies, new PieceDistanceComparator(gameMap, startPiece));
//        List<Piece> filteredPieces = npcsAndEnemies.stream()
//                .sorted(new PieceDistanceComparator(gameMap, startPiece))
//                .filter(p -> gameMap.getDistance(startPiece.getLoc(), p.getLoc()) < 4)
//                .collect(Collectors.toList());
//
//        while(searchNextPiece) {
//            for (Piece filteredPiece : filteredPieces) {
//                if(filteredPiece.getProduction() >= productionGoal) return filteredPiece;
//            }
//            startPercent = startPercent - 5;
//            productionGoal = getTopNPercentEnemyProduction(startPercent);
//            if(startPercent < 70) searchNextPiece = false;
//        }
//
////        int firstProductionGoal = getTopNPercentEnemyProduction(98);
////        return findMostValuablePieceWitdhProduction(npcsAndEnemies, startPiece, firstProductionGoal);
//        return new NullPiece();
//    }
//
//
//    private Piece findMostValuablePieceWitdhProduction(List<Piece> npcsAndEnemies, Piece startPiece, int production) {
//
//        return npcsAndEnemies.stream()
//                .filter(p -> p.getProduction() >= production)
//                .reduce(new NullPiece(), (p1, p2) -> {
//                    if (!p1.isNil()
//                            && gameMap.getDistance(startPiece.getLoc(), p1.getLoc()) < gameMap.getDistance(startPiece.getLoc(), p2.getLoc()))
//                        return p1;
//                    else return p2;
//                });
//    }

//    public List<Piece> getXPiecesClosestToToConquerPiece(Piece nextPieceToConquer) {
//        List<Piece> retPieces = new ArrayList<>();
//        for (Piece own : getOwns()) {
//            if (retPieces.size() < Seventeenth.soManyPiecesMustConquer) {
//                retPieces.add(own);
//            } else {
//
//                Piece longestAway = retPieces.stream().reduce(new NullPiece(), (p1, p2) -> {
//                    if (gameMap.getDistance(nextPieceToConquer.getLoc(), p1.getLoc()) <
//                            gameMap.getDistance(nextPieceToConquer.getLoc(), p2.getLoc()))
//                        return p2;
//                    else return p1;
//                });
//
//                if(gameMap.getDistance(nextPieceToConquer.getLoc(), longestAway.getLoc())
//                        > gameMap.getDistance(nextPieceToConquer.getLoc(), own.getLoc())){
//                    retPieces.remove(longestAway);
//                    retPieces.add(own);
//                }
//            }
//        }
//
//        return retPieces;
//    }
}
