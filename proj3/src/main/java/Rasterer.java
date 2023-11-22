import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    private double mapUllon;
    private double mapUllat;
    private double mapLrlon;
    private double mapLrlat;
    private double mapWidth;
    private double mapHeight;
    private double tideSize;

    private final static double S = 288200;

    public Rasterer() {
        // YOUR CODE HERE
        mapUllon = MapServer.ROOT_ULLON;
        mapUllat = MapServer.ROOT_ULLAT;
        mapLrlon = MapServer.ROOT_LRLON;
        mapLrlat = MapServer.ROOT_LRLAT;
        mapWidth = mapLrlon - mapUllon;
        mapHeight = mapUllat - mapLrlat;
        tideSize = MapServer.TILE_SIZE;
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        System.out.println(params);
        Map<String, Object> results = new HashMap<>();

        // read the request params
        double queryLrlon = params.get("lrlon");
        double queryUllon = params.get("ullon");
        double queryUllat = params.get("ullat");
        double queryLrlat = params.get("lrlat");
        double displayWidth = params.get("w");
        double displayHeight = params.get("h");

        // corner cases
        if (queryUllon > queryLrlon || queryUllat < queryLrlat
                || !(Math.max(queryUllon, mapUllon) < Math.min(queryLrlon, mapLrlon)
                    && Math.max(queryLrlat, mapLrlat) < Math.min(queryUllat, mapUllat))) {
            results.put("query_success", false);
            return results;
        }

        // find the depth
        int depth = findDepthByQuery(queryLrlon, queryUllon, displayWidth);
        // find Upper left tile of the bounding box
        double interval_lon = (mapLrlon - mapUllon) / Math.pow(2, depth);
        double interval_lat = (mapUllat - mapLrlat) / Math.pow(2, depth);
        int ul_x = findULImageX(interval_lon, queryUllon);
        int ul_y = findULImageY(interval_lat, queryUllat);
        int lr_x = findLRImageX(interval_lon, queryLrlon, depth);
        int lr_y = findLRImageY(interval_lat, queryLrlat, depth);
        // prepare the results
        String[][] grids = new String[lr_y - ul_y + 1][lr_x - ul_x + 1];
        for (int i = 0; i < grids.length; i++) {
            for (int j = 0; j < grids[i].length; j++) {
                grids[i][j] = "d" + depth + "_x" + (ul_x+j) + "_y" + (ul_y+i) + ".png";
            }
        }
        double raster_ul_lon = mapUllon + interval_lon * ul_x;
        double raster_ul_lat = mapUllat - interval_lat * ul_y;
        double raster_lr_lon = mapUllon + interval_lon * (lr_x+1);
        double raster_lr_lat = mapUllat - interval_lat * (lr_y+1);
        results.put("render_grid", grids);
        results.put("raster_ul_lon", raster_ul_lon);
        results.put("raster_ul_lat", raster_ul_lat);
        results.put("raster_lr_lon", raster_lr_lon);
        results.put("raster_lr_lat", raster_lr_lat);
        results.put("depth", depth);
        results.put("query_success", true);
        return results;
    }

    private void printGrids(String[][] grids) {
        for (int i = 0; i < grids.length; i++) {
            for (int j = 0; j < grids[i].length; j++) {
                System.out.print(grids[i][j] + " ");
            }
            System.out.println();
        }
    }

    private int findULImageX(double interval_lon, double queryUllon) {
        double curLon = mapUllon;
        int ix = 0;
        while (curLon < queryUllon && (curLon + interval_lon) < queryUllon) {
            curLon += interval_lon;
            ix += 1;
        }
        return ix;
    }

    private int findULImageY(double interval_lat, double queryUllat) {
        double curLat = mapUllat;
        int iy = 0;
        while (curLat > queryUllat && (curLat - interval_lat) > queryUllat) {
            curLat -= interval_lat;
            iy += 1;
        }
        return iy;
    }

    private int findLRImageX(double interval_lon, double queryLrlon, int depth) {
        double curLon = mapLrlon;
        int ix = (int)Math.pow(2, depth) - 1;
        while (curLon > queryLrlon && (curLon - interval_lon) > queryLrlon) {
            curLon -= interval_lon;
            ix -= 1;
        }
        return ix;
    }

    private int findLRImageY(double interval_lat, double queryLrlat, int depth) {
        double curLat = mapLrlat;
        int iy = (int)Math.pow(2, depth) - 1;
        while (curLat < queryLrlat && (curLat + interval_lat) < queryLrlat) {
            curLat += interval_lat;
            iy -= 1;
        }
        return iy;
    }

    private int findDepthByQuery(double queryLrlon, double queryUllon, double displayWidth) {
        double query_longDPP = (queryLrlon - queryUllon) / displayWidth;
        int d = 0;
        double map_longDPP = (mapLrlon - mapUllon) / tideSize; // d0 resolution
        while (map_longDPP > query_longDPP) {
            map_longDPP /= 2;
            d += 1;
        }
        return Math.min(d, 7);
    }

    private void testBasic(Map<String, Object> results) {
        String[][] grid = {{"d7_x84_y28.png", "d7_x85_y28.png", "d7_x86_y28.png"},
                {"d7_x84_y29.png", "d7_x85_y29.png", "d7_x86_y29.png"},
                {"d7_x84_y30.png", "d7_x85_y30.png", "d7_x86_y30.png"},
        };
        results.put("render_grid", grid);
        results.put("raster_ul_lon", 122.24212646484375);
        results.put("raster_ul_lat", 37.87701580361881);
        results.put("raster_lr_lon", 122.24006652832031);
        results.put("raster_lr_lat", 37.87538940251607);
        results.put("depth", 7);
        results.put("query_success", true);
    }

}
