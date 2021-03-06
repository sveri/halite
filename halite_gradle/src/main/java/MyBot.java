import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * this bot will just move to new tiles when it is an enemy /empty tile, otherwise it just
 * grows until the tiles are full
 */
public class MyBot {

    private static final String botName = "sveriJavaBot33";

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
                Set<Piece> enemiesInDistance = own.getEnemiesInDistance(2, enemies);

                if (!enemiesInDistance.isEmpty()) {
                    Piece enemy = own.findDirectionWithMostEnemies(enemiesInDistance);
                    if ((enemy.getOwner() == 0 && enemy.getStrength() < own.getStrength())
                            || enemy.getOwner() != 0) {
                        pieceDirection = Direction.getDirectionFromToGameMap(own.getLocation(), enemy.getLocation(), gameMap);
                    }
                } else if (own.isInside()) {
                    if (moveAccordingToOwnStrength(own)) {

                        Optional<Piece> enemy = enemies.stream().filter(piece -> piece.getProduction() > 6)
                                .sorted(new PieceDistanceSorter(own, gameMap)).findFirst();
                        if (enemy.isPresent() && gameMap.getDistance(own.getLocation(), enemy.get().getLocation()) < gameMap.width / 4) {
                            pieceDirection = Direction.getDirectionFromToGameMap(own.getLocation(), enemy.get().getLocation(), gameMap);
                        } else {
                            Piece nextPiece = findMostValuablePiece(own, gameMap, myID, true);
//
                            if (!nextPiece.isNull()) {
                                pieceDirection = nextPiece.getDirection();
                            }
                        }
                    }
                } else {
                    Piece nextPiece = findMostValuablePiece(own, gameMap, myID, false);

                    if (!nextPiece.isNull()
                            && (nextPiece.getStrength() < own.getStrength() && nextPiece.getOwner() != myID)
                                || nextPiece.getOwner() == myID && moveAccordingToOwnStrength(own)) {
                        pieceDirection = nextPiece.getDirection();
                    }

                }

                moves.add(new Move(own.getLocation(), pieceDirection));
            }

            Networking.sendFrame(moves);
            frameId++;
        }
    }


    private static Piece findMostValuablePiece(Piece own, GameMap gameMap, int myID, boolean fromInside) {

        final int maxLookAhead = getMaxLookAhead(gameMap);

        Map<Direction, Double> dirToValue = new HashMap<>();

        for (Direction dir : Direction.CARDINALS) {
            dirToValue.put(dir, 0.);
            Location curLoc = own.getLocation();
            int i = 0;
            int breakAfterThese = 0;

            while (i < maxLookAhead && breakAfterThese < 100) {
                Site curSite = gameMap.getSite(curLoc, dir);
                Double curValue = dirToValue.get(dir);
                curLoc = gameMap.getLocation(curLoc, dir);

                if (curSite.owner != myID) {
                    curValue += curSite.production / getStrengthValue(curSite);
                }

                dirToValue.put(dir, curValue);

                if (!fromInside) {
                    i++;
                } else if (fromInside && curSite.owner != myID) {
                    i++;
                }

                breakAfterThese++;
            }
        }

        if (dirToValue.isEmpty()) {
            return NullPiece.newNullPiece();
        }

        Map.Entry<Direction, Double> maxEntry = null;

        for (Map.Entry<Direction, Double> entry : dirToValue.entrySet()) {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
        }

        return Piece.fromLocationAndDirection(own.getLocation(), gameMap, maxEntry.getKey());
    }

    private static int getMaxLookAhead(GameMap gameMap) {
        if (gameMap.width < 21) return 3;
        if (gameMap.width < 31) return 4;
        if (gameMap.width < 41) return 5;
        return 6;
    }

    private static double getStrengthValue(Site curSite) {
        if(curSite.strength == 0) return 1.;
        return curSite.strength;
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
                || p.getProduction() == 1 && p.getStrength() > 5)
                || (p.getProduction() == 2 && p.getStrength() > 10)
                || (p.getProduction() == 3 && p.getStrength() > 15)
                || (p.getProduction() == 4 && p.getStrength() > 20)
                || (p.getProduction() == 5 && p.getStrength() > 25)
                || (p.getProduction() == 6 && p.getStrength() > 30)
                || (p.getProduction() == 7 && p.getStrength() > 35)
                || (p.getProduction() == 8 && p.getStrength() > 40)
                || (p.getProduction() == 9 && p.getStrength() > 45)
                || (p.getProduction() == 10 && p.getStrength() > 50)
                || (p.getProduction() == 11 && p.getStrength() > 55)
                || (p.getProduction() == 12 && p.getStrength() > 60)
                || (p.getProduction() == 13 && p.getStrength() > 65)
                || (p.getProduction() == 14 && p.getStrength() > 70)
                || (p.getProduction() == 15 && p.getStrength() > 75)
                || (p.getProduction() == 16 && p.getStrength() > 80);

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