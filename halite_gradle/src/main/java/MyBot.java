import java.io.IOException;
import java.util.*;


/**
 * this bot will just move to new tiles when it is an enemy /empty tile, otherwise it just
 * grows until the tiles are full
 */
public class MyBot {

    private static final String botName = "sveriJavaBot";

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

//            productionAreas = ProductionArea.collectProductionAreas(gameMap, productionAreas);

            for (Piece own : owns) {
                Direction pieceDirection = Direction.STILL;

                if (own.hasOnlyOwnNeighbors()) {
                    if (moveAccordingToOwnStrength(own)) {
                        Collections.sort(enemies, new PieceDistanceSorter(own, gameMap));
                        for (Piece enemy : enemies) {
                            pieceDirection = Direction.getDirectionFromTo(own.getLocation(), enemy.getLocation(), gameMap.height, gameMap.width);
                        }
                    }
                } else {
                    Piece nextPiece = MoveValueCalculator.findMostValuablePiece(own, gameMap, myID);

                    if (!nextPiece.isNull() && nextPiece.getStrength() < own.getStrength()) {
                        pieceDirection = nextPiece.getDirection();
                    }

                }

//                if (pieceDirection != Direction.STILL) {
//                    Location fromLoc = own.getLocation();
//                    Location toLoc = gameMap.getLocation(own.getLocation(), pieceDirection);
//
////                    if (movedToFrom.containsKey(fromLoc) && movedToFrom.get(fromLoc).equals(toLoc)) {
//////                        Location oldFrom = movedToFrom.get(fromLoc);
//////                        if (oldFrom.equals(toLoc)) {
////                            pieceDirection = Direction.randomDirection();
////                            movedToFrom.put(gameMap.getLocation(own.getLocation(), pieceDirection), fromLoc);
//////                        }
////                    } else {
////                        movedToFrom.put(toLoc, fromLoc);
////                    }
//
////                    if(movedFromTo.get(toLoc).equals(fromLoc) && )
////                    movedFromTo.put(fromLoc, toLoc);
//                }

                moves.add(new Move(own.getLocation(), pieceDirection));
            }


            Networking.sendFrame(moves);
            frameId++;
        }
    }

//    private static Piece findMostValuablePiece(Piece own, GameMap gameMap, int myID) {
//
////        long endTime = System.nanoTime() + 500000000;
////        long endTime = System.nanoTime() + 100000000;
//
//        final int maxLookAhead = 100;
//
//        Map<Direction, Integer> dirToValue = new HashMap<>();
//
//        for (Direction dir : Direction.CARDINALS) {
//            long endTime = System.nanoTime() + 100000000;
//            dirToValue.put(dir, 0);
//            Location curLoc = own.getLocation();
////            int i = 0;
////
////            //            if (curSite.owner != myID) {
////            while (System.nanoTime() < endTime && i < maxLookAhead) {
////                logger.info(System.nanoTime() + " - " + endTime);
////                Site curSite = gameMap.getSite(curLoc, dir);
////                //                if (i == maxLookAhead - 1 && curSite.owner == myID) {
////                //                    dirToValue.remove(dir);
////                //                } else {
////                Integer curValue = dirToValue.get(dir);
////                curLoc = gameMap.getLocation(curLoc, dir);
////                dirToValue.put(dir, curValue + getProductionValue(curSite.production) + getStrengthValue(curSite)
////                        + getNpcOwnEnemyValue(curSite, myID));
////                //                }
////                i++;
////            }
//            //            }
//        }
//
//        if (dirToValue.isEmpty()) {
//            return NullPiece.newNullPiece();
//        }
//
//        Map.Entry<Direction, Integer> maxEntry = null;
//
//        for (Map.Entry<Direction, Integer> entry : dirToValue.entrySet()) {
//            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
//                maxEntry = entry;
//            }
//        }
//
//        return Piece.fromLocationAndDirection(own.getLocation(), gameMap, maxEntry.getKey());
//    }

//    private static int getNpcOwnEnemyValue(Site curSite, int myID) {
//        if (curSite.owner == myID) return 0;
//
//        return 10;
//    }
//
//    private static int getStrengthValue(Site curSite) {
//        if (curSite.strength > 240) return 0;
//        if (curSite.strength > 210) return 1;
//        if (curSite.strength > 190) return 2;
//        if (curSite.strength > 170) return 3;
//        if (curSite.strength > 150) return 4;
//        if (curSite.strength > 130) return 5;
//        if (curSite.strength > 110) return 6;
//        if (curSite.strength > 90) return 7;
//        if (curSite.strength > 70) return 8;
//        if (curSite.strength > 60) return 9;
//        if (curSite.strength > 50) return 10;
//        if (curSite.strength > 40) return 11;
//        if (curSite.strength > 30) return 12;
//        if (curSite.strength > 20) return 13;
//        if (curSite.strength > 10) return 13;
//
//        return 15;
//    }
//
//    private static int getProductionValue(int production) {
//        if (production < 3) return 2;
//        if (production < 5) return 4;
//        if (production < 7) return 6;
//        if (production < 9) return 8;
//        if (production < 11) return 10;
//        if (production < 13) return 12;
//        if (production < 15) return 14;
//        return 16;
//    }

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
                || (p.getProduction() == 4 && p.getStrength() > 20)
                || (p.getProduction() == 5 && p.getStrength() > 30)
                || (p.getProduction() == 6 && p.getStrength() > 40)
                || (p.getProduction() == 7 && p.getStrength() > 50)
                || (p.getProduction() == 8 && p.getStrength() > 60)
                || (p.getProduction() == 9 && p.getStrength() > 65)
                || (p.getProduction() == 10 && p.getStrength() > 70)
                || (p.getProduction() == 11 && p.getStrength() > 75)
                || (p.getProduction() == 12 && p.getStrength() > 80)
                || (p.getProduction() == 13 && p.getStrength() > 90)
                || (p.getProduction() == 14 && p.getStrength() > 100)
                || (p.getProduction() == 15 && p.getStrength() > 105)
                || (p.getProduction() == 16 && p.getStrength() > 110);

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