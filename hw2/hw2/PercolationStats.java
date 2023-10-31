package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {

    private double[] rates;
    //perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf){
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("Illegal Augment!");
        }
        rates = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation p = pf.make(N);
            while (!p.percolates()) {
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                p.open(row, col);
            }
           rates[i] = (p.numberOfOpenSites() * 1.0 / (N*N));
        }
    }
    public double mean() {
        return StdStats.mean(rates);
    }
    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(rates);
    }
    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(rates.length);
    }
    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(rates.length);
    }
}
