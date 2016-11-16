import java.util.ArrayList;
public class GameMap{
    public ArrayList< ArrayList<Site> > contents;
    public int width, height;

    public GameMap() {
        width = 0;
        height = 0;
        contents = new ArrayList< ArrayList<Site> >(0);
    }

    public GameMap(int width_, int height_) {
        width = width_;
        height = height_;
        contents = new ArrayList< ArrayList<Site> >(0);
        for(int y = 0; y < height; y++) {
            ArrayList<Site> row = new ArrayList<Site>();
            for(int x = 0; x < width; x++) {
                row.add(new Site());
            }
            contents.add(row);
        }
    }

    public boolean inBounds(Location loc) {
        return loc.x < width && loc.x >= 0 && loc.y < height && loc.y >= 0;
    }

    public double getDistance(Location loc1, Location loc2) {
        int dx = Math.abs(loc1.x - loc2.x);
        int dy = Math.abs(loc1.y - loc2.y);

        if(dx > width / 2.0) dx = width - dx;
        if(dy > height / 2.0) dy = height - dy;

        return dx + dy;
    }

    public double getAngle(Location loc1, Location loc2) {
        int dx = loc1.x - loc2.x;

        // Flip order because 0,0 is top left
        // and want atan2 to look as it would on the unit circle
        int dy = loc2.y - loc1.y;

        if(dx > width - dx) dx -= width;
        if(-dx > width + dx) dx += width;

        if(dy > height - dy) dy -= height;
        if(-dy > height + dy) dy += height;

        return Math.atan2(dy, dx);
    }

    public Location getLocation(Location loc, Direction dir) {
        Location l = new Location(loc);
        if(dir != Direction.STILL) {
            if(dir == Direction.NORTH) {
                if(l.y == 0) l.y = height - 1;
                else l.y--;
            }
            else if(dir == Direction.EAST) {
                if(l.x == width - 1) l.x = 0;
                else l.x++;
            }
            else if(dir == Direction.SOUTH) {
                if(l.y == height - 1) l.y = 0;
                else l.y++;
            }
            else if(dir == Direction.WEST) {
                if(l.x == 0) l.x = width - 1;
                else l.x--;
            }
        }
        return l;
    }

    Direction getDirectionFromTo(Location from, Location to, int heigth, int width) {
        // need to move on y line
        if(from.x == to.x) {
            if(from.y < to.y && (to.y - from.y) < heigth / 2) {
                return Direction.SOUTH;
            } else if(from.y < to.y && (to.y - from.y) >= heigth / 2) {
                return Direction.NORTH;
            } else if(to.y < from.y && (from.y - to.y) < heigth / 2) {
                return Direction.NORTH;
            } else {
                return Direction.SOUTH;
            }
        }
        // need to move on x line
        else {
            if (from.x < to.x && (to.x - from.x) < width / 2) {
                return Direction.EAST;
            } else if (from.x < to.x && (to.x - from.x) >= width / 2) {
                return Direction.WEST;
            } else if (to.x < from.x && (from.x - to.x) < width / 2) {
                return Direction.WEST;
            }
            return Direction.EAST;
        }
    }

    Direction getFromPieceToPiece(Piece from, Piece to) {
        return getDirectionFromTo(from.getLoc(), to.getLoc(), from.getGameMap().height, from.getGameMap().width);
    }

    public Site getSite(Location loc, Direction dir) {
        Location l = getLocation(loc, dir);
        return contents.get(l.y).get(l.x);
    }

    public int getOwner(Location loc, Direction dir) {
        Location l = getLocation(loc, dir);
        return contents.get(l.y).get(l.x).owner;
    }

    Site getSite(Location loc) {
        return contents.get(loc.y).get(loc.x);
    }

    public int getTotalPiecesCount(){
        return height * width;
    }

}
