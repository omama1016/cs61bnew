package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState{

    private int[][] titles;
    private int[][] goal;
    private int N;

    private void initGoal() {
        goal = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                goal[i][j] = N * i + j + 1;
            }
        }
        goal[N-1][N-1] = 0;
    }

    private void copyBoard (int[][] tiles) {
        this.titles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.titles[i][j] = tiles[i][j];
            }
        }
    }

    public Board(int[][] tiles){
        this.N = tiles.length;
        copyBoard(tiles);
        initGoal();
    }
    private boolean outOfIndex(int index) {
        if (index < 0 || index >= N) {
            return true;
        }
        return false;
    }
    public int tileAt(int i, int j){
        if (outOfIndex(i) || outOfIndex(j)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return titles[i][j];
    }

    public int size(){
        return N;
    }
    public int hamming(){
        int ed = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (titles[i][j] != 0 && titles[i][j] != goal[i][j]) {
                    ed += 1;
                }
            }
        }
        return ed;
    }
//    private int computeManhattan(int val, int i, int j) {
//        int targetR = (val - 1) / N;
//        int targetC = (val - 1) % N;
//        return Math.abs(targetR - i) + Math.abs(targetC - j);
//    }

    private int computeManhattan(int val, int i, int j) {
        int targetR = val % N == 0 ? (val / N - 1) : val / N;
        int targetC = (val - 1) % N;
        return Math.abs(targetR - i) + Math.abs(targetC - j);
    }
    public int manhattan(){
        int ed = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (titles[i][j] != 0 && titles[i][j] != goal[i][j]) {
                    ed += computeManhattan(titles[i][j], i, j);
                }
            }
        }
        return ed;
    }
//    private int toX(int v){
//        return (v - 1) / N;
//    }
//    private int toY(int v) {
//        return (v - 1) % N ;
//    }
//
//    public int manhattan() {
//
//        int dis = 0;
//        for (int i = 0; i < N; i++) {
//            for (int j = 0; j < N; j++) {
//                int x = 0;
//                int y = 0;
//                if (titles[i][j] == 0) {
//                    continue;
//                } else {
//                    x = toX(titles[i][j]);
//                    y = toY(titles[i][j]);
//                }
//                dis += Math.abs(i - x) + Math.abs(j - y);
//            }
//        }
//        return dis;
//    }
    public boolean equals(Object o){

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Board b = (Board) o;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (titles[i][j] != b.titles[i][j]) {
                    return false;
                }
            }
        }
        return true;

    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == 0) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = 0;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = 0;
                }
            }
        }
        return neighbors;
    }

    /** Returns the string representation of the board.
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    public static void main(String[] args) {

        int[][] a = {{8,1,3}, {4,0,2}, {7,6,5}};
        Board board = new Board(a);
        System.out.println(board);
        System.out.println(board.hamming());
        System.out.println(board.manhattan());

    }

}


//public class Board implements WorldState{
//
//    private final int BLANK = 0;
//    private int N;
//    private int[][] board;
//    private int[][] goal;
//
//    public Board(int[][] tiles) {
//        this.N = tiles.length;
//        // goal state
//        goal = new int[N][N];
//        for (int i = 0; i < N; i++) {
//            for (int j = 0; j < N; j++) {
//                goal[i][j] = i * N + j + 1;
//            }
//        }
//        goal[N-1][N-1] = 0;
//        // copy
//        board = new int[N][N];
//        for (int i = 0; i < N; i++) {
//            System.arraycopy(tiles[i], 0, board[i], 0, N);
//        }
//
//    }
//    public int tileAt(int i, int j) {
//        if (i < 0 || i >= N || j < 0 || j >= N) {
//            throw new IndexOutOfBoundsException("OUT OF SCOPE!!!");
//        }
//        return board[i][j];
//    }
//    public int size() {
//        return N;
//    }
//
//    public int hamming() {
//        int count = 0;
//        for (int i = 0; i < N; i++) {
//            for (int j = 0; j < N; j++) {
//                if (board[i][j] == 0) continue;
//                if (board[i][j] != goal[i][j]) {
//                    count++;
//                }
//            }
//        }
//        return count;
//    }
//
//    private int toX(int v){
//        return (v - 1) / N;
//    }
//    private int toY(int v) {
//        return (v - 1) % N ;
//    }
//
//    public int manhattan() {
//
//        int dis = 0;
//        for (int i = 0; i < N; i++) {
//            for (int j = 0; j < N; j++) {
//                int x = 0;
//                int y = 0;
//                if (board[i][j] == 0) {
//                    continue;
//                } else {
//                    x = toX(board[i][j]);
//                    y = toY(board[i][j]);
//                }
//                dis += Math.abs(i - x) + Math.abs(j - y);
//            }
//        }
//        return dis;
//    }
//    public boolean equals(Object y) {
//        Board other = (Board) y;
////        for (int i = 0; i < N; i++) {
////            for (int j = 0; j < N; j++) {
////                if (board[i][j] != other.board[i][j]) {
////                    return false;
////                }
////            }
////        }
////        return true;
//        return this.toString().equals(other.toString());
//    }
//
//    @Override
//    public int estimatedDistanceToGoal() {
//        return manhattan();
//    }
//
//    @Override
//    public Iterable<WorldState> neighbors() {
//        Queue<WorldState> neighbors = new Queue<>();
//        int hug = size();
//        int bug = -1;
//        int zug = -1;
//        for (int rug = 0; rug < hug; rug++) {
//            for (int tug = 0; tug < hug; tug++) {
//                if (tileAt(rug, tug) == BLANK) {
//                    bug = rug;
//                    zug = tug;
//                }
//            }
//        }
//        int[][] ili1li1 = new int[hug][hug];
//        for (int pug = 0; pug < hug; pug++) {
//            for (int yug = 0; yug < hug; yug++) {
//                ili1li1[pug][yug] = tileAt(pug, yug);
//            }
//        }
//        for (int l11il = 0; l11il < hug; l11il++) {
//            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
//                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
//                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
//                    ili1li1[l11il][lil1il1] = BLANK;
//                    Board neighbor = new Board(ili1li1);
//                    neighbors.enqueue(neighbor);
//                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
//                    ili1li1[bug][zug] = BLANK;
//                }
//            }
//        }
//        return neighbors;
//    }
//
//    /** Returns the string representation of the board.
//     * Uncomment this method. */
//    public String toString() {
//        StringBuilder s = new StringBuilder();
//        int N = size();
//        s.append(N + "\n");
//        for (int i = 0; i < N; i++) {
//            for (int j = 0; j < N; j++) {
//                s.append(String.format("%2d ", tileAt(i,j)));
//            }
//            s.append("\n");
//        }
//        s.append("\n");
//        return s.toString();
//    }
//
//}
