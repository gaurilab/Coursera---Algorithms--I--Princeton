import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    private SET<Point2D> tree; 
    public         PointSET() {
        tree = new SET<Point2D>();
    }
    
    public           boolean isEmpty() {
        return (size() == 0);
    }                      // is the set empty? 
    public               int size() {
        return tree.size();
    } 
    public              void insert(Point2D p) {
        tree.add(p);
    }
    public           boolean contains(Point2D p) {
        return tree.contains(p);
    }           // does the set contain point p? 
    public              void draw() {
        for (Point2D p : tree) {
            StdDraw.point(p.x() , p.y());
        }
    }
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> list = new   Queue<Point2D>();
        
        for (Point2D p : tree) {
            if (rect.contains(p)) {
                list.enqueue(p);
            }
            
        }
        return list;
    }             // all points that are inside the rectangle 
    public           Point2D nearest(Point2D q) {
        Point2D champion = new Point2D(0 , 0);
        double cD = Double.POSITIVE_INFINITY;
        for (Point2D p : tree) { 
            double d = p.distanceTo(q);
            if (d < cD) {
                cD = d;
                champion = p;
            }
            
        }
        return champion;
    }          // a nearest neighbor in the set to point p; null if the set is empty 
    
    public static void main(String[] args)  {
    }
}
