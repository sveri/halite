import java.util.Comparator;

/**
 * Created by sveri on 29.11.16.
 */
public class MoveValueComparator implements Comparator<MoveFromTo> {

    @Override
    public int compare(MoveFromTo o1, MoveFromTo o2) {
        if(o1.getMoveValue() == o2.getMoveValue()) return 0;
        if(o1.getMoveValue() < o2.getMoveValue()) return -1;

        return 1;
    }
}
