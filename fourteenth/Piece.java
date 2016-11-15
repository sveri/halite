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

    static Piece fromXY(int x, int y, GameMap gameMap) {
        Location loc = new Location(x, y);
        return new Piece(loc, gameMap.getSite(loc), gameMap);
    }

    static Piece fromLocation(Location newLoc, GameMap gameMap) {
        return new Piece(newLoc, gameMap.getSite(newLoc), gameMap);
    }

    static Piece fromLocationAndDirection(Location loc, Direction direction, GameMap gameMap) {
        Site site = gameMap.getSite(loc, direction);
        return new Piece(gameMap.getLocation(loc, direction), site, gameMap);
    }

    Direction findNextDirection(int myID, GeneralGameInformation gameInformation) {
        Direction retDirection = Direction.STILL;

        List<Piece> npcsAndEnemies = gameInformation.getNpcsAndEnemies();

        int prodGoal = (int) gameInformation.getTopNPercentEnemyProduction(85);
        Piece closestNpcWithXProduction = findClosestPieceWithProduction(npcsAndEnemies, prodGoal);
        Direction nextNpcDirection = gameMap.getDirectionFromTo(this.getLoc(), closestNpcWithXProduction.getLoc(), gameMap.height, gameMap.width);
//        Piece nextPiece = Piece.fromLocationAndDirection(getLoc(), nextNpcDirection, gameMap);

        Enemy enemy = new Enemy(gameMap, myID);

        if (enemy.hasDirectEnemyNeighbor(getLoc())){
            Piece nextEnemy = enemy.getEnemyNeighbor(this);
            if(!nextEnemy.isNil()) {
                retDirection = gameMap.getFromPieceToPiece(this, nextEnemy);
//                MyBot.logger.info(this.toString());
//                MyBot.logger.info(Piece.fromLocationAndDirection(getLoc(), retDirection, gameMap).toString());
//                MyBot.logger.info(retDirection.toString());
            }
            else retDirection = Direction.STILL;

        } else if (hasOnlyOwnNeighbors(myID)
                && (closestNpcWithXProduction.getProduction() > -1
                && moveAccordingToOwnStrength())) {
            retDirection = nextNpcDirection;
//            MyBot.logger.info(this.toString());
//            MyBot.logger.info(nextPiece.toString());
//            MyBot.logger.info(nextNpcDirection.toString());

        } else {
            List<Piece> nonOwnNeighbors = getAllNonOwnNeighbors(myID);
            int tempProd = -1;

            for (Piece nonOwnNeighbor : nonOwnNeighbors) {
                if(nonOwnNeighbor.getStrength() < getStrength() && nonOwnNeighbor.getProduction() > tempProd) {
                    retDirection = gameMap.getDirectionFromTo(getLoc(), nonOwnNeighbor.getLoc(), gameMap.height, gameMap.width);
                    tempProd = nonOwnNeighbor.getProduction();
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

//    private Piece findClosestNonOwnPiece(List<Piece> npcsAndEnemies) {
//        Piece npcOrEnemie = null;
//        if (npcsAndEnemies.size() > 0) {
//            npcOrEnemie = npcsAndEnemies.get(0);
//            for (Piece piece : npcsAndEnemies) {
//                if (piece.getStrength() > 1 &&
//                        gameMap.getDistance(loc, piece.getLoc()) < gameMap.getDistance(loc, npcOrEnemie.getLoc())) {
//                    npcOrEnemie = piece;
//                }
//
//            }
//        }
//        return npcOrEnemie;
//    }

    private Piece findClosestPieceWithProduction(List<Piece> npcsAndEnemies, int minProduction) {
        Piece npcOrEnemie = new NullPiece();

        if (npcsAndEnemies.size() > 0) {
            npcOrEnemie = npcsAndEnemies.get(0);
            for (Piece piece : npcsAndEnemies) {
                if (piece.getStrength() > 1 && piece.getProduction() >= minProduction &&
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
                || (getProduction() == 15 && getStrength() > 105)
                || (getProduction() == 16 && getStrength() > 110);
    }

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

    public boolean isNil() { return false;}

    GameMap getGameMap() {
        return gameMap;
    }

    boolean isMe(int myID) { return getOwner() == myID;}

    boolean isNpc() { return getOwner() == MyBot.npcID;}

    boolean isEnemy(int myID) {
        return !isMe(myID) && ! isNpc();
    }
}
