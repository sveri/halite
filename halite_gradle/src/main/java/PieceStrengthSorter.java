import java.util.Comparator;

/**
 * Created by sveri on 25.11.16.
 */
public class PieceStrengthSorter implements Comparator<Piece> {

    @Override
    public int compare(Piece o1, Piece o2) {
        if(o1.getStrength() == o2.getStrength()) return 0;
        if(o1.getStrength() < o2.getStrength()) return -1;

        return 1;
    }
}
