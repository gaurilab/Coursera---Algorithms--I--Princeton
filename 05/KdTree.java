import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private static class Node {
        private Point2D p;      // the point
        private RectHV container;   
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private boolean dir;

        public Node(Point2D q, boolean d,
                double xmin, double ymin, double xmax, double ymax) {
            p = new Point2D(q.x(), q.y());
            dir = d;
            container = new RectHV(xmin ,
                                   ymin ,
                                   xmax ,
                                   ymax);   
            lb = null;
            rt = null;
        }
    }
    
    private int size;
    private Node root;
    
    public         KdTree() {
        root = null;
        size  = 0;
    }                              // construct an empty set of points 
    
    public           boolean isEmpty() {
        return (root == null);
    }                      // is the set empty? 
    public               int size() {
        return size;
    }                        // number of points in the set 
    
    private Node add(Node n, Point2D q, boolean dir,
            double xmin, double ymin, double xmax, double ymax) {
        if (n == null) return new Node(q , dir, xmin , ymin , xmax , ymax);
        
        if (n.dir) { //vertical
            int result =  Double.compare(q.x() , n.p.x());
            if (result <= 0) { //left 

                n.lb = add(n.lb , q, !dir ,
                           n.container.xmin() ,
                                      n.container.ymin() ,
                                      n.p.x() ,
                                      n.container.ymax());
            } else { //right

                n.rt = add(n.rt , q , !dir ,
                           n.p.x() ,
                                      n.container.ymin() ,
                                      n.container.xmax() ,
                                      n.container.ymax());
            }
        } else { // horizontal
            int result =  Double.compare(q.y() , n.p.y());
            if (result <= 0) { //bottom

                n.lb = add(n.lb , q, !dir ,
                           n.container.xmin() ,
                                      n.container.ymin() ,
                                      n.container.xmax() ,
                                      n.p.y());
            } else { //top

                n.rt = add(n.rt , q, !dir ,
                           n.container.xmin() ,
                                      n.p.y() ,
                                      n.container.xmax() ,
                                      n.container.ymax());
            }
            
        }
        return n;
    }

    
    
    public void insert(Point2D p) {
        if (p == null) return;
        if (contains(p)) return;
        root = add(root , p , true , 0 , 0 , 1 , 1);
        size++;
 
        
    }     
    
    private Node containHelper(Node s , Point2D q) {
        if (s.p.equals(q)) {
            return s;
        }

        Node ans = null;
        if (s.dir) { //vertical
            int result = Double.compare(q.x() , s.p.x());
            if (result <= 0) { //left 
                if (s.lb != null) {
                    ans = containHelper(s.lb , q);
                }   
            } else {
                if (s.rt != null) {
                    ans = containHelper(s.rt , q);
                }
            }
        } else { //horizontal
            int result = Double.compare(q.y() , s.p.y());
            if (result <= 0) { //bottom
                if (s.lb != null) {
                    ans = containHelper(s.lb , q);
                }
            } else {
                if (s.rt != null) {
                    ans = containHelper(s.rt , q);
                }
            }
        }
        
        
        return ans;
    }
    
    public boolean contains(Point2D q) {
        if (root == null) return false;
        Node a = containHelper(root , q);
        return (a != null);
    } 

    private void drawNode(Node n) {
        if (n == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        StdDraw.point(n.p.x() , n.p.y());
        StdDraw.setPenColor(StdDraw.GRAY);
//        n.container.draw();
        if (n.dir) { //vertical
            StdDraw.setPenRadius(.000001);
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.p.x() , n.container.ymin() ,
                         n.p.x() , n.container.ymax());
            
        } else { //horizontal
            StdDraw.setPenRadius(.000001);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.container.xmin() , n.p.y() ,
                         n.container.xmax() , n.p.y());
        }
        drawNode(n.lb);
        drawNode(n.rt);
        StdDraw.setPenRadius(.01);
    }
    public void draw() {

        drawNode(root);
        
        
    }       
    // draw all points to standard draw 
    private  void rangeHelper(Node n, RectHV rect , Queue<Point2D> list) {
        if (n == null) return;

        if (n.container.intersects(rect)) {
            if (rect.contains(n.p)) {
                list.enqueue(n.p);
            }
            rangeHelper(n.lb , rect , list);
            rangeHelper(n.rt , rect , list);
        }
        
    }
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> list = new   Queue<Point2D>();
        if (root == null) return list;

        rangeHelper(root, rect , list);
        return list;
    } // all points that are inside the rectangle
    
    private  Point2D nearestHelper(Node n , Point2D q , Point2D cp) {
        
        Point2D champion = null;

        assert (n != null);

        
        double t = n.container.distanceSquaredTo(q);
        double closest = q.distanceSquaredTo(cp);
        int r = Double.compare(t , closest); 
        if (r <= 0) {
            double d = n.p.distanceSquaredTo(q);
            if (d < closest) {
                cp = n.p;

            }

            if (n.lb != null) cp = nearestHelper(n.lb , q , cp);
            if (n.rt != null) cp = nearestHelper(n.rt , q , cp);

        }


        return cp;
        
        
    }
    public Point2D nearest(Point2D q)   {


        if (isEmpty()) return null;

        Point2D champion =  new Point2D(root.p.x() , root.p.y());
        if (root.lb != null) champion = nearestHelper(root.lb , q , champion);
        if (root.rt != null) champion = nearestHelper(root.rt , q , champion);

        return champion;
    }          // a nearest neighbor in the set to point p; null if the set is empty 

}
