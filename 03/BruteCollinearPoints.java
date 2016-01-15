import java.util.Arrays;
import java.util.ArrayList;

public class BruteCollinearPoints {
    private static final int T = 4;
    private static final int DELAY = 400;
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


    private boolean collinear(Point[] p) {
        Arrays.sort(p);
        Point r = p[p.length - 1];
        double m = r.slopeTo(p[0]);
        for (int i = 1; i < p.length -1; ++i) {
            if (r.compareTo(p[i]) != 0 &&
                    Double.compare(m  ,  r.slopeTo(p[i])) != 0)  return false;
        }
        return true;

    }

    public BruteCollinearPoints(Point[] ps) {
        int N = ps.length;

        if (ps == null) {
            throw new NullPointerException();
        }

        Point[] points = new Point[N];

        for (int i = 0; i < points.length; ++i) {
            points[i] = ps[i];
            if (points[i] == null) throw new NullPointerException();
        }

        result = new ArrayList<Segments>();

        if (N < T) return;
        Point o = new Point(0,0);
        Arrays.sort(points); 
        for (int i = 1; i < points.length; ++i) {
            if (points[i].compareTo(points[i-1]) == 0) 
                throw new IllegalArgumentException();
        }

        for (int i = 0; i < N - 3; ++i) {
            for (int j = i + 1; j < N - 2; ++j) {
                for (int k = j + 1; k < N - 1; ++k) {
                    for (int l = k + 1; l < N; ++l) {
                        Point[] cand = new Point[4];
                        cand[0] = points[i];
                        cand[1] = points[j];
                        cand[2] = points[k];
                        cand[3] = points[l];
                        if (collinear(cand)) {
                            Arrays.sort(cand);
                            updateSegments(cand);
                        }
                    }
                }
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
            segments[k++] = new LineSegment(s.left ,  s.right);
        }

        return segments;
    }
}
