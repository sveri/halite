import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class Enemy {

    private final GameMap gameMap;
    private int myID;

    public Enemy(GameMap gameMap, int myID) {
        this.gameMap = gameMap;
        this.myID = myID;
    }


    Piece getEnemyNeighbor(Piece curPiece) {
        Map<Direction, Integer> dirToStrength = new HashMap<>();

        for (Direction direction : Direction.CARDINALS) {
            int strengthOfAllConnectedEnemies = getStrengthOfAllConnectedEnemies(curPiece, direction);
            if(strengthOfAllConnectedEnemies > 0) dirToStrength.put(direction, strengthOfAllConnectedEnemies);
        }

        if (dirToStrength.isEmpty()) {
            return new NullPiece();
        }

        Direction directionMinStrength = Collections.min(dirToStrength.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey();
//        MyBot.logger.info(dirToStrength.toString());

        return Piece.fromLocationAndDirection(curPiece.getLoc(), directionMinStrength, gameMap);
    }

    private int getStrengthOfAllConnectedEnemies(Piece curPiece, Direction direction) {
        int enemyStrength = 0;
        Piece piece = Piece.fromLocationAndDirection(curPiece.getLoc(), direction, gameMap);
        if (piece.isEnemy(myID)) enemyStrength += piece.getStrength();

        Location nextLoc = gameMap.getLocation(curPiece.getLoc(), direction);

        piece = Piece.fromLocationAndDirection(nextLoc, Direction.getBeforeDirection(direction), gameMap);
        if (piece.isEnemy(myID)) enemyStrength += piece.getStrength();

        piece = Piece.fromLocationAndDirection(nextLoc, Direction.getAfterDirection(direction), gameMap);
        if (piece.isEnemy(myID)) enemyStrength += piece.getStrength();

        return enemyStrength;
    }

    boolean hasDirectEnemyNeighbor(Location loc) {
        for (Direction direction : Direction.CARDINALS) {
            if (hasCornerwiseEnemyNeighbor(direction, loc))
                return true;
        }
        return false;
    }

    private boolean hasCornerwiseEnemyNeighbor(Direction direction, Location loc) {
        boolean retVal = false;
        if (direction == Direction.EAST) {
            Location oneStepLoc = gameMap.getLocation(loc, direction);
            retVal = hasEnemyNeighborAt(loc, Direction.EAST)
                    || hasEnemyNeighborAt(oneStepLoc, Direction.NORTH)
                    || hasEnemyNeighborAt(oneStepLoc, Direction.SOUTH);
        } else if (direction == Direction.SOUTH) {
            Location oneStepLoc = gameMap.getLocation(loc, direction);
            retVal = hasEnemyNeighborAt(loc, Direction.SOUTH)
                    || hasEnemyNeighborAt(oneStepLoc, Direction.EAST)
                    || hasEnemyNeighborAt(oneStepLoc, Direction.WEST);
        } else if (direction == Direction.WEST) {
            Location oneStepLoc = gameMap.getLocation(loc, direction);
            retVal = hasEnemyNeighborAt(loc, Direction.WEST)
                    || hasEnemyNeighborAt(oneStepLoc, Direction.NORTH)
                    || hasEnemyNeighborAt(oneStepLoc, Direction.SOUTH);
        } else if (direction == Direction.NORTH) {
            Location oneStepLoc = gameMap.getLocation(loc, direction);
            retVal = hasEnemyNeighborAt(loc, Direction.NORTH)
                    || hasEnemyNeighborAt(oneStepLoc, Direction.EAST)
                    || hasEnemyNeighborAt(oneStepLoc, Direction.WEST);
        }
        return retVal;
    }

    private boolean hasEnemyNeighborAt(Location loc, Direction direction) {
        return gameMap.getOwner(loc, direction) != myID && gameMap.getOwner(loc, direction) != MyBot.npcID;
    }
}
