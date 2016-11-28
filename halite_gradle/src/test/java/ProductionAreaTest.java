import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * Created by sveri on 28.11.16.
 */
public class ProductionAreaTest {

    @Test
    public void testMeanAverage() {
        GameMap gameMap = GameMapHelper.newGameMap();

        Assert.assertEquals(1, ProductionArea.getMeanAreaProduction(new Location(0, 0), gameMap));
        Assert.assertEquals(2, ProductionArea.getMeanAreaProduction(new Location(8, 8), gameMap));
        Assert.assertEquals(13, ProductionArea.getMeanAreaProduction(new Location(4, 1), gameMap));
    }

    @Test
    public void createFromLocation() {
        GameMap gameMap = GameMapHelper.newGameMap();

        ProductionArea productionArea = ProductionArea.fromLocation(new Location(0, 0), gameMap);
        Assert.assertEquals(1, productionArea.getMeanProduction());

        productionArea = ProductionArea.fromLocation(new Location(4, 1), gameMap);
        Assert.assertEquals(13, productionArea.getMeanProduction());
    }

    @Test
    public void isOwnedBy() {
        GameMap gameMap = GameMapHelper.newGameMap();

        ProductionArea productionArea = ProductionArea.fromLocation(new Location(8, 8), gameMap);
        Assert.assertFalse(productionArea.isOwnedByX(1, gameMap));

        productionArea = ProductionArea.fromLocation(new Location(2, 2), gameMap);
        Assert.assertTrue(productionArea.isOwnedByX(0, gameMap));
        Assert.assertFalse(productionArea.isOwnedByX(1, gameMap));

    }

    @Test
    public void nextHighesProductionPiece() {
        GameMap gameMap = GameMapHelper.newGameMap();
        List<ProductionArea> productionAreas = Collections.emptyList();
        productionAreas = ProductionArea.collectProductionAreas(gameMap, productionAreas);

        ProductionArea productionArea = ProductionArea.fromLocation(new Location(0, 0), gameMap);
        Piece nextHighestProductionPiece = ProductionArea.getNextHighestProductionPiece(Piece.fromLocation(new Location(0, 0), gameMap, 1), productionAreas, gameMap);


//        productionArea.
//                Assert.assertEquals(1, productionArea.getMeanProduction());
//
//        productionArea = ProductionArea.fromLocation(new Location(4, 1), gameMap);
        Assert.assertEquals(4, nextHighestProductionPiece.getLocation().x);
        Assert.assertEquals(1, nextHighestProductionPiece.getLocation().y);
    }


}
