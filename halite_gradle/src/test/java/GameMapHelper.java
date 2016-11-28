    import java.util.ArrayList;
    import java.util.SimpleTimeZone;
    
    /**
     * Created by sveri on 28.11.16.
     */
    public class GameMapHelper {
    
        static GameMap newGameMap() {
            GameMap gameMap = new GameMap();
            gameMap.height = 10;
            gameMap.width = 10;

            ArrayList<ArrayList<Site>> sites = new ArrayList<>();
            ArrayList<Site> sites0 = new ArrayList<>();
            sites0.add(getSite(0, 100, 1));
            sites0.add(getSite(0, 120, 1));
            sites0.add(getSite(0, 130, 2));
            sites0.add(getSite(0, 90, 2));
            sites0.add(getSite(0, 80, 3));
            sites0.add(getSite(0, 70, 5));
            sites0.add(getSite(0, 60, 4));
            sites0.add(getSite(0, 200, 3));
            sites0.add(getSite(0, 100, 3));
            sites0.add(getSite(0, 20, 2));
    
            ArrayList<Site> sites1 = new ArrayList<>();
            sites1.add(getSite(0, 100, 1));
            sites1.add(getSite(0, 120, 1));
            sites1.add(getSite(0, 130, 2));
            sites1.add(getSite(0, 90, 2));
            sites1.add(getSite(0, 80, 10));
            sites1.add(getSite(0, 70, 14));
            sites1.add(getSite(0, 60, 4));
            sites1.add(getSite(0, 200, 3));
            sites1.add(getSite(0, 100, 3));
            sites1.add(getSite(0, 20, 2));
    
            ArrayList<Site> sites2 = new ArrayList<>();
            sites2.add(getSite(0, 100, 1));
            sites2.add(getSite(0, 120, 1));
            sites2.add(getSite(0, 130, 2));
            sites2.add(getSite(0, 90, 2));
            sites2.add(getSite(0, 80, 15));
            sites2.add(getSite(0, 70, 16));
            sites2.add(getSite(0, 60, 4));
            sites2.add(getSite(0, 200, 3));
            sites2.add(getSite(0, 100, 3));
            sites2.add(getSite(0, 20, 2));
    
            ArrayList<Site> sites3 = new ArrayList<>();
            sites3.add(getSite(0, 100, 1));
            sites3.add(getSite(0, 120, 1));
            sites3.add(getSite(0, 130, 2));
            sites3.add(getSite(0, 90, 2));
            sites3.add(getSite(0, 80, 3));
            sites3.add(getSite(0, 70, 5));
            sites3.add(getSite(0, 60, 4));
            sites3.add(getSite(0, 200, 3));
            sites3.add(getSite(0, 100, 3));
            sites3.add(getSite(0, 20, 2));
    
            ArrayList<Site> sites4 = new ArrayList<>();
            sites4.add(getSite(0, 100, 1));
            sites4.add(getSite(0, 120, 1));
            sites4.add(getSite(0, 130, 2));
            sites4.add(getSite(0, 90, 2));
            sites4.add(getSite(0, 80, 3));
            sites4.add(getSite(0, 70, 5));
            sites4.add(getSite(0, 60, 4));
            sites4.add(getSite(0, 200, 3));
            sites4.add(getSite(0, 100, 3));
            sites4.add(getSite(0, 20, 2));
    
            ArrayList<Site> sites5 = new ArrayList<>();
            sites5.add(getSite(0, 100, 1));
            sites5.add(getSite(0, 120, 1));
            sites5.add(getSite(0, 130, 2));
            sites5.add(getSite(0, 90, 2));
            sites5.add(getSite(0, 80, 3));
            sites5.add(getSite(0, 70, 5));
            sites5.add(getSite(0, 60, 4));
            sites5.add(getSite(0, 200, 3));
            sites5.add(getSite(0, 100, 3));
            sites5.add(getSite(0, 20, 2));
    
            ArrayList<Site> sites6 = new ArrayList<>();
            sites6.add(getSite(0, 100, 1));
            sites6.add(getSite(0, 120, 1));
            sites6.add(getSite(0, 130, 2));
            sites6.add(getSite(0, 90, 2));
            sites6.add(getSite(0, 80, 3));
            sites6.add(getSite(0, 70, 5));
            sites6.add(getSite(0, 60, 4));
            sites6.add(getSite(0, 200, 3));
            sites6.add(getSite(0, 100, 3));
            sites6.add(getSite(0, 20, 2));
    
            ArrayList<Site> sites7 = new ArrayList<>();
            sites7.add(getSite(0, 100, 1));
            sites7.add(getSite(0, 120, 1));
            sites7.add(getSite(0, 130, 2));
            sites7.add(getSite(0, 90, 2));
            sites7.add(getSite(0, 80, 3));
            sites7.add(getSite(0, 70, 5));
            sites7.add(getSite(0, 60, 4));
            sites7.add(getSite(0, 200, 3));
            sites7.add(getSite(0, 100, 3));
            sites7.add(getSite(0, 20, 2));
    
            ArrayList<Site> sites8 = new ArrayList<>();
            sites8.add(getSite(0, 100, 11));
            sites8.add(getSite(0, 120, 15));
            sites8.add(getSite(0, 130, 2));
            sites8.add(getSite(0, 90, 2));
            sites8.add(getSite(0, 80, 3));
            sites8.add(getSite(0, 70, 5));
            sites8.add(getSite(0, 60, 4));
            sites8.add(getSite(0, 200, 3));
            sites8.add(getSite(1, 100, 3));
            sites8.add(getSite(1, 20, 2));
    
            ArrayList<Site> sites9 = new ArrayList<>();
            sites9.add(getSite(0, 100, 12));
            sites9.add(getSite(0, 120, 13));
            sites9.add(getSite(0, 130, 2));
            sites9.add(getSite(0, 90, 2));
            sites9.add(getSite(0, 80, 3));
            sites9.add(getSite(0, 70, 5));
            sites9.add(getSite(0, 60, 4));
            sites9.add(getSite(0, 200, 3));
            sites9.add(getSite(1, 100, 3));
            sites9.add(getSite(0, 20, 2));
            
            sites.add(sites0);
            sites.add(sites1);
            sites.add(sites2);
            sites.add(sites3);
            sites.add(sites4);
            sites.add(sites5);
            sites.add(sites6);
            sites.add(sites7);
            sites.add(sites8);
            sites.add(sites9);
            gameMap.contents = sites;
            return gameMap;
        }
    
        private static Site getSite(int owner, int strength, int production) {
            Site site = new Site();
            site.owner = owner;
            site.strength = strength;
            site.production = production;
            
            return site;
        }
    }
