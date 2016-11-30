import java.util.*;

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

    public boolean hasEnemyNeighbor() {
        for (Direction direction : Direction.CARDINALS) {
            if (gameMap.getSite(this.loc, direction).owner > 1) {
                return true;
            }
        }
        return false;
    }

    public Piece findPieceWithMostEnemyNeighbors() {
        Adder adder = new Adder(gameMap.width, gameMap.height);
        Map<Direction, Integer> dirToEnemies = new HashMap<>();

        for (Direction dir : Direction.CARDINALS) {
            dirToEnemies.put(dir, 0);
        }

        Site site = gameMap.getSite(new Location(adder.addX(getLocation().x, 0), adder.subY(getLocation().y, 1)));
        incEnemiesCount(dirToEnemies, site, Direction.NORTH);
        site = gameMap.getSite(new Location(adder.subX(getLocation().x, 1), adder.subY(getLocation().y, 1)));
        incEnemiesCount(dirToEnemies, site, Direction.NORTH);
        site = gameMap.getSite(new Location(adder.addX(getLocation().x, 1), adder.subY(getLocation().y, 1)));
        incEnemiesCount(dirToEnemies, site, Direction.NORTH);

        site = gameMap.getSite(new Location(adder.addX(getLocation().x, 1), adder.subY(getLocation().y, 0)));
        incEnemiesCount(dirToEnemies, site, Direction.EAST);
        site = gameMap.getSite(new Location(adder.addX(getLocation().x, 1), adder.subY(getLocation().y, 1)));
        incEnemiesCount(dirToEnemies, site, Direction.EAST);
        site = gameMap.getSite(new Location(adder.addX(getLocation().x, 1), adder.addY(getLocation().y, 1)));
        incEnemiesCount(dirToEnemies, site, Direction.EAST);

        site = gameMap.getSite(new Location(adder.addX(getLocation().x, 0), adder.addY(getLocation().y, 1)));
        incEnemiesCount(dirToEnemies, site, Direction.SOUTH);
        site = gameMap.getSite(new Location(adder.addX(getLocation().x, 1), adder.addY(getLocation().y, 1)));
        incEnemiesCount(dirToEnemies, site, Direction.SOUTH);
        site = gameMap.getSite(new Location(adder.subX(getLocation().x, 1), adder.addY(getLocation().y, 1)));
        incEnemiesCount(dirToEnemies, site, Direction.SOUTH);

        site = gameMap.getSite(new Location(adder.subX(getLocation().x, 1), adder.subY(getLocation().y, 0)));
        incEnemiesCount(dirToEnemies, site, Direction.WEST);
        site = gameMap.getSite(new Location(adder.subX(getLocation().x, 1), adder.subY(getLocation().y, 1)));
        incEnemiesCount(dirToEnemies, site, Direction.WEST);
        site = gameMap.getSite(new Location(adder.subX(getLocation().x, 1), adder.addY(getLocation().y, 1)));
        incEnemiesCount(dirToEnemies, site, Direction.WEST);


        if (dirToEnemies.isEmpty()) {
            return NullPiece.newNullPiece();
        }

        Map.Entry<Direction, Integer> maxEntry = null;

        for (Map.Entry<Direction, Integer> entry : dirToEnemies.entrySet()) {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
        }

        return Piece.fromLocationAndDirection(getLocation(), gameMap, maxEntry.getKey());
    }

    private void incEnemiesCount(Map<Direction, Integer> m, Site site, Direction dir) {
        if (site.owner > 1) {
            Integer count = m.get(dir);
            m.put(dir,  count += 1);
        }
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
