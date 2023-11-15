package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
//    private Maze maze;
//    public MazeCycles(Maze m) {
//        super(m);
//        maze = m;
//        edgeTo[0] = 0;
//        distTo[0] = 0;
//    }
//
//    @Override
//    public void solve() {
//        // TODO: Your code here!
//        detectCycleDFS(0);
//    }
//
//    // Helper methods go here
//    private void detectCycleDFS(int v) {
//        marked[v] = true;
//        announce();
//        for (int w : maze.adj(v)) {
//            if (marked[w] && edgeTo[v] != w) {
//                edgeTo[w] = w;
//                announce();
//                return;
//            }
//
//            if (!marked[w]) {
//                edgeTo[w] = v;
//                announce();
//                distTo[w] = distTo[v] + 1;
//                detectCycleDFS(w);
//            }
//        }
//    }
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int[] parent;
    private boolean foundCircle = false;

    public MazeCycles(Maze m) {
        super(m);
        parent = new int[maze.V()];
    }

    @Override
    public void solve() {
        dfs(0);
    }

    private void dfs(int v) {
        marked[v] = true;
        announce();

        for (int w : maze.adj(v)) {
            if (foundCircle) {
                return;
            }
            if (w != parent[v]) {
                if (marked[w]) {
                    edgeTo[w] = v;
                    announce();
                    while (v != w) {
                        int n = parent[v];
                        edgeTo[v] = n;
                        announce();
                        v = n;
                    }
                    foundCircle = true;
                } else {
                    parent[w] = v;
                    announce();
                    dfs(w);
                }
            }
        }
    }
}

