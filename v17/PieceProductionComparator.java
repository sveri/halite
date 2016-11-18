import java.util.Comparator;

class PieceProductionComparator implements Comparator<Piece> {

    @Override
    public int compare(Piece o1, Piece o2) {
        if(o1.getSite().production == o2.getSite().production) return 0;
        if(o1.getSite().production > o2.getSite().production) return 1;
        return -1;
    }
}
