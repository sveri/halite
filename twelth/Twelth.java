import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * this bot will just move to new tiles when it is an enemy /empty tile, otherwise it just
 * grows until the tiles are full
 */
public class Twelth {

    static Logger logger;

    private static int frame = 0;

    private static final String botName = "twelthBot";

    private static GameMap gameMap = null;

    static int npcID = 0;

    public static void main(String[] args) throws IOException {
        logger = new Logger(botName);
        addShutdownHook(logger);

        InitPackage iPackage = Networking.getInit();
        int myID = iPackage.myID;


        logger.info("myID: " + myID);


        Networking.sendInit(botName);

        while (true) {
            logger.info("Frame: " + frame);
            ArrayList<Move> moves = new ArrayList<>();

            gameMap = Networking.getFrame();

            GeneralGameInformation gameInformation = getGeneralGameInformation(gameMap, myID);

            List<Piece> ownPiecesFromOuterToInner = gameInformation.getOwnPiecesFromOuterToInner(myID);

            for (Piece piece : ownPiecesFromOuterToInner) {
                Direction direction = piece.findNextDirection(myID, gameInformation);
                moves.add(new Move(piece.getLoc(), direction));
            }

            Networking.sendFrame(moves);
            frame++;
        }
    }

    private static GeneralGameInformation getGeneralGameInformation(GameMap gameMap, int myID) {
        GeneralGameInformation info = new GeneralGameInformation();
        for (int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {
                Piece piece = Piece.fromLocation(x, y, gameMap);
                if(piece.getOwner() == 0) info.addToNpcs(piece);
                else if(piece.getOwner() == myID) info.addToOwn(piece);
                else info.addToEnemies(piece);
            }

        }

        return info;
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
