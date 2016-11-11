import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * this bot will just move to new tiles when it is an enemy /empty tile, otherwise it just
 * grows until the tiles are full
 */
public class FourthBot {

    private static final String botName = "fourthBot";

    public static void main(String[] args) throws IOException {
        final Logger logger = new Logger(botName);
        addShutdownHook(logger);

        InitPackage iPackage = Networking.getInit();
        int myID = iPackage.myID;


        logger.info("myID: " + myID);
        GameMap gameMap = null;

        Networking.sendInit(botName);

        while (true) {
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
                            logger.info(curPiece.toString());
//                            logger.info(weakestNeigbhor.toString());
//                            logger.info(curPiece.getDirectionTo(weakestNeigbhor.getLoc()).toString());
                            moves.add(new Move(curLoc, curPiece.getDirectionTo(shouldMovePiece.getLoc())));
                        } else {
                            moves.add(new Move(curLoc, Direction.STILL));
                        }
                    }
                }
            }

            Networking.sendFrame(moves);
        }
    }

    private static Piece shouldMove(Piece curPiece, List<Piece> neighbors, int myID) {

        if (curPiece.getProduction() > 5 && curPiece.getStrength() < 240) return null;

        Piece weakestEnemyNeighbor = Piece.getWeakestEnemyNeighbor(neighbors, myID);
        if (weakestEnemyNeighbor != null && weakestEnemyNeighbor.getSite().strength < curPiece.getStrength())
            return weakestEnemyNeighbor;

//        Piece weakestOwnNeighbor = Piece.getWeakestOwnNeighbor(neighbors, myID);

        if(curPiece.getStrength() > 100)
            return curPiece.findPieceInNextEnemyDirection(myID);

//        if (weakestOwnNeighbor != null && weakestOwnNeighbor.getSite().strength < curPiece.getStrength()
//                && curPiece.getStrength() > 100 && weakestOwnNeighbor.getSite().strength > 10)
//            return weakestOwnNeighbor;


        return null;
    }

//    private static boolean shouldMove(Site curSite, Piece weakestNeigbhor, int myID) {
//        if (curSite.production > 5 && curSite.strength < 200) return false;
//        if (weakestNeigbhor != null && weakestNeigbhor.getSite().owner != myID) return true;
//
//        return false;
//    }

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
