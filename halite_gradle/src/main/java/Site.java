public class Site {
    public int owner, strength, production;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Site site = (Site) o;

        if (owner != site.owner) return false;
        if (strength != site.strength) return false;
        return production == site.production;

    }

    @Override
    public int hashCode() {
        int result = owner;
        result = 31 * result + strength;
        result = 31 * result + production;
        return result;
    }
}
