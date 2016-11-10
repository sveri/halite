public class Piece {

    final private Location loc;

    final private Site site;

    public Piece(Location loc, Site site) {
        this.loc = loc;
        this.site = site;
    }

    public Piece findWeakestEnemyNeigbhor(GameMap gameMap, int myId) {
        Piece enemyPiece = null;
        Piece tempPiece;

        tempPiece = findWeakestEnemyNeigbhorIntern(gameMap, myId, new Location(loc.x + 1, loc.y));
        if (tempPiece != null) enemyPiece = tempPiece;
        tempPiece = findWeakestEnemyNeigbhorIntern(gameMap, myId, new Location(loc.x - 1, loc.y));
        if (tempPiece != null) enemyPiece = tempPiece;
        tempPiece = findWeakestEnemyNeigbhorIntern(gameMap, myId, new Location(loc.x, loc.y + 1));
        if (tempPiece != null) enemyPiece = tempPiece;
        tempPiece = findWeakestEnemyNeigbhorIntern(gameMap, myId, new Location(loc.x, loc.y - 1));
        if (tempPiece != null) enemyPiece = tempPiece;


        return enemyPiece;
    }

    private Piece findWeakestEnemyNeigbhorIntern(GameMap gameMap, int myId, Location newLoc) {

        if (newLoc.x < gameMap.width && newLoc.x >= 0 && newLoc.y < gameMap.height && newLoc.y >= 0) {
            Site newSite = gameMap.getSite(newLoc);

            if ((newSite.strength < this.site.strength && newSite.owner != myId)
                    || (newSite.strength < this.site.strength && this.site.strength > 200)) {
                return new Piece(newLoc, newSite);
            }
        }

        return null;
    }

    public Direction getDirectionTo(Location newLoc) {
        if (newLoc.x > loc.x) return Direction.EAST;
        if (newLoc.x < loc.x) return Direction.WEST;
        if (newLoc.y > loc.y) return Direction.SOUTH;
        return Direction.NORTH;
    }

    @Override
    public String toString() {
        return "Piece{" +
                "loc=" + loc +
                ", site=" + site +
                '}';
    }

    public Location getLoc() {
        return loc;
    }

    public Site getSite() {
        return site;
    }
}
