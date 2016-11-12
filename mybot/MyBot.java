import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * this bot will just move to new tiles when it is an enemy /empty tile, otherwise it just
 * grows until the tiles are full
 */
public class MyBot {

    static Logger logger;
    static boolean logNow = false;

    static int frame = 0;

    private static final String botName = "sveriBot";

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

            GeneralGameInformation gameInformation = getGeneralGameInformation(gameMap, myID);

            for (int y = 0; y < gameMap.height; y++) {
                for (int x = 0; x < gameMap.width; x++) {
                    Site curSite = gameMap.getSite(new Location(x, y));
                    Location curLoc = new Location(x, y);
                    Piece curPiece = new Piece(curLoc, curSite, gameMap);

                    if (curSite.owner == myID) {


                        List<Piece> neighbors = curPiece.getNeighbors();

                        Piece shouldMovePiece = shouldMove(curPiece, neighbors, myID, gameInformation);

                        if (shouldMovePiece != null) {
                            Direction directionTo = curPiece.getDirectionTo(shouldMovePiece.getLoc(), myID);
                            if(gameMap.getSite(curLoc, directionTo).strength + curPiece.getStrength() < 400)
                                moves.add(new Move(curLoc, directionTo));
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

    private static GeneralGameInformation getGeneralGameInformation(GameMap gameMap, int myID) {
        GeneralGameInformation info = new GeneralGameInformation();
        for (int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {
                Site site = gameMap.getSite(new Location(x, y));
                if(site.owner == 0) info.addOneToNpcTilesCount();
                else if(site.owner == myID) info.addOneToOwnTilesCount();
                else info.addOneToEnemyTilesCount();
            }

        }

        return info;
    }

    private static Piece shouldMove(Piece curPiece, List<Piece> neighbors, int myID, GeneralGameInformation gameInformation) {

        if (curPiece.getProduction() == 6 && curPiece.getStrength() < 250 && gameInformation.getOwnTilesCount() > 10)
            return null;


        Piece weakestEnemyNeighbor = Piece.getWeakestEnemyNeighbor(neighbors, myID);
        if (weakestEnemyNeighbor != null && weakestEnemyNeighbor.getStrength() < curPiece.getStrength()) {
            return weakestEnemyNeighbor;
        }

        if(curPiece.getStrength() > 100)
            return curPiece.findNextEnemyOrNPCPiece(myID);


        return null;
    }

    private static void addShutdownHook(final Logger logger) {

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
