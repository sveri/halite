import java.util.Comparator;

/**
 * Created by sveri on 25.11.16.
 */
public class ProductionAreaDistanceSorter implements Comparator<ProductionArea> {

    private final Piece fromPiece;
    private final GameMap gameMap;

    public ProductionAreaDistanceSorter(Piece fromPiece, GameMap gameMap) {
        this.fromPiece = fromPiece;
        this.gameMap = gameMap;
    }

    @Override
    public int compare(ProductionArea o1, ProductionArea o2) {
        double fromToP1 = gameMap.getDistance(fromPiece.getLocation(), o1.getTopLeft());
        double fromToP2 = gameMap.getDistance(fromPiece.getLocation(), o2.getTopLeft());

        if(fromToP1 == fromToP2) return 0;
        if(fromToP1 < fromToP2) return -1;

        return 1;
    }
}
