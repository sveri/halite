import java.util.Comparator;

class PieceStrengthComparator implements Comparator<Piece> {

    @Override
    public int compare(Piece o1, Piece o2) {
        if(o1.getSite().strength == o2.getSite().strength) return 0;
        if(o1.getSite().strength > o2.getSite().strength) return 1;
        return -1;
    }
}
