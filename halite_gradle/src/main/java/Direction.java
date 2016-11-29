import java.util.Random;

enum Direction {
    STILL, NORTH, EAST, SOUTH, WEST;

    public static final Direction[] DIRECTIONS = new Direction[]{STILL, NORTH, EAST, SOUTH, WEST};
    public static final Direction[] CARDINALS = new Direction[]{NORTH, EAST, SOUTH, WEST};

    private static Direction fromInteger(int value) {
        if(value == 0) {
            return STILL;
        }
        if(value == 1) {
            return NORTH;
        }
        if(value == 2) {
            return EAST;
        }
        if(value == 3) {
            return SOUTH;
        }
        if(value == 4) {
            return WEST;
        }
        return null;
    }

    public static Direction randomDirection() {
        return fromInteger(new Random().nextInt(5));
    }

    static Direction getDirectionFromToGameMap(Location from, Location to, GameMap gameMap) {
        return getDirectionFromTo(from, to, gameMap.height, gameMap.width);
    }

    static Direction getDirectionFromTo(Location from, Location to, int heigth, int width) {
        // need to move on y line
        if(from.x == to.x) {
            if(from.y < to.y && (to.y - from.y) < heigth / 2) {
                return Direction.SOUTH;
            } else if(from.y < to.y && (to.y - from.y) >= heigth / 2) {
                return Direction.NORTH;
            } else if(to.y < from.y && (from.y - to.y) < heigth / 2) {
                return Direction.NORTH;
            } else {
                return Direction.SOUTH;
            }
        }
        // need to move on x line
        else {
            if (from.x < to.x && (to.x - from.x) < width / 2) {
                return Direction.EAST;
            } else if (from.x < to.x && (to.x - from.x) >= width / 2) {
                return Direction.WEST;
            } else if (to.x < from.x && (from.x - to.x) < width / 2) {
                return Direction.WEST;
            }
            return Direction.EAST;
        }
    }

    public static Direction getOppositeDirection(Direction pieceDirection) {
        switch (pieceDirection) {
            case EAST: return WEST;
            case SOUTH:
                return NORTH;
            case WEST:
                return EAST;
            case NORTH:
                return SOUTH;
            default:
                return STILL;
        }
    }
}
