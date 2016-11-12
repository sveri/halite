public class GeneralGameInformation {
    private int ownTilesCount;

    private int enemyTilesCount;

    private int npcTilesCount;

    public int getOwnTilesCount() {
        return ownTilesCount;
    }

    public void addOneToOwnTilesCount() {
        this.ownTilesCount++;
    }

    public int getEnemyTilesCount() {
        return enemyTilesCount;
    }

    public void addOneToEnemyTilesCount() {
        this.enemyTilesCount++;
    }

    public int getNpcTilesCount() {
        return npcTilesCount;
    }

    public void addOneToNpcTilesCount() {
        this.npcTilesCount++;
    }
}
