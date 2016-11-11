import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * this bot will just move to new tiles when it is an enemy /empty tile, otherwise it just
 * grows until the tiles are full
 */
public class FithBot {

    static Logger logger;
    static boolean logNow = false;

    static int frame = 0;

    private static final String botName = "FithBot";

    public static void main(String[] args) throws IOException {
        logger = new Logger(botName);
        addShutdownHook(logger);

        InitPackage iPackage = Networking.getInit();
        int myID = iPackage.myID;


        logger.info("myID: " + myID);
        GameMap gameMap = null;

        Networking.sendInit(botName);

        while (true) {
            logger.info("Frame: " + frame);
            ArrayList<Move> moves = new ArrayList<>();

            gameMap = Networking.getFrame();

            for (int y = 0; y < gameMap.height; y++) {
                for (int x = 0; x < gameMap.width; x++) {
                    Site curSite = gameMap.getSite(new Location(x, y));
                    Location curLoc = new Location(x, y);
                    Piece curPiece = new Piece(curLoc, curSite, gameMap);

                    if (curSite.owner == myID) {


                        List<Piece> neighbors = curPiece.getNeighbors();

                        Piece shouldMovePiece = shouldMove(curPiece, neighbors, myID);

                        if (shouldMovePiece != null) {
//                            logger.info(curPiece.toString());
//                            logger.info(weakestNeigbhor.toString());
//                            logger.info(curPiece.getDirectionTo(weakestNeigbhor.getLoc()).toString());
                            moves.add(new Move(curLoc, curPiece.getDirectionTo(shouldMovePiece.getLoc())));
                            if(logNow)
                            {
                                logger.info(curPiece.getDirectionTo(shouldMovePiece.getLoc()).toString());
                                logNow =false;
                            }
                        } else {
                            moves.add(new Move(curLoc, Direction.STILL));
                        }
                    }
                }
            }

            Networking.sendFrame(moves);
            frame++;
        }
    }

    private static Piece shouldMove(Piece curPiece, List<Piece> neighbors, int myID) {

        if (curPiece.getProduction() == 6 && curPiece.getStrength() < 250) return null;

        Piece weakestEnemyNeighbor = Piece.getWeakestEnemyNeighbor(neighbors, myID);
        if (weakestEnemyNeighbor != null && weakestEnemyNeighbor.getStrength() < curPiece.getStrength()) {
            return weakestEnemyNeighbor;
        }

        if(curPiece.getStrength() > 100)
            return curPiece.findPieceInNextEnemyDirection(myID);


        return null;
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
