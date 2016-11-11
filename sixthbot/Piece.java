import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

        int i;
        Location newLoc;

        if (loc.x + 1 < gameMap.width) {
            i = loc.x + 1;
        } else {
            i = 0;
        }
        newLoc = new Location(i, loc.y);
        neighbors.add(new Piece(newLoc, gameMap.getSite(newLoc), gameMap));

        if (loc.x - 1 >= 0) i = loc.x - 1;
        else i = gameMap.width - 1;
        newLoc = new Location(i, loc.y);
        neighbors.add(new Piece(newLoc, gameMap.getSite(newLoc), gameMap));

        if (loc.y + 1 < gameMap.height) i = loc.y + 1;
        else i = 0;
        newLoc = new Location(loc.x, i);
        neighbors.add(new Piece(newLoc, gameMap.getSite(newLoc), gameMap));

        if (loc.y - 1 >= 0) i = loc.y - 1;
        else i = gameMap.height - 1;
        newLoc = new Location(loc.x, i);
        neighbors.add(new Piece(newLoc, gameMap.getSite(newLoc), gameMap));

        return neighbors;
    }

    Direction getDirectionTo(Location newLoc, int myID) {
        if (loc.x == 0) {
            if(gameMap.getSite(loc, Direction.WEST).owner == myID) return Direction.EAST; else return Direction.WEST;
        }
        if (loc.x == gameMap.width - 1){
            if(gameMap.getSite(loc, Direction.EAST).owner == myID) return Direction.WEST; else return Direction.EAST;
        }
        if (loc.y == 0) {
            if(gameMap.getSite(loc, Direction.NORTH).owner == myID) return  Direction.SOUTH; else return Direction.NORTH;
        }
        if (loc.y == gameMap.height - 1){
            if(gameMap.getSite(loc, Direction.SOUTH).owner == myID) return Direction.NORTH; else return Direction.SOUTH;
        }

        if (newLoc.x > loc.x) return Direction.EAST;
        if (newLoc.x < loc.x) return Direction.WEST;
        if (newLoc.y > loc.y) return Direction.SOUTH;
        return Direction.NORTH;
    }

    static Piece getWeakestEnemyNeighbor(List<Piece> neighbors, int myID) {
        return getWeakestNeighbor(neighbors, myID);
    }

    private static Piece getWeakestNeighbor(List<Piece> neighbors, int myID) {
        Collections.sort(neighbors, new PieceStrengthComparator());
        for (Piece piece : neighbors) {
            if (piece.getSite().owner != myID) return piece;
        }
        return null;
    }

    Piece findPieceInNextEnemyDirection(int myID) {
        for (int i = 0; i < gameMap.height; i++) {

            int j = i;
            if (loc.x + i >= gameMap.height) j = loc.x + i - gameMap.height;
            Location newLoc = new Location(j, loc.y);
            Site newSite = gameMap.getSite(newLoc);
            if (newSite.owner != myID) {
                return new Piece(newLoc, newSite, gameMap);
            }

            j = i;
            if (loc.x - i < 0) j = gameMap.height + loc.x - i;
            newLoc = new Location(j, loc.y);
            newSite = gameMap.getSite(newLoc);
            if (newSite.owner != myID) {
                return new Piece(newLoc, newSite, gameMap);
            }

            j = i;
            if (loc.y + i >= gameMap.width) j = loc.y + i - gameMap.width;
            newLoc = new Location(loc.x, j);
            newSite = gameMap.getSite(newLoc);
            if (newSite.owner != myID) {
                return new Piece(newLoc, newSite, gameMap);
            }

            j = i;
            if (loc.y - i < 0) j = gameMap.width + loc.y - i;
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

    int getStrength() {
        return this.getSite().strength;
    }

    int getProduction() {
        return this.getSite().production;
    }


}
