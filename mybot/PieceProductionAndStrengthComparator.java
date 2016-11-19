import java.util.Comparator;

class PieceProductionAndStrengthComparator implements Comparator<Piece> {

    /**
     * returns the piece with highest production and lowest strength
     * -1 if o1 < o2
     * 0 if o1 == o2
     * 1 if o1 > o2
     */
    @Override
    public int compare(Piece o1, Piece o2) {
        if(o1.getProduction() == o2.getProduction() && o1.getStrength() == o2.getStrength()) return 0;
        
        if(o1.getProduction() == o2.getProduction() && o1.getStrength() <= o2.getStrength()) return -1;
        if(o1.getProduction() == o2.getProduction() && o1.getStrength() > o2.getStrength()) return 1;
        
        if(o1.getProduction() <= o2.getProduction() && o1.getStrength() == o2.getStrength()) return 1;
        if(o1.getProduction() > o2.getProduction() && o1.getStrength() == o2.getStrength()) return -1;

        if(o1.getProduction() <= o2.getProduction() && o1.getStrength() <= o2.getStrength()) return 1;
        if(o1.getProduction() <= o2.getProduction() && o1.getStrength() > o2.getStrength()) return 1;

        if(o1.getProduction() > o2.getProduction() && o1.getStrength() <= o2.getStrength()) return -1;
        if(o1.getProduction() <= o2.getProduction() && o1.getStrength() <= o2.getStrength()) return 1;
//        if(o1.getProduction() > o2.getProduction() && o1.getStrength() == o2.getStrength()) return -1;
//        if(o1.getProduction() > o2.getProduction()) return 1;
        return -1;
    }
}
