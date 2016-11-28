import java.util.Comparator;

/**
 * Created by sveri on 25.11.16.
 */
public class ProductionAreaProductionSorter implements Comparator<ProductionArea> {

    private final GameMap gameMap;

    public ProductionAreaProductionSorter(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public int compare(ProductionArea o1, ProductionArea o2) {
        int prod1 = ProductionArea.getMeanAreaProduction(o1.getTopLeft(), gameMap);
        int prod2 = ProductionArea.getMeanAreaProduction(o2.getTopLeft(), gameMap);

        if(prod1 == prod2) return 0;
        if(prod1 < prod2) return -1;

        return 1;
    }
}
