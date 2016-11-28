import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class ProductionArea {

    private static int areaSize = 2;

    enum ProductionAreaType {
        VERY_HIGH, HIGH, MIDDLE, LOW, VERY_LOW;
    }

    private Location topLeft;
    private Location bottomRight;

    private final ProductionAreaType type;
    private final int meanProduction;

    ProductionArea(Location topLeft, Location bottomRight, ProductionAreaType type, int meanProduction) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        this.type = type;
        this.meanProduction = meanProduction;
    }

    static ProductionArea fromLocation(Location loc, GameMap gameMap) {
        int production = getMeanAreaProduction(loc, gameMap);
        ProductionArea.ProductionAreaType type = ProductionArea.ProductionAreaType.VERY_LOW;

        if(production > 3 && production <= 5 ) type = ProductionArea.ProductionAreaType.LOW;
        else if(production > 5 && production <= 7 ) type = ProductionArea.ProductionAreaType.MIDDLE;
        else if(production > 7 && production <= 13 ) type = ProductionArea.ProductionAreaType.HIGH;
        else if(production > 13 && production <= 16 ) type = ProductionArea.ProductionAreaType.VERY_HIGH;


        return new ProductionArea(loc, new Location(loc.x + areaSize, loc.y + areaSize), type, production);
    }

    static int getMeanAreaProduction(Location topLeft, GameMap gameMap) {
        int totalProduction = 0;
        for(int x = topLeft.x; x < topLeft.x + areaSize; x++) {
            for (int y = topLeft.y; y < topLeft.y + areaSize; y++) {
                totalProduction += gameMap.getSite(new Location(x, y)).production;
            }
        }

        return totalProduction / (areaSize * areaSize);
    }

    static List<ProductionArea> collectProductionAreas(GameMap gameMap, List<ProductionArea> productionAreas) {
        if (productionAreas.isEmpty()) {
            productionAreas = new ArrayList<>();
            for (int y = 0; y < gameMap.height; y++) {
                for (int x = 0; x < gameMap.width; x++) {
                    if(x >= gameMap.width - areaSize) break;
                    productionAreas.add(ProductionArea.fromLocation(new Location(x, y), gameMap));
//
//                    x++;
//                    y++;
                }
                if(y >= gameMap.height - areaSize) break;
            }
        }
        return productionAreas;
    }

    boolean isOwnedByX(int owner, GameMap gameMap) {
        for (int x = topLeft.x; x < bottomRight.x; x++) {
            for (int y = topLeft.y; y < bottomRight.y; y++) {
                if (gameMap.getSite(new Location(x, y)).owner != owner)
                    return false;
            }
        }

        return true;
    }

    static Piece getNextHighestProductionPiece(Piece fromPiece, List<ProductionArea> areas, GameMap gameMap, List<Piece> owns) {
        int maxDistance = 100;
        if(owns.size() < 10) maxDistance = 5;
        if(owns.size() < 50) maxDistance = 8;

        final int reallyMaxDistance = 4;

        List<ProductionArea> sortedByDistance = areas.stream().sorted(new ProductionAreaDistanceSorter(fromPiece, gameMap))
                .filter(productionArea -> gameMap.getDistance(fromPiece.getLocation(), productionArea.getTopLeft()) < reallyMaxDistance)
                .filter(productionArea -> !productionArea.isOwnedByX(1, gameMap)).collect(Collectors.toList());

        List<ProductionArea> typedAreas = filterByProductionType(sortedByDistance, ProductionAreaType.VERY_HIGH);
        if (typedAreas.isEmpty()) typedAreas = filterByProductionType(sortedByDistance, ProductionAreaType.HIGH);
        if (typedAreas.isEmpty()) typedAreas = filterByProductionType(sortedByDistance, ProductionAreaType.MIDDLE);
        if (typedAreas.isEmpty()) typedAreas = filterByProductionType(sortedByDistance, ProductionAreaType.LOW);
        if (typedAreas.isEmpty()) typedAreas = filterByProductionType(sortedByDistance, ProductionAreaType.VERY_LOW);

        if (typedAreas.isEmpty())
            return null;

        ProductionArea resultingArea = typedAreas.get(0);

        for (int x = resultingArea.topLeft.x; x < resultingArea.topLeft.x + areaSize; x++) {
            for (int y = resultingArea.topLeft.y; y < resultingArea.topLeft.y + areaSize; y++) {
                if (gameMap.getSite(new Location(x, y)).owner != 1)
                    return Piece.fromLocation(new Location(x, y), gameMap, 1);
            }
        }
        return null;
    }

    private static List<ProductionArea> filterByProductionType(List<ProductionArea> sortedByDistance, ProductionAreaType type) {
        return sortedByDistance.stream().filter(productionArea -> productionArea.type.equals(type)).collect(Collectors.toList());
    }


    Location getTopLeft() {
        return topLeft;
    }

    private Location getBottomRight() {
        return bottomRight;
    }

    public int getMeanProduction() {
        return meanProduction;
    }
}
