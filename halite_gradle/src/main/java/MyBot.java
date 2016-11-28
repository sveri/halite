import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * this bot will just move to new tiles when it is an enemy /empty tile, otherwise it just
 * grows until the tiles are full
 */
public class MyBot {

    private static final String botName = "sveriJavaBot";

    private static List<ProductionArea> productionAreas = Collections.emptyList();


    public static void main(String[] args) throws IOException {
        final Logger logger = new Logger(botName);
        addShutdownHook(logger);
        InitPackage iPackage = Networking.getInit();
        final int myID = iPackage.myID;


        logger.info("myID: " + myID);
        GameMap gameMap = null;

        Networking.sendInit(botName);

        while (true) {
            ArrayList<Move> moves = new ArrayList<>();

            List<Piece> owns = new ArrayList<>();
            List<Piece> npcs = new ArrayList<>();
            List<Piece> enemies = new ArrayList<>();

            gameMap = Networking.getFrame();

            collectGameMapPieces(myID, gameMap, owns, npcs, enemies);

            productionAreas = ProductionArea.collectProductionAreas(gameMap, productionAreas);

            for (Piece own : owns) {
                Direction pieceDirection = Direction.STILL;

                Piece nextPiece = ProductionArea.getNextHighestProductionPiece(own, productionAreas, gameMap);

                if (nextPiece != null) {
                    Direction possibleNextDirection = Direction.getDirectionFromTo(own.getLocation(), nextPiece.getLocation(), gameMap.height, gameMap.width);
                    Piece possibleNextPiece = Piece.fromLocationAndDirection(own.getLocation(), gameMap, possibleNextDirection);
                    if(possibleNextPiece.getStrength() < own.getStrength())
                        pieceDirection = possibleNextDirection;
                }

                moves.add(new Move(own.getLocation(), pieceDirection));
            }


            Networking.sendFrame(moves);
        }
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