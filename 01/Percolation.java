import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // create N-by-N grid, with all sites blocked

    private final int gridSize;
    private WeightedQuickUnionUF uf;
    private boolean[] open;
    public Percolation(int N)   {
        gridSize = N;
        open = new boolean[N*N+2];
        uf = new WeightedQuickUnionUF(N*N + 2);
        
        for (int i = N*N+1; i >= 0; i--) {
            open[i] = false;
        }    
        open[0] = true;
        open[N*N+1] = true;

    }

    private boolean inRangeIndex(int i , int j) {
        int c = index(i , j);
        boolean ar = (c > 0 && c <= gridSize * gridSize);
        boolean inR = i > 0 && i <= gridSize;
        boolean inC = j > 0 && j <= gridSize;
        return ar && inR && inC;
    }

    private int index(int i , int j) {
        return  (i - 1) * gridSize + j;
    }

    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        if (!inRangeIndex(i , j)) {
            throw new IndexOutOfBoundsException();
        }
        int c = index(i , j);
        open[c] = true;

        int[] row = {1, gridSize};
        int[] merge = {0, gridSize * gridSize + 1};

        for (int o = 0; o < 2; o++) {
            if (i == row[o] && !uf.connected(merge[o], c)) {
                uf.union(merge[o] , c);
            } 
        }

        int[] dx = {1 , 0 , -1 , 0};
        int[] dy = {0 , 1 , 0 , -1};

        int n;
        for (int k = 0; k < 4; ++k) {
            if (inRangeIndex(i + dx[k], j + dy[k])) {
                n = index(i + dx[k], j + dy[k]);
                if (open[n] && !uf.connected(c , n)) {
                    uf.union(c , n);
                }
            }
        }
    }

    // is site (row i, column j) open?
    public boolean isOpen(int i , int j) {

        if (!inRangeIndex(i , j)) {
            throw new IndexOutOfBoundsException();
        }
        return (open[index(i , j)]);
    }

    // is site (row i, column j) full?
    public boolean isFull(int i , int j) {
        if (!inRangeIndex(i , j)) {
            throw new IndexOutOfBoundsException();
        }
        return (uf.connected(0 , index(i , j)));
    }

    // does the system percolate?
    public boolean percolates()  {
        return uf.connected(0 , gridSize * gridSize + 1);   
    }
    public static void main(String[] args)
    {


    }
}
