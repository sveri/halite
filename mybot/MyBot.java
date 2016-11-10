import java.io.IOException;
import java.util.ArrayList;


/**
 * this bot will just move to new tiles when it is an enemy /empty tile, otherwise it just
 * grows until the tiles are full
 */
public class MyBot {

    private static final String botName = "sveriBot";

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
                    Piece curPiece = new Piece(curLoc, curSite);

                    if (curSite.owner == myID) {


                        Piece weakestEnemyNeigbhor = curPiece.findWeakestEnemyNeigbhor(gameMap, myID);

                        if(weakestEnemyNeigbhor != null) {
                            logger.info(curPiece.toString());
                            logger.info(weakestEnemyNeigbhor.toString());
                            logger.info(curPiece.getDirectionTo(weakestEnemyNeigbhor.getLoc()).toString());
                            moves.add(new Move(curLoc, curPiece.getDirectionTo(weakestEnemyNeigbhor.getLoc())));
                        } else {
                            moves.add(new Move(curLoc, Direction.STILL));
                        }

//                        Direction weakestDirection = Neighbor.getWeakestNeighborDirection(gameMap, curLoc, curSite);
//
//                        if(weakestDirection != null){
//                            moves.add(new Move(curLoc, weakestDirection));
//                        } else {
//                            moves.add(new Move(curLoc, Direction.STILL));
//                        }
                    }
                }
            }

            Networking.sendFrame(moves);
        }
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
