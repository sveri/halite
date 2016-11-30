/**
 * Created by sveri on 30.11.16.
 */
public class DirectionAndDistanceValue implements Comparable{

    private final Direction direction;

    private final int value;
    private final int distance;


    public DirectionAndDistanceValue(Direction direction, int value, int distance) {
        this.direction = direction;
        this.value = value;
        this.distance = distance;
    }

    @Override
    public int compareTo(Object o) {
        DirectionAndDistanceValue dadv = (DirectionAndDistanceValue) o;
        if(value == dadv.getValue()) return 0;
        if(value < dadv.getValue()) return -1;
        return 1;
    }

    @Override
    public String toString() {
        return "DirectionAndDistanceValue{" +
                "direction=" + direction +
                ", value=" + value +
                ", distance=" + distance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DirectionAndDistanceValue that = (DirectionAndDistanceValue) o;

        if (value != that.value) return false;
        if (distance != that.distance) return false;
        return direction == that.direction;
    }

    @Override
    public int hashCode() {
        int result = direction.hashCode();
        result = 31 * result + value;
        result = 31 * result + distance;
        return result;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getValue() {
        return value;
    }
}
