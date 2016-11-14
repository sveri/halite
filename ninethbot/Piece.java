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

    Direction getNextNonEnemyNonOwnDirectionThatItCanMoveTo(int myID, List<Piece> npcs) {
        Direction retDirection = Direction.STILL;

        Site siteEast = gameMap.getSite(getLoc(), Direction.EAST);
        Site siteSouth = gameMap.getSite(getLoc(), Direction.SOUTH);
        Site siteWest = gameMap.getSite(getLoc(), Direction.WEST);
        Site siteNorth = gameMap.getSite(getLoc(), Direction.NORTH);

        if (siteEast.owner == NinethBot.npcID && siteEast.strength < getStrength()) retDirection = Direction.EAST;
        if (siteSouth.owner == NinethBot.npcID && siteSouth.strength < getStrength()) retDirection = Direction.SOUTH;
        if (siteWest.owner == NinethBot.npcID && siteWest.strength < getStrength()) retDirection = Direction.WEST;
        if (siteNorth.owner == NinethBot.npcID && siteNorth.strength < getStrength()) retDirection = Direction.NORTH;

//        if(retDirection != Direction.STILL && hasEnemyNeighbor(myID)) return retDirection;


        if (hasOnlyOwnNeighbors(myID)) {
            retDirection = Direction.STILL;
            Piece nextNpc = findClosestNpc(npcs);
            if (nextNpc != null && moveAccordingToOwnStrength()) {
                retDirection = gameMap.getDirectionFromTo(loc, nextNpc.getLoc(), gameMap.height, gameMap.width);
                NinethBot.logger.info("from " + loc );
                NinethBot.logger.info("to " + nextNpc.getLoc() );
                NinethBot.logger.info(retDirection.toString());
            }
        }

        return retDirection;
    }

    private Piece findClosestNpc(List<Piece> pieces) {
        Piece closestNpc = null;
        if (pieces.size() > 0) {
            closestNpc = pieces.get(0);
            for (Piece piece : pieces) {
                if (piece.getStrength() > 1 &&
                        gameMap.getDistance(loc, piece.getLoc()) < gameMap.getDistance(loc, closestNpc.getLoc())) {
                    closestNpc = piece;
                }

            }
        }
        return closestNpc;
    }

    private boolean moveAccordingToOwnStrength() {
        return (getProduction() == 0
                || getProduction() == 1 && getStrength() > 10)
                || (getProduction() == 2 && getStrength() > 30)
                || (getProduction() == 3 && getStrength() > 70)
                || (getProduction() == 4 && getStrength() > 100)
                || (getProduction() == 5 && getStrength() > 130)
                || (getProduction() == 6 && getStrength() > 170)
                || (getProduction() == 7 && getStrength() > 200)
                || (getProduction() >= 8 && getStrength() > 250);
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
//        return gameMap.getOwner(loc, direction) != myID && gameMap.getOwner(loc, direction) != NinethBot.npcID;
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
