public class Neighbor {

    public static Direction getWeakestNeighborDirection(GameMap gameMap, Location curLoc, Site curSite) {
        if (getSite(gameMap, curLoc, curSite, Direction.NORTH) !=null) return Direction.NORTH;
        if (getSite(gameMap, curLoc, curSite, Direction.SOUTH) !=null) return Direction.SOUTH;
        if (getSite(gameMap, curLoc, curSite, Direction.WEST) !=null) return Direction.WEST;
        if (getSite(gameMap, curLoc, curSite, Direction.EAST) !=null) return Direction.EAST;

        return null;
    }

    private static Direction getSite(GameMap gameMap, Location curLoc, Site curSite, Direction direction) {
        Site site = gameMap.getSite(curLoc, direction);
        if(site.strength < curSite.strength && curSite.owner != site.owner) return direction;
        return null;
    }
}
