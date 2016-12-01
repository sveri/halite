import java.util.*;

/**
 * Created by sveri on 29.11.16.
 */
public class MoveValueCalculator {

    private final GameMap gameMap;
    private final int myID;

    public MoveValueCalculator(GameMap gameMap, int myID) {
        this.gameMap = gameMap;
        this.myID = myID;
    }

    static Piece findMostValuablePiece(Piece own, GameMap gameMap, int myID) {

//        long endTime = System.nanoTime() + 500000000;
//        long endTime = System.nanoTime() + 100000000;
//        Map<Direction, Integer> dirToValue = new HashMap<>();
        List<MoveFromTo> possibleMoves = new ArrayList<>();

        Set<Piece> neighborPieces = getNeighborPieces(own, gameMap);

        for (Piece piece : neighborPieces) {
            List<Site> sitesFromTo = getSitesFromTo(own, piece, gameMap);
            int moveValue = 0;
            for (Site site : sitesFromTo) {
                moveValue += getStrengthValue(site) + getProductionValue(site) + getNpcOwnEnemyValue(site, myID);
            }
            possibleMoves.add(new MoveFromTo(own, piece, Direction.getDirectionFromToGameMap(own.getLocation(),
                    piece.getLocation(), gameMap), moveValue));
        }

        MoveFromTo moveFromTo = possibleMoves.stream().max(new MoveValueComparator()).get();

        return Piece.fromLocationAndDirection(own.getLocation(), gameMap, moveFromTo.getNextDirection());
    }

    static List<Site> getSitesFromTo(Piece from, Piece to, GameMap gameMap) {
        List<Site> retSites = new ArrayList<>();
        boolean notArrivedYet = true;

        Location fromLoc = from.getLocation();
        while (notArrivedYet) {

            Direction nextDir = Direction.getDirectionFromToGameMap(fromLoc, to.getLocation(), gameMap);
            fromLoc = gameMap.getLocation(fromLoc, nextDir);
            retSites.add(gameMap.getSite(fromLoc, nextDir));

            if (fromLoc.equals(to.getLocation())) {
                notArrivedYet = false;
            }


        }

        return retSites;
    }



    static Set<Piece> getNeighborPieces(Piece own, GameMap gameMap) {
        Set<Piece> neighbors = new HashSet<>();

        Adder adder = new Adder(gameMap.width, gameMap.height);

        for (int x = 1; x < 3; x++) {
            for (int y = 1; y < 3; y++) {
                Location newLoc = new Location(adder.addX(own.getLocation().x, x), adder.addX(own.getLocation().y, y));
                neighbors.add(Piece.fromLocation(newLoc, gameMap, gameMap.getSite(newLoc).owner));

                newLoc = new Location(adder.addX(own.getLocation().x, x), adder.subY(own.getLocation().y, y));
                neighbors.add(Piece.fromLocation(newLoc, gameMap, gameMap.getSite(newLoc).owner));

                newLoc = new Location(adder.subX(own.getLocation().x, x), adder.addY(own.getLocation().y, y));
                neighbors.add(Piece.fromLocation(newLoc, gameMap, gameMap.getSite(newLoc).owner));

                newLoc = new Location(adder.subX(own.getLocation().x, x), adder.subY(own.getLocation().y, y));
                neighbors.add(Piece.fromLocation(newLoc, gameMap, gameMap.getSite(newLoc).owner));

                newLoc = new Location(own.getLocation().x, adder.subY(own.getLocation().y, y));
                neighbors.add(Piece.fromLocation(newLoc, gameMap, gameMap.getSite(newLoc).owner));

                newLoc = new Location(own.getLocation().x, adder.addY(own.getLocation().y, y));
                neighbors.add(Piece.fromLocation(newLoc, gameMap, gameMap.getSite(newLoc).owner));

                newLoc = new Location(adder.addX(own.getLocation().x, x), own.getLocation().y);
                neighbors.add(Piece.fromLocation(newLoc, gameMap, gameMap.getSite(newLoc).owner));

                newLoc = new Location(adder.subX(own.getLocation().x, x), own.getLocation().y);
                neighbors.add(Piece.fromLocation(newLoc, gameMap, gameMap.getSite(newLoc).owner));


            }
        }
        return neighbors;
    }

//    int getValueInDirection(Piece from, Direction to) {
//        Site site = gameMap.getSite(from.getLocation(), to);
//        return getNpcOwnEnemyValue(site, myID) + getStrengthValue(site) + getProductionValue(site);
//    }
//
//    int getValueInTwoDirections(Piece from, Direction firstDir, Direction secondDir) {
//        Site firstSite = gameMap.getSite(from.getLocation(), firstDir);
//        return getNpcOwnEnemyValue(firstSite, myID) + getStrengthValue(firstSite) + getProductionValue(firstSite);
//    }


    private static int getNpcOwnEnemyValue(Site site, int myID) {
        if (site.owner == myID) return 0;

        return 30;
    }

    private static int getStrengthValue(Site site) {
        if (site.strength > 240) return 0;
        if (site.strength > 210) return 1;
        if (site.strength > 190) return 2;
        if (site.strength > 170) return 3;
        if (site.strength > 150) return 4;
        if (site.strength > 130) return 5;
        if (site.strength > 110) return 6;
        if (site.strength > 90) return 7;
        if (site.strength > 70) return 8;
        if (site.strength > 60) return 9;
        if (site.strength > 50) return 10;
        if (site.strength > 40) return 11;
        if (site.strength > 30) return 12;
        if (site.strength > 20) return 13;
        if (site.strength > 10) return 13;

        return 15;
    }

    private static int getProductionValue(Site site) {
        if (site.production < 3) return 2;
        if (site.production < 5) return 4;
        if (site.production < 7) return 6;
        if (site.production < 9) return 8;
        if (site.production < 11) return 10;
        if (site.production < 13) return 12;
        if (site.production < 15) return 14;
        return 16;
    }
}
