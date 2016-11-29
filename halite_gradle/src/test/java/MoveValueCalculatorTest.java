import org.junit.Test;

import java.util.List;
import java.util.Set;

/**
 * Created by sveri on 29.11.16.
 */
public class MoveValueCalculatorTest {

    @Test
    public void getNeighborPieces() {
        GameMap gameMap = GameMapHelper.newGameMap();
        Set<Piece> neighborPieces = MoveValueCalculator.getNeighborPieces(Piece.fromLocation(new Location(1, 1), gameMap, 1), gameMap);
        System.out.println(neighborPieces);
    }
}
