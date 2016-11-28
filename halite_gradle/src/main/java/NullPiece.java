
public class NullPiece extends Piece{

    private NullPiece(int id) {
        super(id);
    }

    static Piece newNullPiece(){
        return new NullPiece(-1);
    }

    public boolean isNull() {
        return true;
    }
}
