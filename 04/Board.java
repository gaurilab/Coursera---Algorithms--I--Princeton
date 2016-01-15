
//import java.util.Arrays;
//import java.util.NoSuchElementException;
//import java.util.ArrayList;
import edu.princeton.cs.algs4.StdOut;
//import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

public class Board {
    private final int[][] blocks;
    private final int senitel;
    // construct a board from an N-by-N array of blocks
    public Board(int[][] b) {
        int zero =  -1;
        blocks = new int[b.length][b.length];
        for (int i = 0; i < blocks.length; ++i) {
            for (int j = 0; j < blocks.length; ++j) {
                blocks[i][j] = b[i][j];
                if (blocks[i][j] == 0) {
                    zero = (i * dimension() + j);
                }
            }
        }
        
        senitel = zero;
        assert (inRangeIndex(senitel / dimension() , senitel % dimension()));
        
    }
    
    // (where blocks[i][j] = block in row i, column j)
    // board dimension N
    public int dimension() {
        return blocks.length;
    }
    
//    private int position(int i , int j) {
//        return (i * dimension() + j);
//    }
    
    private int noOfBlocks() {
        return dimension() * dimension() - 1;
    }
    
//    private int[] position(int v) {
//        int r = v / dimension();
//        int c = v % dimension();
//        
//        int[] ans = new int[2];
//        ans[0] = r;
//        ans[1] = c;
//        return ans;
//        
//    }
    
    // number of blocks out of place
    public int hamming() {
        int ans = 0;
        for (int i = 0; i < dimension(); ++i) {
            for (int j = 0; j < dimension(); ++j) {
                if (blocks[i][j] == 0) continue; 
                if (blocks[i][j] != (i * dimension() + j + 1) % (noOfBlocks() + 1)) {
                    ans++;
                }
            }
        }
        return ans;
    }
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int dy = 0;
        int dx = 0;
        
        for (int i = 0; i < blocks.length; ++i) {
            for (int j = 0; j < blocks.length; ++j) {
                if (blocks[i][j] == 0) continue; 
                if (blocks[i][j] != (i * dimension() + j + 1) % (noOfBlocks() + 1)) {
                    int r = (blocks[i][j] - 1) / dimension();
                    int c = (blocks[i][j] - 1) % dimension();
//                     StdOut.printf("For [%d][%d]=%d actual[%d][%d] dx=%d dy=%d\n",
//                     i , j , blocks[i][j] , r , c , dx , dy);             
                    dx += Math.abs(r - i);
                    dy += Math.abs(c - j);
//                    StdOut.printf("For [%d][%d]=%d actual[%d][%d] dx=%d dy=%d\n",
//                    i , j , blocks[i][j] , r , c , dx , dy);
                }                
            }
        }
        return dx + dy;
    }
    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < blocks.length; ++i) {
            for (int j = 0; j < blocks.length; ++j) {
                if (blocks[i][j] != (i * dimension() + j + 1) % (noOfBlocks() + 1))
                    return false;
            }
        }
        return true;
    }               
    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        
        int i = senitel / dimension();
        int j = senitel % dimension();        
        int[] cand = new int[2];
        
        int[] dx = {1 , 0 , -1 , 0};
        int[] dy = {0 , 1 , 0 , -1};
        int z = 0;
        for (int k = 0; k < 4; ++k) {
            int px = i + dx[k];
            int py = j + dy[k];
            if (inRangeIndex(px , py)) {
                cand[z++] = px * dimension() + py; 
            }
            if (z == 2) break;            
            
        }
        int x = cand[0];
        int y = cand[1];
        
        int[][] newblocks = new int[dimension()][dimension()];
        for (int m = 0; m < dimension(); ++m) {
            for (int n = 0; n < dimension(); ++n) {
                newblocks[m][n] = blocks[m][n];
            }
        }
        
        assert (newblocks[x / dimension()][x % dimension()] != 0);
        assert (newblocks[y / dimension()][y % dimension()] != 0);
        
        int r = y / dimension();
        int c = y % dimension();
        
        int t = newblocks[r][c];
        newblocks[r][c] = newblocks[x / dimension()][x % dimension()];
        newblocks[x / dimension()][x % dimension()] = t;
        
        Board b = new Board(newblocks);
        return b;    
    }
    
    // does this board equal y?
    public boolean equals(Object x) {
        //StdOut.println("Calling equals");
        Board y = (Board) x;
        if (y.dimension() != dimension()) return false;
        for (int i = 0; i < blocks.length; ++i) {
            for (int j = 0; j < blocks.length; ++j) {
                if (blocks[i][j] != y.blocks[i][j])
                    return false;
            }
        }
        return true;
    }
    
    private boolean inRangeIndex(int i , int j) {
        int c = i * dimension() + j;
        boolean ar = (c >= 0 && c <= noOfBlocks());
        boolean inR = i >= 0 && i < dimension();
        boolean inC = j >= 0 && j < dimension();
        return ar && inR && inC;
    } 
    
    // all neighboring boards
    public Iterable<Board> neighbors() {
        int i = senitel / dimension();
        int j = senitel % dimension();
        
        assert (blocks[i][j] == 0);
        
        Stack<Board> s = new Stack<Board>();
        
        int[] dx = {1 , 0 , -1 , 0};
        int[] dy = {0 , 1 , 0 , -1};
        
        for (int k = 0; k < 4; ++k) {
            
            int px = i + dx[k];
            int py = j + dy[k];
//            StdOut.printf("%d %d , %d ,%d\n", i, j, px, py);            
            if (inRangeIndex(px , py)) {
                
                int[][] newblocks = new int[dimension()][dimension()];
                
                for (int m = 0; m < blocks.length; ++m) {
                    for (int n = 0; n < blocks.length; ++n) {
                        newblocks[m][n] = blocks[m][n];
                    }
                }
                
                newblocks[i][j] = blocks[px][py];
                newblocks[px][py] = 0;
                
                Board b = new Board(newblocks);
                s.push(b);
                
            }
        }
        return s;
        
        
    }
    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension() + "\n");
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    // unit tests (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        StdOut.println(initial);
//        StdOut.println(initial.isGoal());
        StdOut.println(initial.manhattan());
//        StdOut.println(initial.hamming());
//        
        StdOut.println(initial.twin());
        StdOut.println(initial.twin().twin());

        StdOut.println(initial.twin().twin().equals(initial));
        StdOut.println(initial.twin().isGoal());
        
        StdOut.println(initial.twin().hamming());
        
        StdOut.println(initial.twin().manhattan());
        
        for (Board b: initial.neighbors()) {
            StdOut.println(b);
            StdOut.println(b.isGoal());
            StdOut.println(b.manhattan());
            StdOut.println(b.hamming());
        }
        
        
        // solve the puzzle
//        Solver solver = new Solver(initial);
//        
//        // print solution to standard output
//        if (!solver.isSolvable())
//            StdOut.println("No solution possible");
//        else {
//            StdOut.println("Minimum number of moves = " + solver.moves());
//            for (Board board : solver.solution())
//                StdOut.println(board);
//        }
    }
    
}
