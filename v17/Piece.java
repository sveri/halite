import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Piece {

    final private Location loc;

    final private Site site;

    private final GameMap gameMap;

    Piece(Location loc, Site site, GameMap gameMap) {
        this.loc = loc;
        this.site = site;
        this.gameMap = gameMap;
    }

    static Piece fromXY(int x, int y, GameMap gameMap) {
        Location loc = new Location(x, y);
        return new Piece(loc, gameMap.getSite(loc), gameMap);
    }

//    static Piece fromLocation(Location newLoc, GameMap gameMap) {
//        return new Piece(newLoc, gameMap.getSite(newLoc), gameMap);
//    }

    static Piece fromLocationAndDirection(Location loc, Direction direction, GameMap gameMap) {
        Site site = gameMap.getSite(loc, direction);
        return new Piece(gameMap.getLocation(loc, direction), site, gameMap);
    }

//    static Piece fromLocationAndDirection(Site site, GameMap gameMap) {;
//        return new Piece(, site, gameMap);
//    }



    public Direction findNextDirection(int myID, GeneralGameInformation gameInformation) {
        if (!hasOnlyOwnNeighbors(myID)) {
            List<Piece> npcNeighbors = getNpcNeighbors();
            Piece retPiece = npcNeighbors.stream()
                    .filter(piece -> piece.getStrength() < getStrength())
                    .max(new PieceProductionComparator()).orElse(new NullPiece());

            if(!retPiece.isNil())
                return gameMap.getFromPieceToPiece(this, retPiece);
        } else if(moveAccordingToOwnStrength()){
            List<Piece> pieces = gameInformation.getOwns().stream()
                    .filter(piece -> !piece.hasOnlyOwnNeighbors(myID))
                    .sorted(new PieceDistanceComparator(gameMap, this)).collect(Collectors.toList());

            int shortestDistance = (int) gameMap.getDistance(this.getLoc(), pieces.get(0).getLoc());

            Piece bestPiece = pieces.stream().filter(piece -> gameMap.getDistance(this.getLoc(), piece.getLoc()) == shortestDistance)
                    .max(new PieceProductionComparator()).orElse(new NullPiece());

            if(!bestPiece.isNil())
                return gameMap.getFromPieceToPiece(this, bestPiece);
        }

        return Direction.STILL;
    }


//    Direction findNextDirectionForClosestPiece() {
//        Direction direction = gameMap.getFromPieceToPiece(this, Seventeenth.nextPieceToConquer);
//        Piece pieceTo = Piece.fromLocationAndDirection(getLoc(), direction, gameMap);
//
//        if(pieceTo.getStrength() < getStrength())
//            return direction;
//
//        return Direction.STILL;
//    }

    // v11

//    Direction findNextDirection(int myID, GeneralGameInformation gameInformation) {
//        Direction retDirection = Direction.STILL;
//
//
//        // v16
//
//        Enemy enemy = new Enemy(gameMap, myID);
//
//        if (enemy.hasDirectEnemyNeighbor(getLoc())) {
//            Piece nextEnemy = enemy.getEnemyNeighbor(this);
//            if (!nextEnemy.isNil()) {
//                retDirection = gameMap.getFromPieceToPiece(this, nextEnemy);
//            } else retDirection = Direction.STILL;
//
//        } else if (hasOnlyOwnNeighbors(myID) && moveAccordingToOwnStrength()) {
//            Piece nextNpc = findClosestNonOwnPiece(gameInformation.getNpcsAndEnemies());
//
//            if (nextNpc != null && moveAccordingToOwnStrength()) {
//                retDirection = gameMap.getDirectionFromTo(loc, nextNpc.getLoc(), gameMap.height, gameMap.width);
//            }
//        } else {
//            List<Piece> nonOwnNeighbors = getAllNonOwnNeighbors(myID);
//            int tempProd = -1;
//
//            for (Piece nonOwnNeighbor : nonOwnNeighbors) {
//                if(nonOwnNeighbor.getStrength() < getStrength() && nonOwnNeighbor.getProduction() > tempProd) {
//                    retDirection = gameMap.getDirectionFromTo(getLoc(), nonOwnNeighbor.getLoc(), gameMap.height, gameMap.width);
//                    tempProd = nonOwnNeighbor.getProduction();
//                }
//            }
//        }
//
//
////        int nextPieceToConquerOwner = gameMap.getSite(Seventeenth.nextPieceToConquer.getLoc()).owner;
//
////        if (nextPieceToConquerOwner != myID && moveAccordingToOwnStrength()) {
////
////            Piece nextPiece = Piece.fromLocationAndDirection(getLoc(), gameMap.getFromPieceToPiece(this, Seventeenth.nextPieceToConquer), gameMap);
////            if (nextPiece.getStrength() < getStrength())
////                retDirection = gameMap.getFromPieceToPiece(this, Seventeenth.nextPieceToConquer);
////        } else
//
////        if (hasOnlyOwnNeighbors(myID)) {
////
////            Piece nextNpc = findClosestNonOwnPiece(gameInformation.getNpcsAndEnemies());
////            if (!nextNpc.isNil() && moveAccordingToOwnStrength()) {
////                retDirection = gameMap.getDirectionFromTo(loc, nextNpc.getLoc(), gameMap.height, gameMap.width);
////            }
////        } else {
////            List<Piece> nonOwnNeighbors = getAllNonOwnNeighbors(myID);
////            int tempProd = -1;
////
////            for (Piece nonOwnNeighbor : nonOwnNeighbors) {
////                if (nonOwnNeighbor.getStrength() < getStrength() && nonOwnNeighbor.getProduction() > tempProd) {
////                    retDirection = gameMap.getDirectionFromTo(getLoc(), nonOwnNeighbor.getLoc(), gameMap.height, gameMap.width);
////                    tempProd = nonOwnNeighbor.getProduction();
////                }
////            }
////        }
//
//
////        Enemy enemy = new Enemy(gameMap, myID);
////
////        if (enemy.hasDirectEnemyNeighbor(getLoc())) {
////            Piece nextEnemy = enemy.getEnemyNeighbor(this);
////            if (!nextEnemy.isNil() && nextEnemy.getStrength() <= getStrength() && getStrength() > 200) {
////                retDirection = gameMap.getFromPieceToPiece(this, nextEnemy);
////            } else retDirection = Direction.STILL;
////
////        } else if (hasOnlyOwnNeighbors(myID)) {
////            List<Piece> npcsAndEnemies = new ArrayList<>();
////            npcsAndEnemies.addAll(gameInformation.getNpcs());
////            npcsAndEnemies.addAll(gameInformation.getEnemies());
////            Piece nextNpc = findClosestNonOwnPiece(npcsAndEnemies);
////            if (nextNpc != null && moveAccordingToOwnStrength()) {
////                retDirection = gameMap.getDirectionFromTo(loc, nextNpc.getLoc(), gameMap.height, gameMap.width);
////            }
////        } else {
////            List<Piece> nonOwnNeighbors = getAllNonOwnNeighbors(myID);
////            int tempProd = -1;
////
////            for (Piece nonOwnNeighbor : nonOwnNeighbors) {
////                if(nonOwnNeighbor.getStrength() < getStrength() && nonOwnNeighbor.getProduction() > tempProd) {
////                    retDirection = gameMap.getDirectionFromTo(getLoc(), nonOwnNeighbor.getLoc(), gameMap.height, gameMap.width);
////                    tempProd = nonOwnNeighbor.getProduction();
////                }
////            }
////        }
//
//        return retDirection;
//    }

    private Piece findClosestNonOwnPiece(List<Piece> npcsAndEnemies) {
        Piece npcOrEnemie = new NullPiece();
        if (npcsAndEnemies.size() > 0) {
            npcOrEnemie = npcsAndEnemies.get(0);
            for (Piece piece : npcsAndEnemies) {
                if (piece.getStrength() > 1 &&
                        gameMap.getDistance(loc, piece.getLoc()) < gameMap.getDistance(loc, npcOrEnemie.getLoc())) {
                    npcOrEnemie = piece;
                }

            }
        }
        return npcOrEnemie;
    }




    private List<Piece> getNpcNeighbors() {
        return Arrays.stream(Direction.CARDINALS)
                .map(direction -> Piece.fromLocationAndDirection(getLoc(), direction, gameMap))
                .filter(piece -> piece.getOwner() == Seventeenth.npcID).collect(Collectors.toList());
    }

    private List<Piece> getAllNonOwnNeighbors(int myID) {
        List<Piece> pieces = new ArrayList<>();

        for (Direction direction : Direction.CARDINALS) {
            if (getNonOwnNeighborByDirection(myID, direction) != null)
                pieces.add(getNonOwnNeighborByDirection(myID, direction));
        }

        return pieces;
    }

    private Piece getNonOwnNeighborByDirection(int myID, Direction direction) {
        Site site = gameMap.getSite(getLoc(), direction);
        if (site.owner != myID) return new Piece(gameMap.getLocation(getLoc(), direction), site, gameMap);
        return null;
    }

//    private Piece findMostValuablePiece(List<Piece> npcsAndEnemies, GeneralGameInformation gameInformation) {
//
//        int firstProductionGoal = gameInformation.getTopNPercentEnemyProduction(97);
////        int secondProductionGoal = gameInformation.getTopNPercentEnemyProduction(92);
////        int thirdProductionGoal = gameInformation.getTopNPercentEnemyProduction(89);
//
//        List<Piece> pieces = new ArrayList<>();
//        pieces.add(findMostValuablePieceWitdhProduction(npcsAndEnemies, firstProductionGoal));
////        pieces.add(findMostValuablePieceWitdhProduction(npcsAndEnemies, secondProductionGoal));
////        pieces.add(findMostValuablePieceWitdhProduction(npcsAndEnemies, thirdProductionGoal));
//
////        return pieces.stream()
////                .min((p1, p2) -> (int) (gameMap.getDistance(loc, p1.getLoc())))
////                .orElse(new NullPiece());
//        return findMostValuablePieceWitdhProduction(npcsAndEnemies, firstProductionGoal);
//    }
//
//
//    private Piece findMostValuablePieceWitdhProduction(List<Piece> npcsAndEnemies, int production) {
//
//        return npcsAndEnemies.stream()
//                .filter(p -> p.getProduction() >= production)
//                .reduce(new NullPiece(), (p1, p2) -> {
//                    if (!p1.isNil()
//                            && gameMap.getDistance(loc, p1.getLoc()) < gameMap.getDistance(loc, p2.getLoc()))
//                        return p1;
//                    else return p2;
//                });
//    }

    // v16
    private boolean moveAccordingToOwnStrength() {
        return (getProduction() == 0
                || getProduction() == 1 && getStrength() > 2)
                || (getProduction() == 2 && getStrength() > 5)
                || (getProduction() == 3 && getStrength() > 10)
                || (getProduction() == 4 && getStrength() > 20)
                || (getProduction() == 5 && getStrength() > 30)
                || (getProduction() == 6 && getStrength() > 40)
                || (getProduction() == 7 && getStrength() > 50)
                || (getProduction() == 8 && getStrength() > 60)
                || (getProduction() == 9 && getStrength() > 65)
                || (getProduction() == 10 && getStrength() > 70)
                || (getProduction() == 11 && getStrength() > 75)
                || (getProduction() == 12 && getStrength() > 80)
                || (getProduction() == 13 && getStrength() > 90)
                || (getProduction() == 14 && getStrength() > 100)
                || (getProduction() == 15 && getStrength() > 105)
                || (getProduction() == 16 && getStrength() > 110);

    }


        // v11
//    private boolean moveAccordingToOwnStrength() {
//        return (getProduction() == 0
//                || getProduction() == 1 && getStrength() > 2)
//                || (getProduction() == 2 && getStrength() > 5)
//                || (getProduction() == 3 && getStrength() > 10)
//                || (getProduction() == 4 && getStrength() > 20)
//                || (getProduction() == 5 && getStrength() > 30)
//                || (getProduction() == 6 && getStrength() > 40)
//                || (getProduction() == 7 && getStrength() > 50)
//                || (getProduction() == 8 && getStrength() > 60)
//                || (getProduction() == 9 && getStrength() > 65)
//                || (getProduction() == 10 && getStrength() > 70)
//                || (getProduction() == 11 && getStrength() > 75)
//                || (getProduction() == 12 && getStrength() > 80)
//                || (getProduction() == 13 && getStrength() > 90)
//                || (getProduction() == 14 && getStrength() > 100)
//                || (getProduction() == 15 && getStrength() > 110)
//                || (getProduction() == 16 && getStrength() > 120);


//    private boolean moveAccordingToOwnStrength() {
//        return (getProduction() == 0
//                || getProduction() == 1 && getStrength() > 2)
//                || (getProduction() == 2 && getStrength() > 5)
//                || (getProduction() == 3 && getStrength() > 10)
//                || (getProduction() == 4 && getStrength() > 15)
//                || (getProduction() == 5 && getStrength() > 20)
//                || (getProduction() == 6 && getStrength() > 25)
//                || (getProduction() == 7 && getStrength() > 30)
//                || (getProduction() == 8 && getStrength() > 35)
//                || (getProduction() == 9 && getStrength() > 40)
//                || (getProduction() == 10 && getStrength() > 45)
//                || (getProduction() == 11 && getStrength() > 50)
//                || (getProduction() >= 12 && getStrength() > 55);
////                || (getProduction() == 13 && getStrength() > 60)
////                || (getProduction() == 14 && getStrength() > 65)
////                || (getProduction() == 15 && getStrength() > 70)
////                || (getProduction() == 16 && getStrength() > 75);
//    }

//    private boolean moveAccordingToOwnStrength() {
//        return (getProduction() == 0
//                || getProduction() == 1 && getStrength() > 2)
//                || (getProduction() == 2 && getStrength() > 5)
//                || (getProduction() == 3 && getStrength() > 10)
//                || (getProduction() == 4 && getStrength() > 15)
//                || (getProduction() == 5 && getStrength() > 20)
//                || (getProduction() == 6 && getStrength() > 25)
//                || (getProduction() == 7 && getStrength() > 30)
//                || (getProduction() == 8 && getStrength() > 40)
//                || (getProduction() == 9 && getStrength() > 50)
//                || (getProduction() == 10 && getStrength() > 55)
//                || (getProduction() == 11 && getStrength() > 60)
//                || (getProduction() == 12 && getStrength() > 65)
//                || (getProduction() == 13 && getStrength() > 70)
//                || (getProduction() == 14 && getStrength() > 75)
//                || (getProduction() == 15 && getStrength() > 80)
//                || (getProduction() == 16 && getStrength() > 90);
//    }

    private boolean hasOnlyOwnNeighbors(int myID) {
        return hasXOtherNeighbors(myID, 0);
    }

    private boolean hasTwoOrMoreOwnNeighbors(int myID) {
        return hasXOtherNeighbors(myID, 0) || hasXOtherNeighbors(myID, 1) || hasXOtherNeighbors(myID, 2);
    }

    private boolean hasThreeOrMoreOwnNeighbors(int myID) {
        return hasXOtherNeighbors(myID, 0) || hasXOtherNeighbors(myID, 1);
    }

    boolean hasXOtherNeighbors(int myID, int expectedNeighborCount) {
        int neighborCount = 0;
        if (gameMap.getSite(loc, Direction.EAST).owner != myID) neighborCount++;
        if (gameMap.getSite(loc, Direction.SOUTH).owner != myID) neighborCount++;
        if (gameMap.getSite(loc, Direction.WEST).owner != myID) neighborCount++;
        if (gameMap.getSite(loc, Direction.NORTH).owner != myID) neighborCount++;

        return neighborCount == expectedNeighborCount;
    }

    @Override
    public String toString() {
        return "Piece{" +
                "loc=" + loc +
                ", site=" + site +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Piece piece = (Piece) o;

        if(loc.x == piece.getLoc().x && loc.y == piece.getLoc().y) return true;
//        if (site != null ? !site.equals(piece.site) : piece.site != null) return false;
//        return gameMap != null ? gameMap.equals(piece.gameMap) : piece.gameMap == null;

        return false;
    }

    @Override
    public int hashCode() {
        int result = loc != null ? loc.hashCode() : 0;
        result = 31 * result + (site != null ? site.hashCode() : 0);
        result = 31 * result + (gameMap != null ? gameMap.hashCode() : 0);
        return result;
    }

    Location getLoc() {
        return loc;
    }

    Site getSite() {
        return site;
    }

    int getOwner() {
        return this.getSite().owner;
    }

    int getStrength() {
        return this.getSite().strength;
    }

    int getProduction() {
        return this.getSite().production;
    }

    public boolean isNil() {
        return false;
    }

    GameMap getGameMap() {
        return gameMap;
    }

    boolean isMe(int myID) {
        return getOwner() == myID;
    }

    boolean isNpc() {
        return getOwner() == Seventeenth.npcID;
    }

    boolean isEnemy(int myID) {
        return !isMe(myID) && !isNpc();
    }


}
