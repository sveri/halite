import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * this bot will just move to new tiles when it is an enemy /empty tile, otherwise it just
 * grows until the tiles are full
 */
public class MyBot {

    private static final String botName = "sveriJavaBot23_1";

//    private static List<ProductionArea> productionAreas = Collections.emptyList();

//    private static final Map<Location, Location> movedFromTo = new HashMap<>();
//    private static final Map<Location, Location> movedToFrom = new HashMap<>();

    static final Logger logger = new Logger(botName);


    public static void main(String[] args) throws IOException {

        addShutdownHook(logger);
        InitPackage iPackage = Networking.getInit();
        final int myID = iPackage.myID;
        int frameId = 0;


        logger.info("myID: " + myID);
        GameMap gameMap = null;

        Networking.sendInit(botName);

        while (true) {
            ArrayList<Move> moves = new ArrayList<>();

            List<Piece> owns = new ArrayList<>();
            List<Piece> npcs = new ArrayList<>();
            List<Piece> enemies = new ArrayList<>();

            gameMap = Networking.getFrame();

            logger.info("---------" + frameId + ": ");

            collectGameMapPieces(myID, gameMap, owns, npcs, enemies);

            for (Piece own : owns) {
                Direction pieceDirection = Direction.STILL;

                if (own.hasOnlyOwnNeighbors()) {
                    logger.info("own neighbors");
                    if (moveAccordingToOwnStrength(own)) {
                        Collections.sort(enemies, new PieceDistanceSorter(own, gameMap));
                        if (enemies.size() > 0) {
                            pieceDirection = Direction.getDirectionFromToGameMap(own.getLocation(), enemies.get(0).getLocation(), gameMap);
                        }
                    }
                } else {
                    Piece nextPiece = findMostValuablePiece(own, gameMap, myID);

                    if (!nextPiece.isNull() && nextPiece.getStrength() < own.getStrength()) {
                        pieceDirection = nextPiece.getDirection();
                    }

                }

                moves.add(new Move(own.getLocation(), pieceDirection));
            }


            Networking.sendFrame(moves);
            frameId++;
        }
    }

    private static Piece findMostValuablePiece(Piece own, GameMap gameMap, int myID) {

        long endTime = System.nanoTime() + 500000000;
//        long endTime = System.nanoTime() + 100000000;

        final int maxLookAhead = 10;

//        Map<Direction, Integer> dirToValue = new HashMap<>();
        Set<DirectionAndDistanceValue> directionAndDistanceValues = new HashSet<>();

        for (Direction dir : Direction.CARDINALS) {
//            dirToValue.put(dir, 0);

            int i = 1;

            while (System.nanoTime() < endTime && i < maxLookAhead) {
                int curValue = 0;
                Location curLoc = own.getLocation();
                int curDistance = 0;

                for (int j = 0; j < i; j++) {

                    Site curSite = gameMap.getSite(curLoc, dir);
//                    Integer curValue = dirToValue.get(dir);
                    curLoc = gameMap.getLocation(curLoc, dir);
//                dirToValue.put(dir, curValue + getProductionValue(curSite.production) + getStrengthValue(curSite)
//                        + getNpcOwnEnemyValue(curSite, myID));
                    curDistance = (int) gameMap.getDistance(own.getLocation(), curLoc);
                    curValue += (getProductionValue(curSite.production) + getStrengthValue(curSite) + getNpcOwnEnemyValue(curSite, myID))
                            / Math.pow(10, j);

//                        + getDistanceValue(curDistance);
                    directionAndDistanceValues.add(new DirectionAndDistanceValue(dir, curValue, curDistance));

                }

                i++;
            }

//            while (System.nanoTime() < endTime && i < maxLookAhead) {
//                Site curSite = gameMap.getSite(curLoc, dir);
//                Integer curValue = dirToValue.get(dir);
//                curLoc = gameMap.getLocation(curLoc, dir);
//                dirToValue.put(dir, curValue + getProductionValue(curSite.production) + getStrengthValue(curSite)
//                        + getNpcOwnEnemyValue(curSite, myID));
//                i++;
//            }
        }

        if (directionAndDistanceValues.isEmpty()) {
            return NullPiece.newNullPiece();
        }

        List<DirectionAndDistanceValue> distanceValuesSortedByLowValueFirst =
                directionAndDistanceValues.stream().sorted(DirectionAndDistanceValue::compareTo).collect(Collectors.toList());
        Collections.reverse(distanceValuesSortedByLowValueFirst);
        logger.info(distanceValuesSortedByLowValueFirst.subList(0, 4).toString());
//        Map.Entry<Direction, Integer> maxEntry = null;
//
//        for (Map.Entry<Direction, Integer> entry : dirToValue.entrySet()) {
//            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
//                maxEntry = entry;
//            }
//        }

        DirectionAndDistanceValue max = directionAndDistanceValues.stream().max(DirectionAndDistanceValue::compareTo).get();
        logger.info("max: " + directionAndDistanceValues.stream().max(DirectionAndDistanceValue::compareTo).get());
        return Piece.fromLocationAndDirection(own.getLocation(), gameMap, max.getDirection());
    }

//    private static int getDistanceValue(int distance) {
//        if(distance == 1) return 90;
//        if(distance == 2) return 80;
//        if(distance == 3) return 70;
//        if(distance == 4) return 60;
//        if(distance == 5) return 40;
//        if(distance == 7) return 30;
//        if(distance == 8) return 20;
//        if(distance == 9) return 5;
//
//        return 1;
//    }

    private static int getNpcOwnEnemyValue(Site curSite, int myID) {
        if (curSite.owner == myID) return 0;

        return 20;
    }

    private static int getStrengthValue(Site curSite) {
        if (curSite.strength > 240) return 0;
        if (curSite.strength > 210) return 1;
        if (curSite.strength > 190) return 2;
        if (curSite.strength > 170) return 3;
        if (curSite.strength > 150) return 4;
        if (curSite.strength > 130) return 5;
        if (curSite.strength > 110) return 6;
        if (curSite.strength > 90) return 7;
        if (curSite.strength > 70) return 8;
        if (curSite.strength > 60) return 9;
        if (curSite.strength > 50) return 10;
        if (curSite.strength > 40) return 11;
        if (curSite.strength > 30) return 12;
        if (curSite.strength > 20) return 13;
        if (curSite.strength > 10) return 14;

        return 15;
    }

    private static int getProductionValue(int production) {
        if (production < 1) return 0;
        if (production < 2) return 2;
        if (production < 3) return 4;
        if (production < 5) return 6;
        if (production < 7) return 8;
        if (production < 9) return 10;
        if (production < 11) return 12;
        if (production < 13) return 14;
        if (production < 15) return 16;
        return 18;
    }

// convolutional neural network
// small area around the thing to move
// same network for every single piece -> 5 output neurons
// stochastic gradient descentor

// value production over strength

    private static void collectGameMapPieces(int myID, GameMap gameMap, List<Piece> owns, List<Piece> npcs, List<Piece> enemies) {
        for (int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {
                Location curLoc = new Location(x, y);
                Site curSite = gameMap.getSite(curLoc);
                Piece piece = Piece.fromLocation(curLoc, gameMap, curSite.owner);
                if (piece.getOwner() == 0) {
                    npcs.add(piece);
                } else if (piece.getOwner() == myID) {
                    owns.add(piece);
                } else {
                    enemies.add(piece);
                }
            }
        }
    }

    private static boolean moveAccordingToOwnStrength(Piece p) {
        return (p.getProduction() == 0
                || p.getProduction() == 1 && p.getStrength() > 2)
                || (p.getProduction() == 2 && p.getStrength() > 5)
                || (p.getProduction() == 3 && p.getStrength() > 10)
                || (p.getProduction() == 4 && p.getStrength() > 15)
                || (p.getProduction() == 5 && p.getStrength() > 20)
                || (p.getProduction() == 6 && p.getStrength() > 25)
                || (p.getProduction() == 7 && p.getStrength() > 30)
                || (p.getProduction() == 8 && p.getStrength() > 35)
                || (p.getProduction() == 9 && p.getStrength() > 40)
                || (p.getProduction() == 10 && p.getStrength() > 45)
                || (p.getProduction() == 11 && p.getStrength() > 50)
                || (p.getProduction() == 12 && p.getStrength() > 60)
                || (p.getProduction() == 13 && p.getStrength() > 70)
                || (p.getProduction() == 14 && p.getStrength() > 80)
                || (p.getProduction() == 15 && p.getStrength() > 90)
                || (p.getProduction() == 16 && p.getStrength() > 100);

    }

    private static void addShutdownHook(Logger logger) {

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    logger.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}


// finding highest production rank, need something different I guess
//                Piece nextPiece = ProductionArea.getNextHighestProductionPiece(own, productionAreas, gameMap);
//
//                if (nextPiece != null) {
//                    Direction possibleNextDirection = Direction.getDirectionFromTo(own.getLocation(), nextPiece.getLocation(), gameMap.height, gameMap.width);
//                    Piece possibleNextPiece = Piece.fromLocationAndDirection(own.getLocation(), gameMap, possibleNextDirection);
//                    if(possibleNextPiece.getStrength() < own.getStrength())
//                        pieceDirection = possibleNextDirection;
//                }


// trying to omit pieces that are worth nothing - did not work yet
//                if (own.hasOnlyOwnNeighbors()) {
//                    if (moveAccordingToOwnStrength(own)) {
//                        Collections.sort(enemies, new PieceDistanceSorter(own, gameMap));
//                        for (Piece enemy : enemies) {
//                            Direction toEnemy = Direction.getDirectionFromTo(own.getLocation(), enemy.getLocation(), gameMap.height, gameMap.width);
//                            Piece toEnemyPiece = Piece.fromLocationAndDirection(own.getLocation(), gameMap, toEnemy);
//                            if (toEnemyPiece.isOwnMoveablePiece(owns)) {
//                                pieceDirection = Direction.getDirectionFromTo(own.getLocation(), enemy.getLocation(), gameMap.height, gameMap.width);
//                                break;
//                            }
//                        }
//                    }
//                } else {
//                    Piece bestNonOwnNeighbor = own.getBestNonOwnNeighbor(owns);
////                    if(bestNonOwnNeighbor.isNull()) {
////                        List<Piece> ownNeighbors = own.getOwnNeighbors();
////                    }
//                    if (!bestNonOwnNeighbor.isNull() && bestNonOwnNeighbor.getStrength() < own.getStrength()) {
//                        pieceDirection = Direction.getDirectionFromTo(own.getLocation(), bestNonOwnNeighbor.getLocation(), gameMap.height, gameMap.width);
//                    }
//                }