package weekone;

import java.util.Arrays;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
/**
 * 解法：判断一个点的是否full时，改为判断其所在component的根节点的是否为full
 * 所以在每次open一个节点时，必须实时更新其根节点的状态。
 * 
 * table存储每个位置的4个状态block, open, connectTop, connectBtm
 * 每当open一个点时，首先记录其当前状态（是否连接到top或者btm）
 * 在将其与邻接的点连接时，首先记录下邻接点的根节点的状态
 * 在连接完周围的点后更新本节点的根节点的状态（为4个根节点和当前节点的状态之和）
 * @author axolotl
 *
 */
public class Percolation2 {
    private static final byte BLOCK = 0;
    private static final byte OPEN = 1;
    private static final byte CONNECT_TOP = 2;
    private static final byte CONNECT_BTM = 4;
    private static final byte PERCOLATED = 6;

    private WeightedQuickUnionUF weighteduf;
    private boolean isPercolated;
    private byte[][] table;
    private int N;

    // 0000
    // 0001
    // 0010
    // 0100

    public Percolation2(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException(N + "<= 0");
        }
        weighteduf = new WeightedQuickUnionUF((N + 1) * (N + 1));
        table = new byte[N + 1][N + 1];
        this.N = N;
    }

    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        validate(i, j);
        table[i][j] |= OPEN;
        int cur = xyTo1D(i, j);

        if (i == 1) { // connected to the virtual top
            table[i][j] |= CONNECT_TOP;
        }

        if (i == N) {
            table[i][j] |= CONNECT_BTM;
        }

        byte topRoot = BLOCK;
        if ((table[i - 1][j] & OPEN) == OPEN) { // top
            int p = xyTo1D(i - 1, j);
            int rootp = weighteduf.find(p);
            topRoot = table[getX(rootp)][getY(rootp)];
            weighteduf.union(cur, p);
        }

        byte btmroot = BLOCK;
        if (i != N && (table[i + 1][j] & OPEN) == OPEN) { // bottom
            int p = xyTo1D(i + 1, j);
            int rootp = weighteduf.find(p);
            btmroot = table[getX(rootp)][getY(rootp)];
            weighteduf.union(cur, p);
        }

        byte leftRoot = BLOCK;
        if ((table[i][j - 1] & OPEN) == OPEN) { // left
            int p = xyTo1D(i, j - 1);
            int rootp = weighteduf.find(p);
            leftRoot = table[getX(rootp)][getY(rootp)];
            weighteduf.union(cur, p);
        }

        byte rightRoot = BLOCK;
        if (j != N && (table[i][j + 1] & OPEN) == OPEN) { // right
            int p = xyTo1D(i, j + 1);
            int rootp = weighteduf.find(p);
            rightRoot = table[getX(rootp)][getY(rootp)];
            weighteduf.union(cur, p);
        }

        int newRoot = weighteduf.find(cur);
        int x = getX(newRoot);
        int y = getY(newRoot);
        table[x][y] |= (byte) (topRoot | leftRoot | rightRoot | btmroot | table[i][j]);

        if ((table[x][y] & PERCOLATED) == PERCOLATED) {
            isPercolated = true;
        }
    }

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        validate(i, j);
        return (table[i][j] & OPEN) == OPEN;
    }

    public boolean isFull(int i, int j) {
        validate(i, j);
        return isConnectedTop(i, j);
    }

    public boolean percolates() {
        return isPercolated;
    }

    private void validate(int i, int j) {
        if (i < 1 || i > N || j < 1 || j > N) {
            throw new IndexOutOfBoundsException("out side prescribed range" + "i:" + i + "j:" + j);
        }
    }

    private int xyTo1D(int i, int j) {
        return i * (N + 1) + j;
    }

    private int getX(int p) {
        return p / (N + 1);
    }

    private int getY(int p) {
        return p % (N + 1);
    }

    private byte getRootSta(int i, int j) {
        int p = xyTo1D(i, j);
        int rootp = weighteduf.find(p);
        return table[getX(rootp)][getY(rootp)];
    }
    
    private boolean isConnectedTop(int i, int j){
        int p = xyTo1D(i, j);
        int root = weighteduf.find(p);
        int x = getX(root);
        int y = getY(root);
        if((table[x][y]&CONNECT_TOP) == CONNECT_TOP){
            return true;
        }else{
            return false;
        }
    }

    public static void main(String[] args) {
//        Percolation2 percolation2 = new Percolation2(3);
//        percolation2.open(3, 1);
//        percolation2.open(2, 1);
//        percolation2.open(1, 1);
//        percolation2.print2DArray();
//        System.out.println(percolation2.percolates());
    }

    // 1 3
    // 2 3
    // 3 3
    // 3 1
    // 2 1
    // 1 1
    private void print2DArray() {
        for (int i = 0; i < table.length; i++) {
            System.out.println(Arrays.toString(table[i]));
        }
    }

}
