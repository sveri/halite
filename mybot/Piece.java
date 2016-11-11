import java.util.ArrayList;
import java.util.Collections;
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

    List<Piece> getNeighbors() {
        List<Piece> neighbors = new ArrayList<>();
        if (loc.x + 1 < gameMap.width)
            neighbors.add(new Piece(new Location(loc.x + 1, loc.y), gameMap.getSite(new Location(loc.x + 1, loc.y)), gameMap));
        if (loc.x - 1 >= 0)
            neighbors.add(new Piece(new Location(loc.x - 1, loc.y), gameMap.getSite(new Location(loc.x - 1, loc.y)), gameMap));
        if (loc.y + 1 < gameMap.height)
            neighbors.add(new Piece(new Location(loc.x, loc.y + 1), gameMap.getSite(new Location(loc.x, loc.y + 1)), gameMap));
        if (loc.y - 1 >= 0)
            neighbors.add(new Piece(new Location(loc.x, loc.y - 1), gameMap.getSite(new Location(loc.x, loc.y - 1)), gameMap));

        return neighbors;
    }

    Direction getDirectionTo(Location newLoc) {
        if (newLoc.x > loc.x) return Direction.EAST;
        if (newLoc.x < loc.x) return Direction.WEST;
        if (newLoc.y > loc.y) return Direction.SOUTH;
        return Direction.NORTH;
    }

    static Piece getWeakestEnemyNeighbor(List<Piece> neighbors, int myID) {
        return getWeakestNeighbor(neighbors, myID, true);
    }

    static Piece getWeakestOwnNeighbor(List<Piece> neighbors, int myID) {
        return getWeakestNeighbor(neighbors, myID, false);
    }


    private static Piece getWeakestNeighbor(List<Piece> neighbors, int myID, boolean onlyEnemy) {
        Collections.sort(neighbors, new PieceStrengthComparator());
        if(onlyEnemy) {
            for (Piece piece : neighbors) {
                if(piece.getSite().owner != myID) return piece;
            }
        } else {
            return neighbors.get(0);
        }

        return null;
    }

    Piece findPieceInNextEnemyDirection(int myID) {
//        for(int i = 0; i < gameMap.height; i ++) {
        for(int i = 0; i < gameMap.height; i ++) {

            int j = i;
            if(loc.x + i >= gameMap.height) j = loc.x + i - gameMap.height;
            Location newLoc = new Location(j, loc.y);
            Site newSite = gameMap.getSite(newLoc);
            if (newSite.owner != myID) {
                return new Piece(newLoc, newSite, gameMap);
            }

            j = i;
            if(loc.x - i < 0) j = gameMap.height + loc.x - i;
            newLoc = new Location(j, loc.y);
            newSite = gameMap.getSite(newLoc);
            if (newSite.owner != myID) {
                return new Piece(newLoc, newSite, gameMap);
            }

            j = i;
            if(loc.y + i >= gameMap.width) j = loc.y + i - gameMap.width;
            newLoc = new Location(loc.x, j);
            newSite = gameMap.getSite(newLoc);
            if (newSite.owner != myID) {
                return new Piece(newLoc, newSite, gameMap);
            }

            j = i;
            if(loc.x - i < 0) j = gameMap.width + loc.y - i;
            newLoc = new Location(loc.x, j);
            newSite = gameMap.getSite(newLoc);
            if (newSite.owner != myID) {
                return new Piece(newLoc, newSite, gameMap);
            }
        }
        return null;
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

    int getStrength() {return this.getSite().strength;}

    int getProduction() {return this.getSite().production;}


}
