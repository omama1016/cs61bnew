package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * ClassName: Solver
 * Package: hw4.puzzle
 * Description:
 *
 * @Author xyz
 * @Create 2023/11/14 9:59
 * @Version 1.0
 */
public class Solver {

    private MinPQ<SearchNode> pq;
    private int numberOfMoves;
    private Stack<WorldState> path;

    //public int totalNumEnqueue;
    private class SearchNode implements Comparable{

        private WorldState worldState;
        private int currentMoves;
        private SearchNode parent;

        private int priority;

        public SearchNode(){}
        public SearchNode(WorldState worldState, int currentMoves, SearchNode parent) {
            this.worldState = worldState;
            this.currentMoves = currentMoves;
            this.parent = parent;
            this.priority = this.currentMoves + this.worldState.estimatedDistanceToGoal();
        }

        @Override
        public int compareTo(Object o) {
            SearchNode node = (SearchNode) o;
            return this.priority - node.priority;
        }
    }

    public Solver(WorldState initial) {
        pq = new MinPQ<>();
        path = new Stack<>();
        //visited = new TreeSet<>();
        SearchNode initialSearchNode = new SearchNode(initial, 0, null);
        pq.insert(initialSearchNode);
        //totalNumEnqueue++;
        while (!pq.isEmpty()) {
            SearchNode node = pq.delMin();
            //visited.add(node.worldState);
            if (node.worldState.isGoal()) {
                SearchNode cur = node;
                while (cur != null) {
                    numberOfMoves++;
                    path.push(cur.worldState);
                    cur = cur.parent;
                }
                return;
            }
            for (WorldState ws : node.worldState.neighbors()) {
                if (node.parent == null || !node.parent.worldState.equals(ws)) {
                    SearchNode newNode = new SearchNode(ws, node.currentMoves+1, node);
                    pq.insert(newNode);
                    //totalNumEnqueue++;
                }
            }
        }
    }
    public int moves() {
        return numberOfMoves - 1;
    }

    public Iterable<WorldState> solution() {
        return path;
    }
}
