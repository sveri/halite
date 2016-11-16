

public class NullPiece extends Piece{

    NullPiece() {
        this(null, null, null);
    }

    NullPiece(Location loc, Site site, GameMap gameMap) {
        super(loc, site, gameMap);
    }

    @Override
    Direction findNextDirection(int myID, GeneralGameInformation gameInformation) {
        return Direction.STILL;
    }

    @Override
    boolean hasXOtherNeighbors(int myID, int expectedNeighborCount) {
        return false;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    Location getLoc() {
        return new NullLocation();
    }

    @Override
    Site getSite() {
        return null;
    }

    @Override
    int getOwner() {
        return -1;
    }

    @Override
    int getStrength() {
        return -1;
    }

    @Override
    int getProduction() {
        return -1;
    }

    @Override
    public boolean isNil() { return true; }
}
