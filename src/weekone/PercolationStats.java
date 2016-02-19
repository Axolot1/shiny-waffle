package weekone;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    // perform T independent experiments on an N-by-N grid
    // The constructor should throw a java.lang.IllegalArgumentException
    // if either N ≤ 0 or T ≤ 0.
    private int N;
    private int T;
    private double[] data;

    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        data = new double[T];
        this.N = N;
        this.T = T;
        calculate();
    }

    private void calculate() {
        double count;
        Percolation percolation;
        for (int k = 0; k < data.length; k++) {
            count = 0;
            percolation = new Percolation(N);
            while (!percolation.percolates()) {
                int i = StdRandom.uniform(N) + 1;
                int j = StdRandom.uniform(N) + 1;
                if (!percolation.isOpen(i, j)) {
                    percolation.open(i, j);
                    count++;
                }
            }
            data[k] = count / (N * N);
        }

    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(data);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (T == 1) {
            return Double.NaN;
        } else {
            return StdStats.stddev(data);
        }
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double u = mean();
        double q = stddev();
        return u - 1.96 * q / Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double u = mean();
        double q = stddev();
        return u + 1.96 * q / Math.sqrt(T);
    }

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(N, T);
        StdOut.println("mean                    = " + percolationStats.mean());
        StdOut.println("stddev                  = " + percolationStats.stddev());
        StdOut.println("95% confidence interval = " + percolationStats.confidenceLo() + ", "
                + percolationStats.confidenceHi());
    }
}
