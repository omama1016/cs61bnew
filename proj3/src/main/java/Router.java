import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    private static class SearchNode implements Comparable<SearchNode>{
        long id;
        double lon;
        double lat;
        double distance;
        double priority;

        public SearchNode(long id, double lon, double lat, double distanceToS, double distanceToT) {
            this.id = id;
            this.lon = lon;
            this.lat = lat;
            this.distance = distanceToS;
            this.priority = distanceToT;
        }

        @Override
        public int compareTo(SearchNode o) {
            if (this.priority - o.priority < 0) {
                return -1;
            } else if (this.priority - o.priority > 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {

        // used for the marked set
        Set<Long> marked = new HashSet<>();
        // used for the best
        Map<Long, Double> best = new LinkedHashMap<>();
        Map<Long, Long> parent = new LinkedHashMap<>();
        // used to store the path
        List<Long> path = new LinkedList<>();
        Stack<Long> stack = new Stack<>();

        long startID = g.closest(stlon, stlat);
        long endID = g.closest(destlon, destlat);
        SearchNode startNode = new SearchNode(startID, g.lon(startID), g.lat(startID),
                0, g.distance(startID, endID));
        // initialize the best
        best.put(startID, 0.0);
        parent.put(startID, startID);
        PriorityQueue<SearchNode> pq = new PriorityQueue<>();
        pq.offer(startNode);
        // search
        while (!pq.isEmpty()) {
            SearchNode v = pq.poll();
            // ******* if we find the goal
            if (v.id == endID) {
                stack.push(v.id);
                long cur = v.id;
                while (cur != startID) {
                    long parentID = parent.get(cur);
                    stack.push(parentID);
                    cur = parentID;
                }
            }
            // ******* else relaxing
            if (!marked.contains(v.id)) {
                marked.add(v.id);
                for (Long wID : g.adjacent(v.id)) {
                    if (marked.contains(wID)) continue;
                    double disVtoW = g.distance(v.id, wID);
                    double disStoV = best.get(v.id);
                    double disStoW = best.getOrDefault(wID, Double.MAX_VALUE);
                    if (disStoV + disVtoW < disStoW) {
                        best.put(wID, disStoV + disVtoW);
                        parent.put(wID, v.id);
                        // add w with the priority = dsv + dvw + h(w)
                        double priority = disStoV + disVtoW + g.distance(wID, endID);
                        pq.offer(new SearchNode(wID, g.lon(wID), g.lat(wID), disStoV + disVtoW, priority));
                    }
                }
            }
        }

        for (Long v : stack) path.add(0, v);

        return path; // FIXME
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {

        if (route.size() == 0) return null;
        List<NavigationDirection> res = new ArrayList<>();
        String curWayName = g.getWay(route.get(0));
        double curDis = 0;

        if (route.size() == 1) {
            addStartNavigation(res, curWayName, curDis);
            return res;
        }

        int curIndex = 1;
        // consider the starting case
        while (curIndex < route.size()) {
            curDis += g.distance(route.get(curIndex), route.get(curIndex-1));
            if (!g.getWay(route.get(curIndex)).equals(curWayName) || g.getWayId(route.get(curIndex))
                    != g.getWayId(route.get(curIndex - 1))) {
                addStartNavigation(res, curWayName, curDis);
                curDis = 0;
                curWayName = g.getWay(route.get(curIndex));
                break;
            }

            if (curIndex == route.size() - 1) {
                addStartNavigation(res, curWayName, curDis);
                return res;
            }

            curIndex += 1;
        }

        // consider not the starting case
        if (curIndex != route.size() - 1) {
            curIndex += 1;
            while(curIndex < route.size()) {
                curDis += g.distance(route.get(curIndex), route.get(curIndex - 1));
                if (!g.getWay(route.get(curIndex)).equals(curWayName) || g.getWayId(route.get(curIndex))
                        != g.getWayId(route.get(curIndex - 1))) {
                    addIntervalNavigation(g, res, curWayName, curDis, route.get(curIndex), route.get(curIndex-1));
                    curDis = 0;
                    curWayName = g.getWay(route.get(curIndex));
                }

                if (curIndex == route.size() - 1) {
                    addIntervalNavigation(g, res, curWayName, curDis, route.get(curIndex), route.get(curIndex-1));
                }

                curIndex += 1;
            }

        }

        return res; // FIXME
    }

    private static void addStartNavigation(List<NavigationDirection> res, String curWayName, double sDis) {
        NavigationDirection s = new NavigationDirection();
        s.direction = NavigationDirection.START;
        s.way = curWayName;
        s.distance = sDis;
        res.add(s);
    }

    private static void addIntervalNavigation(GraphDB g, List<NavigationDirection> res, String curWayName,
                                              double sDis, long v, long w) {
        NavigationDirection s = new NavigationDirection();
        s.direction = computeDirection(g, v, w);
        s.way = curWayName;
        s.distance = sDis;
        res.add(s);
    }

    private static int computeDirection(GraphDB g, long v, long w) {
        double bear = g.bearing(v, w);
        if (bear >= -15 && bear <= 15) return NavigationDirection.STRAIGHT;
        else if (bear >= -30 && bear < -15) return NavigationDirection.SLIGHT_LEFT;
        else if (bear <= 30 && bear > 15) return NavigationDirection.SLIGHT_RIGHT;
        else if (bear >= -100 && bear < -30) return NavigationDirection.LEFT;
        else if (bear <= 100 && bear > 30) return NavigationDirection.RIGHT;
        else if (bear < -100) return NavigationDirection.SHARP_LEFT;
        else return NavigationDirection.SHARP_RIGHT;
    }

    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
