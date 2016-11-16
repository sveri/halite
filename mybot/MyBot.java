import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * this bot will just move to new tiles when it is an enemy /empty tile, otherwise it just
 * grows until the tiles are full
 */
public class MyBot {

    static Logger logger;

    private static final String botName = "sveriBot";

    private static GameMap gameMap = null;

    public static int frame = 0;

    static int npcID= 0;

    static Piece nextPieceToConquer = new NullPiece();

    static int findNextPieceToConquerInXFrames = 0;

    public static void main(String[] args) throws IOException {
        logger = new Logger(botName);
        addShutdownHook(logger);

        InitPackage iPackage = Networking.getInit();
        int myID = iPackage.myID;

        logger.info("\n");
        logger.info("myID: " + myID);


        Networking.sendInit(botName);




        while (true) {
            ArrayList<Move> moves = new ArrayList<>();
            gameMap = Networking.getFrame();

            GeneralGameInformation gameInformation = GeneralGameInformation.fromGeneralGameInformation(gameMap, myID);
            gameInformation.setNextPieceToConquer(gameInformation.getOwns().get(0));

            logger.info("Frame: " + frame);
            logger.info("first piece to conquer: " + nextPieceToConquer);

            List<Piece> ownPiecesFromOuterToInner = gameInformation.getOwnPiecesFromOuterToInner(myID);

            for (Piece piece : ownPiecesFromOuterToInner) {
                Direction direction = piece.findNextDirection(myID, gameInformation);
                moves.add(new Move(piece.getLoc(), direction));
            }

            Networking.sendFrame(moves);
            frame++;
        }
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
