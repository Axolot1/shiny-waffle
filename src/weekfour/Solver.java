package weekfour;
import java.util.LinkedList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    // find a solution to the initial board (using the A* algorithm)
    // The constructor should throw a java.lang.NullPointerException
    // if passed a null argument.
    private boolean solvable;
    private MinPQ<SearchNode> priQue;
    private LinkedList<Board> solution;

    public Solver(Board initial) {
        if (initial == null) {
            throw new NullPointerException();
        }
        solvable = true;
        priQue = new MinPQ<>(100);
        SearchNode goal = aStartAlgo(initial);
        if (goal != null) {
            constructPath(goal);
        }
    }

    private void constructPath(SearchNode goal) {
        solution = new LinkedList<>();
        while (goal != null) {
            solution.addFirst(goal.board);
            goal = goal.preNode;
        }
    }

    private SearchNode aStartAlgo(Board ini) {
        SearchNode startNode = new SearchNode(0, ini, null);
        MinPQ<SearchNode> tque = new MinPQ<>();
        Board twin = ini.twin();
        SearchNode tStartNode = new SearchNode(0, twin, null);

        priQue.insert(startNode);
        tque.insert(tStartNode);

        while (!priQue.isEmpty() && !tque.isEmpty()) {
            SearchNode node = priQue.delMin();
            SearchNode tNode = tque.delMin();

            if (tNode.board.isGoal()) {
                solvable = false;
                return null;
            }

            if (node.board.isGoal()) {
                solvable = true;
                return node;
            }

            for (Board neighbor : node.board.neighbors()) {
                if (node.preNode != null && neighbor.equals(node.preNode.board)) {
                    continue;
                }
                priQue.insert(new SearchNode(node.move + 1, neighbor, node));
            }

            for (Board neighbor : tNode.board.neighbors()) {
                if (tNode.preNode != null && neighbor.equals(tNode.preNode.board)) {
                    continue;
                }
                tque.insert(new SearchNode(node.move + 1, neighbor, tNode));
            }
        }
        return null;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable())
            return -1;
        return solution.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        return solution;

    }

    private class SearchNode implements Comparable<SearchNode> {
        private int move;
        private int priority;
        private Board board;
        private SearchNode preNode;

        SearchNode(int move, Board board, SearchNode preNode) {
            this.move = move;
            this.board = board;
            this.preNode = preNode;
            this.priority = board.manhattan() + move;
        }

        @Override
        public int compareTo(SearchNode o) {
            if (this.priority != o.priority)
                return this.priority - o.priority;
            return (this.priority - this.move) - (o.priority - o.move);
        }

    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        long start = System.currentTimeMillis();
        Solver solver = new Solver(initial);
        System.out.println(System.currentTimeMillis() - start);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);

        }
    }
}
