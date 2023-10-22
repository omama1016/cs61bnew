package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    //--------------------------------------------------------------------
                            // private instance variables
    private static long SEED;
    private static Random RANDOM;
    private static List<Room> rooms;
    private static int numOfRooms;
    private final static int minWidthOfRoom = 3;
    private final static int maxWidthOfRoom = 20;
    private final static int minHeightOfRoom = 3;
    private final static int maxHeightOfRoom = 10;
    //--------------------------------------------------------------------
    private static class Position {
        private int x;
        private int y;

        Position(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    private static class Room {
        private Position start;
        private int width;
        private int height;

        private static Room.RoomXPositionComparator SORT_COMPARATOR = new Room.RoomXPositionComparator();

        private Room(Position start, int w, int h){
            this.start = start;
            width = w;
            height = h;
        }

        private static class RoomXPositionComparator implements Comparator<Room> {
            @Override
            public int compare(Room o1, Room o2) {
                return (o1.start.x - o2.start.x);
            }
        }
    }
    //--------------------------------------------------------------------
    private static int getRandomSeedFromInput(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isDigit(c)) {
                sb.append(c);
            } else {
                break;
            }
        }
        return Integer.parseInt(sb.toString());
    }

    private void init(TETile[][] world, String input) {
        SEED = getRandomSeedFromInput(input);
        RANDOM = new Random(SEED);
        numOfRooms = RANDOM.nextInt(15) + 5;
        rooms = new ArrayList<>();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    private static boolean isRoomOutOfBound(Room room) {
        if (room.start.x + room.width >= WIDTH || room.start.y + room.height >= HEIGHT) {
            return true;
        }
        return false;
    }

    private static boolean isOverLap(Room r1, Room r2) {
        int r1x1 = r1.start.x;
        int r1y1 = r1.start.y;
        int r1x2 = r1x1 + r1.width;
        int r1y2 = r1y1 + r1.height;
        int r2x1 = r2.start.x;
        int r2y1 = r2.start.y;
        int r2x2 = r2x1 + r2.width;
        int r2y2 = r2y1 + r2.height;
        return Math.max(r1x1, r2x1) < Math.min(r1x2, r2x2) && Math.max(r1y1, r2y1) < Math.min(r1y2, r2y2);
    }

    private static boolean isOverLapFromOthers(List<Room> others, Room room) {
        if (!others.isEmpty()) {
            for (Room r : others){
                if (isOverLap(r, room)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void drawRoom(TETile[][] world, Room room) {
        int x = room.start.x;
        int y = room.start.y;
        for (int i = x; i < x + room.width; i++) {
            if (i == x || i == (x + room.width - 1)) {
                for (int j = y; j < y + room.height; j++) {
                    world[i][j] = Tileset.WALL;
                }
            } else {
                world[i][y] = Tileset.WALL;
                world[i][y+room.height-1] = Tileset.WALL;
                for (int j = y+1; j < y + room.height - 1; j++) {
                    world[i][j] = Tileset.FLOOR;
                }
            }

        }
    }

    private static List<Room> generateRooms(TETile[][] world) {
        for (int i = 0; i < numOfRooms; i++) {
            Room room = null;
            do {
                int startX = RANDOM.nextInt(WIDTH);
                int startY = RANDOM.nextInt(HEIGHT);
                int rw = RANDOM.nextInt(maxWidthOfRoom - minWidthOfRoom) + minWidthOfRoom;
                int rh = RANDOM.nextInt(maxHeightOfRoom - minHeightOfRoom) + minHeightOfRoom;
                room = new Room(new Position(startX, startY), rw, rh);
            } while (isRoomOutOfBound(room) || isOverLapFromOthers(rooms, room));
            drawRoom(world, room);
            rooms.add(room);
        }
        return rooms;
    }

    private static void drawHorizontalHall(TETile[][] world, Position s, Position e) {
        // assume s.x < e.x
        if (s.x > e.x) {
            for (int i = 0; i < s.x - e.x + 1; i++) {
                if (world[s.x - i][s.y-1].character() != Tileset.FLOOR.character()) {
                    world[s.x - i][s.y-1] = Tileset.WALL;
                }
                world[s.x - i][s.y] = Tileset.FLOOR;
                if (world[s.x - i][s.y+1].character() != Tileset.FLOOR.character()){
                    world[s.x - i][s.y+1] = Tileset.WALL;
                }
            }
        } else if (s.x < e.x){
            for (int i = 0; i < e.x - s.x + 1; i++) {
                if (world[s.x + i][s.y-1].character() != Tileset.FLOOR.character()) {
                    world[s.x + i][s.y-1] = Tileset.WALL;
                }
                world[s.x + i][s.y] = Tileset.FLOOR;
                if (world[s.x + i][s.y+1].character() != Tileset.FLOOR.character()){
                    world[s.x + i][s.y+1] = Tileset.WALL;
                }
            }
        }
    }

    private static void drawVerticalHall(TETile[][] world, Position s, Position e) {
        // assume s.y < e.y, if not change
        if (s.y > e.y) {
            for (int i = 0; i < s.y - e.y + 1; i++) {
                if (world[s.x-1][s.y - i].character() != Tileset.FLOOR.character())
                    world[s.x-1][s.y - i] = Tileset.WALL;
                world[s.x][s.y - i] = Tileset.FLOOR;
                if (world[s.x+1][s.y - i].character() != Tileset.FLOOR.character())
                    world[s.x+1][s.y - i] = Tileset.WALL;
            }
        }
        else if (s.y < e.y){
            for (int i = 0; i < e.y - s.y + 1; i++) {
                if (world[s.x-1][s.y + i].character() != Tileset.FLOOR.character())
                    world[s.x-1][s.y + i] = Tileset.WALL;
                world[s.x][s.y + i] = Tileset.FLOOR;
                if (world[s.x+1][s.y + i].character() != Tileset.FLOOR.character())
                    world[s.x+1][s.y + i] = Tileset.WALL;
            }
        }
    }

    private static void drawCorner(TETile[][] world, Position s, Position e) {
        if (s.x < e.x && s.y < e.y) {
            if (world[e.x + 1][s.y - 1].character() != Tileset.FLOOR.character() ) {
                world[e.x + 1][s.y - 1] = Tileset.WALL;
            }
        } else if(s.x < e.x && s.y > e.y){
            if (world[e.x + 1][s.y + 1].character() != Tileset.FLOOR.character() ) {
                world[e.x + 1][s.y + 1] = Tileset.WALL;
            }
        } else if(s.x > e.x && s.y > e.y){
            if (world[e.x - 1][s.y + 1].character() != Tileset.FLOOR.character() ) {
                world[e.x - 1][s.y + 1] = Tileset.WALL;
            }
        } else if(s.x > e.x && s.y < e.y){
            if (world[e.x - 1][s.y - 1].character() != Tileset.FLOOR.character() ) {
                world[e.x - 1][s.y - 1] = Tileset.WALL;
            }
        }
    }


    private static void drawHall(TETile[][] world, Position s, Position e) {
        drawHorizontalHall(world, s, e);
        Position nPos = new Position(e.x, s.y);
        drawVerticalHall(world, nPos, e);
        drawCorner(world, s, e);
    }

    private static void connectRooms(TETile[][] world, List<Room> rooms) {
        rooms.sort(Room.SORT_COMPARATOR);
        for (int i = 0; i < rooms.size() - 1; i++) {
            Room r1 = rooms.get(i);
            Room r2 = rooms.get(i+1);
            int sx = r1.start.x + RANDOM.nextInt(r1.width - 2) + 1;
            int sy = r1.start.y + RANDOM.nextInt(r1.height - 2) + 1;
            int ex = r2.start.x + RANDOM.nextInt(r2.width - 2) + 1;
            int ey = r2.start.y + RANDOM.nextInt(r2.height - 2) + 1;
            drawHall(world, new Position(sx, sy), new Position(ex, ey));
        }
    }

    private static boolean isPositionDoor(TETile[][] world, int x, int y) {
        boolean c1 = world[x][y].character() == Tileset.WALL.character();
        boolean c2 = world[x-1][y].character() == Tileset.NOTHING.character();
        boolean c3 = world[x+1][y].character() == Tileset.NOTHING.character();
        boolean c4 = world[x-1][y].character() == Tileset.FLOOR.character();
        boolean c5 = world[x+1][y].character() == Tileset.FLOOR.character();
        boolean c6 = world[x][y-1].character() == Tileset.NOTHING.character();
        boolean c7 = world[x][y-1].character() == Tileset.FLOOR.character();
        boolean c8 = world[x][y+1].character() == Tileset.NOTHING.character();
        boolean c9 = world[x][y+1].character() == Tileset.FLOOR.character();
        if ( c1 ) {
            if ((c2 && c5) || (c3 && c4) || (c6 && c9) || (c7 && c8)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static void addOutDoor(TETile[][] world) {
        int x = 0;
        int y = 0;
        do {
            x = RANDOM.nextInt(WIDTH - 2) + 1;
            y = RANDOM.nextInt(HEIGHT - 2) + 1;
        } while (!isPositionDoor(world, x, y));

        world[x][y] = Tileset.LOCKED_DOOR;
    }

    private static void addPlayer(TETile[][] world) {
        int x = 0;
        int y = 0;
        do {
            x = RANDOM.nextInt(WIDTH - 2) + 1;
            y = RANDOM.nextInt(HEIGHT - 2) + 1;
        } while (world[x][y].character() != Tileset.FLOOR.character());

        world[x][y] = Tileset.PLAYER;
    }
    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

//        TERenderer ter = new TERenderer();
//        ter.initialize(WIDTH, HEIGHT);

        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        init(finalWorldFrame, input);

        List<Room> rooms = generateRooms(finalWorldFrame);
        connectRooms(finalWorldFrame, rooms);
        addOutDoor(finalWorldFrame);
        addPlayer(finalWorldFrame);

//        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }
}
