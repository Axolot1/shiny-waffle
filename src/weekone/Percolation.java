package weekone;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

//two WeightedQuickUnionUF
public class Percolation {
    private WeightedQuickUnionUF weighteduf;
    private WeightedQuickUnionUF wuf2;
    private boolean[][] table;
    private int N;
    private int virtualTop;
    private int virtualBtm;
    // 0000
    // 0001
    // 0010
    // 0100

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException(N + "<= 0");
        }
        weighteduf = new WeightedQuickUnionUF((N + 1) * (N + 1));
        wuf2 = new WeightedQuickUnionUF((N+1)*(N+1));
        table = new boolean[N + 1][N + 1];
        this.N = N;
        virtualTop = 0;
        virtualBtm = 1;
    }

    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        validate(i, j);
        table[i][j] = true;
        int cur = xyTo1D(i, j);

        if (i == 1) { // connected to the virtual top
            weighteduf.union(cur, virtualTop);
            wuf2.union(cur, virtualTop);
        }

        if (i == N) {
            weighteduf.union(cur, virtualBtm);
        }

        if (table[i - 1][j]) { // top
            weighteduf.union(cur, xyTo1D(i - 1, j));
            wuf2.union(cur, xyTo1D(i - 1, j));
        }

        if (i != N && table[i + 1][j]) { // bottom
            weighteduf.union(cur, xyTo1D(i + 1, j));
            wuf2.union(cur, xyTo1D(i + 1, j));
        }

        if (table[i][j - 1]) { // left
            weighteduf.union(cur, xyTo1D(i, j - 1));
            wuf2.union(cur, xyTo1D(i, j - 1));
        }

        if (j != N && table[i][j + 1]) { // right
            weighteduf.union(cur, xyTo1D(i, j + 1));
            wuf2.union(cur, xyTo1D(i, j + 1));
        }
    }

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        validate(i, j);
        return table[i][j];
    }

    public boolean isFull(int i, int j) {
        validate(i, j);
        return isOpen(i, j) && wuf2.connected(xyTo1D(i, j), virtualTop);
    }

    public boolean percolates() {
        return weighteduf.connected(virtualTop, virtualBtm);
    }

    private void validate(int i, int j) {
        if (i < 1 || i > N || j < 1 || j > N) {
            throw new IndexOutOfBoundsException("out side prescribed range" + "i:" + i + "j:" + j);
        }
    }

    private int xyTo1D(int i, int j) {
        return i * (N + 1) + j;
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(4);
        p.open(1, 1);
        p.open(1, 2);
        System.out.println(p.weighteduf.connected(p.xyTo1D(1, 1), p.xyTo1D(1, 2)));
    }
}
