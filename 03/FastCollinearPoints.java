import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;

public class FastCollinearPoints {
    private static final int T = 4;
    private ArrayList<Segments> result;
    static private final double POSITIVE_INFINITY = Double.POSITIVE_INFINITY;
    static private final double  NEGATIVE_INFINITY = Double.NEGATIVE_INFINITY;

    private class Segments {
        private Point right;
        private Point left;

        //Input shall be sorted 
        public Segments(Point[] points) { //1 slopeTo
            double slope =  points[points.length - 1].slopeTo(points[0]);
            if (Double.compare(slope ,0) < 0) {
                left = points[points.length - 1];
                right = points[0];
            } else { //>=
                right = points[points.length - 1];
                left = points[0];
            }
        }

        public String toString() {
            double slope =  left.slopeTo(right);
            return left + " -> " + right + "(" + slope + ")";
        }

        public boolean aligned(Point[] x) {

            Point r = x[x.length - 1];
            double m = r.slopeTo(x[0]);

            for (int i = 1; i < 4; ++i) {
                if (r.compareTo(x[i]) != 0 && Double.compare(m  ,   r.slopeTo(x[i])) != 0)  return false;
            }
            return true;
        }

        public void merge(Point[] x) {
            Segments s = new Segments(x);
            left = s.left;
            right = s.right;
        }
    }

    private void updateSegments(Point[] points) {
        Segments newS = new Segments(points); 
        for (Segments s: result) {
            Point[] x = new Point[4];
            x[0] =  s.right;
            x[1] =  s.left;
            x[2] =  newS.right;
            x[3] =  newS.left;

            Arrays.sort(x); //6 compareTo
            if (s.aligned(x)) {
                //a Segment can be part of any one segement
                s.merge(x); 
                return;
            }
        }
        result.add(newS);
    }


    public FastCollinearPoints(Point[] ps) {
        int N = ps.length;

        if (ps == null ) {
            throw new NullPointerException();
        }

        Point[] points = new Point[N];
        for (int i = 0; i < N; ++i) {
            if (ps[i] == null ) throw new NullPointerException();
            points[i] = ps[i];
        }

        result = new ArrayList<Segments>();
        if ( N < T ) return;

        //Point o = new Point(0,0);
        //Arrays.sort(points , o.slopeOrder()); 

        //sort by y axis
        Arrays.sort(points);

        for (int i = 1; i < points.length; ++i) {
            if (points[i].compareTo(points[i-1]) == 0) 
                throw new IllegalArgumentException();
        }

        for (int i = N - 1; i >= 0; --i) {

            Point ref = points[i];

            Point[] others = new Point[N-1];

            int k = 0; 

            for (int j = 0; j < points.length ; ++j) {
                if (j == i) continue;
                others[k++] = points[j];
            }
            assert( k == N - 1);
            Arrays.sort(others , ref.slopeOrder());
            double r = ref.slopeTo(others[0]);

            int c = 1;
            int q = 1;
            for (q = 1; q < others.length; ++q) {
                double newr = ref.slopeTo(others[q]);
                if (Double.compare(r , newr) == 0) {
                    c++; 
                    continue;
                } else {
                    if (c > T - 2) {
                        // create a subarray and sort it with origin in the polar order
                        Point[] col = new Point[c+1];
                        col[c] = ref;
                        int t = q - c;
                        for (int cp = 0; cp < c;) {
                            col[cp++]  =  others[t++];
                        }
                        updateSegments(col);
                    }
                    r = newr;
                    c = 1;
                }
            }
            if (c > T - 2) {
                // create a subarray and sort it with origin in the polar order
                Point[] col = new Point[c+1];
                col[c] = ref;
                int t = q - c;
                for (int cp = 0; cp < c;) {
                    col[cp++]  =  others[t++];
                }

                updateSegments(col);
            }
        }

    }

    public int numberOfSegments()  {
        return result.size();
    }
    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[numberOfSegments()];
        int k = 0;
        for (Segments s : result) {
            segments[k++] = new LineSegment(s.left ,   s.right);
        }
        return segments;
    }

}
