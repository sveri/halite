import java.util.ArrayList;
import java.util.List;

class Piece {

    final private Location loc;

    final private Site site;

    private final GameMap gameMap;

    Piece(Location loc, Site site, GameMap gameMap) {
        this.loc = loc;
        this.site = site;
        this.gameMap = gameMap;
    }

    static Piece fromLocation(int x, int y, GameMap gameMap) {
        Location loc = new Location(x, y);
        return new Piece(loc, gameMap.getSite(loc), gameMap);
    }

    Direction findNextDirection(int myID, GeneralGameInformation gameInformation) {
        int earlyGameDivider = gameMap.getTotalPiecesCount() / 30;
        Direction retDirection = Direction.STILL;

        // early game until we have 10 pieces
        if (gameInformation.getOwns().size() <= earlyGameDivider && !hasOnlyOwnNeighbors(myID)) {
            Piece nextPiece = new NullPiece();
            List<Piece> nonOwnNeighbors = getAllNonOwnNeighbors(myID);
            for (Piece nonOwnNeighbor : nonOwnNeighbors) {
                if(nonOwnNeighbor.getProduction() > nextPiece.getProduction()) nextPiece = nonOwnNeighbor;
            }
            if (nextPiece.getProduction() > -1 && nextPiece.getStrength() < getStrength()) {
                retDirection = gameMap.getDirectionFromTo(getLoc(), nextPiece.getLoc(), gameMap.height, gameMap.width);
            }

        }
        //middle to late for now
        else if (gameInformation.getOwns().size() > earlyGameDivider) {

            if (hasOnlyOwnNeighbors(myID)) {
                List<Piece> npcsAndEnemies = new ArrayList<>();
                npcsAndEnemies.addAll(gameInformation.getNpcs());
                npcsAndEnemies.addAll(gameInformation.getEnemies());
                Piece nextNpc = findClosestNonOwnPiece(npcsAndEnemies);
                if (nextNpc != null && moveAccordingToOwnStrength()) {
                    retDirection = gameMap.getDirectionFromTo(loc, nextNpc.getLoc(), gameMap.height, gameMap.width);
                }
            } else {
                List<Piece> nonOwnNeighbors = getAllNonOwnNeighbors(myID);
                int tempProd = -1;

                for (Piece nonOwnNeighbor : nonOwnNeighbors) {
                    if (nonOwnNeighbor.getStrength() < getStrength() && nonOwnNeighbor.getProduction() > tempProd) {
                        retDirection = gameMap.getDirectionFromTo(getLoc(), nonOwnNeighbor.getLoc(), gameMap.height, gameMap.width);
                        tempProd = nonOwnNeighbor.getProduction();
                    }
                }
            }

        }

        return retDirection;
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

    private Piece findClosestNonOwnPiece(List<Piece> npcsAndEnemies) {
        Piece npcOrEnemie = null;
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
                || (getProduction() == 15 && getStrength() > 110)
                || (getProduction() == 16 && getStrength() > 120);
    }


//    private boolean hasEnemyNeighbor(int myID) {
//        return hasCornerwiseEnemyNeighbor(myID, Direction.EAST) ||
//                hasCornerwiseEnemyNeighbor(myID, Direction.SOUTH) ||
//                hasCornerwiseEnemyNeighbor(myID, Direction.WEST) ||
//                hasCornerwiseEnemyNeighbor(myID, Direction.NORTH);
//    }
//
//    private boolean hasCornerwiseEnemyNeighbor(int myID, Direction direction) {
//        boolean retVal = false;
//        if (direction == Direction.EAST) {
//            Location oneStepLoc = gameMap.getLocation(loc, direction);
//            retVal = hasEnemyNeighborAt(loc, myID, Direction.EAST)
//                    || hasEnemyNeighborAt(oneStepLoc, myID, Direction.NORTH)
//                    || hasEnemyNeighborAt(oneStepLoc, myID, Direction.SOUTH);
//        } else if (direction == Direction.SOUTH) {
//            Location oneStepLoc = gameMap.getLocation(loc, direction);
//            retVal = hasEnemyNeighborAt(loc, myID, Direction.SOUTH)
//                    || hasEnemyNeighborAt(oneStepLoc, myID, Direction.EAST)
//                    || hasEnemyNeighborAt(oneStepLoc, myID, Direction.WEST);
//        } else if (direction == Direction.WEST) {
//            Location oneStepLoc = gameMap.getLocation(loc, direction);
//            retVal = hasEnemyNeighborAt(loc, myID, Direction.WEST)
//                    || hasEnemyNeighborAt(oneStepLoc, myID, Direction.NORTH)
//                    || hasEnemyNeighborAt(oneStepLoc, myID, Direction.SOUTH);
//        } else if (direction == Direction.NORTH) {
//            Location oneStepLoc = gameMap.getLocation(loc, direction);
//            retVal = hasEnemyNeighborAt(loc, myID, Direction.NORTH)
//                    || hasEnemyNeighborAt(oneStepLoc, myID, Direction.EAST)
//                    || hasEnemyNeighborAt(oneStepLoc, myID, Direction.WEST);
//        }
//        return retVal;
//    }
//
//    private boolean hasEnemyNeighborAt(Location loc, int myID, Direction direction) {
//        return gameMap.getOwner(loc, direction) != myID && gameMap.getOwner(loc, direction) != Twelth.npcID;
//    }

    private boolean hasOnlyOwnNeighbors(int myID) {
        return hasXOtherNeighbors(myID, 0);
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
}
