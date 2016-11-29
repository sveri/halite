import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by sveri on 25.11.16.
 */
public class Piece {

    private final Location loc;
    private final GameMap gameMap;
    private final int owner;
    private final Direction direction;
    private final Site site;
//    private List<Piece> ownNeighbors;

    protected Piece(int owner) {
        this.owner = owner;
        site = null;
        loc = null;
        gameMap = null;
        direction = null;
    }

    private Piece(Location loc, GameMap gameMap, int id, Direction direction) {
        this.loc = loc;
        this.gameMap = gameMap;
        this.owner = id;
        this.direction = direction;
        this.site = gameMap.getSite(loc);
    }

    public static Piece fromLocation(Location loc, GameMap gameMap, int id) {
        return new Piece(loc, gameMap, id, null);
    }

    public static Piece fromLocationAndDirection(Location loc, GameMap gameMap, Direction direction) {
        Location newLocation = gameMap.getLocation(loc, direction);
        Site newSite = gameMap.getSite(newLocation);
        return new Piece(newLocation, gameMap, newSite.owner, direction);
    }

    public boolean hasOnlyOwnNeighbors() {
        for (Direction direction : Direction.CARDINALS) {
            if (gameMap.getSite(this.loc, direction).owner != owner) {
                return false;
            }
        }
        return true;
    }

    List<Piece> getNonOwnNeighbors() {
        List<Piece> neighbors = new ArrayList<>();
        for (Direction direction : Direction.CARDINALS) {
            Piece neighborPiece = Piece.fromLocationAndDirection(loc, gameMap, direction);
            if(neighborPiece.getOwner() != owner)
                neighbors.add(neighborPiece);
        }
        return neighbors;
    }

    Piece getBestNonOwnNeighbor(List<Piece> ownPieces) {
        List<Piece> neighbors = getNonOwnNeighbors();
        Collections.sort(neighbors, new PieceStrengthSorter());

        for(Piece neighbor : neighbors) {
            if (isMoveToAllowed(neighbor, ownPieces)) {
                return neighbor;
            }
        }

        return NullPiece.newNullPiece();
    }

    private boolean isMoveToAllowed(Piece neighbor, List<Piece> ownPieces) {
        return neighbor.getProduction() > 1 || ownPieces.size() < 20;
    }

//    private List<Piece> getOwnNeighbors() {
//        List<Piece> neighbors = new ArrayList<>();
//        for (Direction direction : Direction.CARDINALS) {
//            Piece neighborPiece = Piece.fromLocationAndDirection(loc, gameMap, direction);
//            if(neighborPiece.getOwner() == owner)
//                neighbors.add(neighborPiece);
//        }
//
//        return neighbors;
//    }

    /**
     * An own piece is considered to be moveable to, if it has no neighbors with a production < x
     * and strength > y
     */
    boolean isOwnMoveablePiece(List<Piece> ownPieces) {
        if(hasOnlyOwnNeighbors()) return true;

        for (Piece nonOwnNeighbor : getNonOwnNeighbors()) {
            if(isMoveToAllowed(nonOwnNeighbor, ownPieces)) return true;
        }
        return false;
    }

    int getOwner() {
        return owner;
    }

    Location getLocation() {
        return loc;
    }

    int getProduction() {
        return site.production;
    }

    int getStrength() {
        return site.strength;
    }

    public boolean isNull() {
        return false;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "Piece{" +
                "loc=" + loc +
                ", owner=" + owner +
//                ", site=" + site +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Piece piece = (Piece) o;

        if (owner != piece.owner) return false;
        return loc.equals(piece.loc);
    }

    @Override
    public int hashCode() {
        int result = loc.hashCode();
        result = 31 * result + owner;
        return result;
    }
}
