
public class Adder {

    private int width, height;

    Adder(int width, int height) {
        this.width = width;
        this.height = height;
    }

    int addX(int x, int plus) {
        return addWidthOrHeight(x, plus, width);
    }

    int addY(int y, int plus) {
        return addWidthOrHeight(y, plus, height);
    }

    private int addWidthOrHeight(int from, int plus, int widthOrHeight) {
        if((from + plus) < widthOrHeight) return from + plus;
        return ((from + plus) % widthOrHeight);
    }

    int subX(int x, int minus) {
        return subWidthOrHeight(x, minus, width);
    }

    int subY(int y, int minus) {
        return subWidthOrHeight(y, minus, height);
    }

    private int subWidthOrHeight(int from, int minus, int widthOrHeight) {
        if((from - minus) >= 0) return from - minus;
        return (widthOrHeight + (from - minus) - 1);
    }
}
