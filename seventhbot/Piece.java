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

    int getOwner() { return this.getSite().owner;}

    int getStrength() {
        return this.getSite().strength;
    }

    boolean hasNOtherNeigbhors(int myID, int expectedNeighborCount) {
        int neigbhorCount = 0;
        if (gameMap.getSite(loc, Direction.EAST).owner != myID) neigbhorCount++;
        if (gameMap.getSite(loc, Direction.SOUTH).owner != myID) neigbhorCount++;
        if (gameMap.getSite(loc, Direction.WEST).owner != myID) neigbhorCount++;
        if (gameMap.getSite(loc, Direction.NORTH).owner != myID) neigbhorCount++;

        return neigbhorCount == expectedNeighborCount;
    }

    Direction getNextNonEnemyNonOwnDirectionThatItCanMoveTo(int myID) {
        Direction retDirection = Direction.STILL;

        Site siteEast = gameMap.getSite(getLoc(), Direction.EAST);
        Site siteSouth = gameMap.getSite(getLoc(), Direction.SOUTH);
        Site siteWest = gameMap.getSite(getLoc(), Direction.WEST);
        Site siteNorth = gameMap.getSite(getLoc(), Direction.NORTH);

        if(siteEast.owner != myID && siteEast.strength < getStrength()) retDirection = Direction.EAST;
        if(siteSouth.owner != myID && siteSouth.strength < getStrength()) retDirection = Direction.SOUTH;
        if(siteWest.owner != myID && siteWest.strength < getStrength()) retDirection = Direction.WEST;
        if(siteNorth.owner != myID && siteNorth.strength < getStrength()) retDirection = Direction.NORTH;

        if(hasNOtherNeigbhors(myID, 0)) {
            if(getStrength() > 100 && siteEast.strength + getStrength() < 500) retDirection = Direction.EAST;
            else if(getStrength() > 100 && siteSouth.strength + getStrength() < 500) retDirection = Direction.SOUTH;
            else if(getStrength() > 100 && siteWest.strength + getStrength() < 500) retDirection = Direction.WEST;
            else if(getStrength() > 100 && siteNorth.strength + getStrength() < 500) retDirection = Direction.NORTH;
        }

        return retDirection;
    }
}
