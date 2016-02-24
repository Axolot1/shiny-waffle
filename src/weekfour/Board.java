package weekfour;
import java.util.ArrayList;

import edu.princeton.cs.algs4.In;

public class Board {
    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    private int[][] blocks;
    private int N;
    private int zi;
    private int zj;

    public Board(int[][] blocks) {
        this.N = blocks.length;
        this.blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                this.blocks[i][j] = blocks[i][j];
                if (blocks[i][j] == 0) {
                    zi = i;
                    zj = j;
                }
            }
    }

    // board dimension N
    public int dimension() {
        return N;
    }

    // number of blocks out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] == 0)
                    continue;
                if (blocks[i][j] != getRightVal(i, j)) {
                    count++;
                }
            }
        return count;
    }

    private int getRightVal(int i, int j) {
        return N * i + j + 1;
    }

    // 8 1 3 1 2 3 1 2 3 4 5 6 7 8 1 2 3 4 5 6 7 8
    // 4 2 4 5 6 ---------------------- ----------------------
    // 7 6 5 7 8 1 1 0 0 1 1 0 1 1 2 0 0 2 2 0 3
    //
    // initial goal Hamming = 5 + 0 Manhattan = 10 + 0

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] == 0)
                    continue;
                int count = Math.abs(getI(blocks[i][j]) - i) + Math.abs(getJ(blocks[i][j]) - j);
                sum += count;
            }
        return sum;
    }

    private int getI(int n) {
        return (n - 1) / N;
    }

    private int getJ(int n) {
        return (n - 1) % N;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] == 0)
                    continue;
                if (blocks[i][j] != getRightVal(i, j))
                    return false;
            }
        return true;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int i1 = -1, j1 = -1, i2 = -1, j2 = -1;
        tag: for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] == 0)
                    continue;
                if (i1 == -1) {
                    i1 = i;
                    j1 = j;
                    continue;
                }
                if (i2 == -1) {
                    i2 = i;
                    j2 = j;
                }
                break tag;
            }
        swap(i1, j1, i2, j2);
        Board temp = new Board(blocks);
        swap(i1, j1, i2, j2);
        return temp;
    }

    private void swap(int i1, int j1, int i2, int j2) {
        int t = blocks[i1][j1];
        blocks[i1][j1] = blocks[i2][j2];
        blocks[i2][j2] = t;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null)
            return false;
        if (this.getClass() != y.getClass())
            return false;
        Board other = (Board) y;
        if (this.N != other.N)
            return false;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                if (this.blocks[i][j] != other.blocks[i][j])
                    return false;
            }
        return true;
    }

    // all neighboring boards
    // 8 1 3 8 1 3 8 1 8 1 3 8 1 3
    // 4 2 4 2 4 2 3 4 2 4 2 5
    // 7 6 5 7 6 5 7 6 5 7 6 5 7 6
    //
    // previous search node neighbor neighbor neighbor
    // (disallow)
    public Iterable<Board> neighbors() {
        ArrayList<Board> list = new ArrayList<>();
        addNeighbor(zi - 1, zj, list);
        addNeighbor(zi, zj + 1, list);
        addNeighbor(zi, zj - 1, list);
        addNeighbor(zi + 1, zj, list);
        return list;
    }

    private void addNeighbor(int i, int j, ArrayList<Board> list) {
        if (isValid(i) && isValid(j)) {
            swap(zi, zj, i, j);
            list.add(new Board(blocks));
            swap(zi, zj, i, j);
        }
    }

    private boolean isValid(int i) {
        if (i >= 0 && i < N)
            return true;
        return false;
    }

    // string representation of this board (in the output format specified
    // below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        System.out.println(initial);
        System.out.println("hamming:" + initial.hamming());
        System.out.println("manhattan:" + initial.manhattan());
        System.out.println("isGoal?:" + initial.isGoal());
        System.out.println("Neighbors:");
        for (Board b : initial.neighbors())
            System.out.println(b);
        System.out.println("Twins:");
        System.out.println(initial.twin().toString());

    }
}
