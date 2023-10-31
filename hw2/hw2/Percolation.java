package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // create N-by-N grid, with all sites initially blocked

    private WeightedQuickUnionUF wqu;
    private WeightedQuickUnionUF back;
    private boolean[][] sites;
    private int N;
    private int openSize;

    public Percolation(int N) {
        if (N < 0) throw new java.lang.IllegalArgumentException();
        this.N = N;
        wqu = new WeightedQuickUnionUF(N*N+2);
        back = new WeightedQuickUnionUF(N*N+1);
        sites = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            wqu.union(N*N, i); // top
        }
        for (int i = N*N-N; i < N*N; i++) {
            wqu.union(N*N+1, i);
        }
        for (int i = 0; i < N; i++) {
            back.union(N*N, i); // top
        }
    }

    private boolean outOfRange(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            return true;
        }
        return false;
    }

    private int map2DTo1D(int row, int col) {
        return row*N + col;
    }

    private void unionNeighbors(int row, int col) {
        if (!outOfRange(row-1, col) && isOpen(row-1, col)){
            wqu.union(map2DTo1D(row, col), map2DTo1D(row-1, col));
            back.union(map2DTo1D(row, col), map2DTo1D(row-1, col));
        }
        if (!outOfRange(row+1, col) && isOpen(row+1, col)) {
            wqu.union(map2DTo1D(row, col), map2DTo1D(row+1, col));
            back.union(map2DTo1D(row, col), map2DTo1D(row+1, col));
        }
        if (!outOfRange(row, col-1) && isOpen(row, col-1)) {
            wqu.union(map2DTo1D(row, col), map2DTo1D(row, col-1));
            back.union(map2DTo1D(row, col), map2DTo1D(row, col-1));
        }
        if (!outOfRange(row, col+1) && isOpen(row, col+1)) {
            wqu.union(map2DTo1D(row, col), map2DTo1D(row, col+1));
            back.union(map2DTo1D(row, col), map2DTo1D(row, col+1));
        }
    }
    // open the site (row, col) if it is not open already
    public void open(int row, int col)  {
        if (outOfRange(row, col)) throw new java.lang.IndexOutOfBoundsException();
        if (!sites[row][col]) {
            sites[row][col] = true;
            openSize++;
            unionNeighbors(row, col);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (outOfRange(row, col)) throw new java.lang.IndexOutOfBoundsException();
        return sites[row][col] == true;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (outOfRange(row, col)) throw new java.lang.IndexOutOfBoundsException();
        return isOpen(row, col) && back.connected(row*N + col, N*N);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openSize;
    }

    // does the system percolate?
    public boolean percolates() {
        return wqu.connected(N*N, N*N+1);
    }
}
