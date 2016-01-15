import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.MinPQ;

public class Solver {
    private Board cou;
    private Board me;
    private int steps;
    private boolean isPossible;
    private Stack<Node>  mPath;
    private Stack<Node>  cPath;
    
    private class Node  implements Comparable<Node> {
        private final Board c;
        private final Board p;
        private final int d;
        
        public Node(Board curr, int depth , Board parent) {
            c = curr;
            d = depth;
            p = parent;
        }
        
        public int compareTo(Node n) {
            return (c.manhattan() + d - n.c.manhattan() - n.d);
        }
        
        public String toString() {
            StringBuilder s = new StringBuilder();
            if (p != null) s.append(p + "\n to \n");
            s.append(c + "\n Depth" + d + "\n manhattan d:" + c.manhattan() + "\n");
            return s.toString();
        }
    };
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        steps = -1;
        isPossible = false;
        me = initial;
        cou = initial.twin();
        MinPQ<Node> mPQ = new MinPQ<Node>();
        MinPQ<Node> cPQ = new MinPQ<Node>();
        mPath = new Stack<Node>();
        cPath = new Stack<Node>();
        //insert
        // Remove from PQ 
        
        mPQ.insert(new Node(initial , 0 , null));
        cPQ.insert(new Node(cou , 0 , null));
        
        
        while (!mPQ.isEmpty() && !cPQ.isEmpty()) {
            
            Node sn = mPQ.delMin();
            Node cn = cPQ.delMin();
        
            mPath.push(sn);
            cPath.push(cn);
            
            //StdOut.println(sn);
            if (sn.c.isGoal()) {
                steps = sn.d;
                isPossible = true;
                //clear cpath
                cPath = null;
                break;
            }
            if (cn.c.isGoal()) {
                //clear cpath and mpath
                cPath = null;
                mPath = null;
                break;
            }
            
            for (Board b : sn.c.neighbors()) {
                if (sn.p == null ||  !b.equals(sn.p)) {
                    mPQ.insert(new Node(b , sn.d + 1 , sn.c));
                }
            }
            
            for (Board b : cn.c.neighbors()) {
                if (cn.p == null ||  !b.equals(cn.p)) {
                    cPQ.insert(new Node(b , cn.d + 1 , cn.c));
                }
            }           
        }
        
        
    }
    
    public boolean isSolvable() {
        return isPossible;   
    } // is the initial board solvable?
    public int moves() {
        return steps;
        
    } // min number of moves to solve initial board; -1 if unsolvable
    public Iterable<Board> solution() {
        
        Stack<Board> path = new Stack<Board>();
        if (isSolvable()) {
            assert (mPath.peek().c.isGoal());
            Board parent =  mPath.peek().p;
            int nextD = mPath.peek().d - 1;
            path.push(mPath.peek().c);
            for (Node e : mPath) {
                if (parent != null && e.c.equals(parent) && e.d == nextD) {
                    path.push(e.c);
                    parent = e.p; 
                    nextD--;
                }
            }
        }
        return path;
        
    } // sequence of boards in a shortest solution; null if unsolvable
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
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            int l = 0;
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.printf("count: %d\n", l);
                ++l;
                StdOut.println(board);
            }
        }
    } // solve a slider puzzle (given below)
}
