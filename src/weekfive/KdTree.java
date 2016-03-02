package weekfive;
import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int HOR = 0;
    private static final int VER = 1;
    private Node root;
    private int N;
    private double minDis;
    private Point2D nearestPoint;

    // construct an empty set of points
    public KdTree() {
        root = null;
        N = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // number of points in the set
    public int size() {
        return N;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }

        if (contains(p)) {
            return;
        }

        root = put(root, p, 0, null, LEFT);
        N++;
    }

    private Node put(Node x, Point2D p, int h, Node parent, int lr) {
        if (x == null) {
            if (parent == null) {
                return new Node(p, new RectHV(0, 0, 1, 1));
            } else {
                if (isEven(h)) {
                    if (lr == LEFT) {
                        return new Node(p,
                                new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.p.y()));
                    } else {
                        return new Node(p,
                                new RectHV(parent.rect.xmin(), parent.p.y(), parent.rect.xmax(), parent.rect.ymax()));
                    }
                } else {
                    if (lr == LEFT) {
                        return new Node(p,
                                new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.p.x(), parent.rect.ymax()));
                    } else {
                        return new Node(p,
                                new RectHV(parent.p.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax()));
                    }
                }
            }
        }

        if (isEven(h)) {
            if (p.x() < x.p.x()) {
                x.lb = put(x.lb, p, h + 1, x, LEFT);
            } else {
                x.rt = put(x.rt, p, h + 1, x, RIGHT);
            }
        } else {
            if (p.y() < x.p.y()) {
                x.lb = put(x.lb, p, h + 1, x, LEFT);
            } else {
                x.rt = put(x.rt, p, h + 1, x, RIGHT);
            }
        }
        return x;
    }

    private boolean isEven(int n) {
        return n % 2 == 0;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
        return get(root, p, VER);
    }

    private boolean get(Node x, Point2D p, int ori) {
        if (x == null) {
            return false;
        }
        if (p.equals(x.p)) {
            return true;
        }
        if (ori == VER) {
            if (p.x() < x.p.x()) {
                return get(x.lb, p, changeOri(ori));
            } else {
                return get(x.rt, p, changeOri(ori));
            }
        } else {
            if (p.y() < x.p.y()) {
                return get(x.lb, p, changeOri(ori));
            } else {
                return get(x.rt, p, changeOri(ori));
            }
        }
    }

    // draw all points to standard draw
    public void draw() {
        // draw points
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        drawPoint(root);

        // draw lines
        StdDraw.setPenRadius();
        drawLine(root, VER);

    }

    private void drawPoint(Node x) {
        if(x == null){
            return ;
        }
        drawPoint(x.lb);
        x.p.draw();
        drawPoint(x.rt);
    }

    private void drawLine(Node x, int ori) {
        if (x == null) {
            return;
        }
        drawLine(x.lb, changeOri(ori));
        if (ori == VER) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }
        drawLine(x.rt, changeOri(ori));
    }

    private int changeOri(int ori) {
        if (ori == VER)
            return HOR;
        else
            return VER;
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException();
        }
        ArrayList<Point2D> result = new ArrayList<>();
        addPointInRange(root, rect, result);
        return result;
    }

    private void addPointInRange(Node x, RectHV rect, ArrayList<Point2D> result) {
        if (x == null || !rect.intersects(x.rect)) {
            return;
        }
        if (rect.contains(x.p)) {
            result.add(x.p);
        }
        addPointInRange(x.lb, rect, result);
        addPointInRange(x.rt, rect, result);
    }

    // a nearest neighbor in the set to point p; null if the set is empty

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
        minDis = Double.POSITIVE_INFINITY;
        nearestPoint = null;
        findNearest(root, p, VER);
        return nearestPoint;
    }

    private void findNearest(Node x, Point2D p, int ori) {
        if (x == null) {
            return;
        }

        // if the closest point discovered so far is closer
        // than the distance between the query point and the rectangle
        // corresponding
        // to a node, there is no need to explore that node (or its subtrees).
        // That
        // is, a node is searched only if it might contain a point that is
        // closer
        // than the best one found so far.
        if (minDis < x.rect.distanceTo(p)) {
            return;
        }

        double dis = p.distanceTo(x.p);
        if (dis < minDis) {
            minDis = dis;
            nearestPoint = x.p;
        }

        if (ori == VER) {
            if (p.x() < x.p.x()) {
                findNearest(x.lb, p, changeOri(ori));
                findNearest(x.rt, p, changeOri(ori));
            } else {
                findNearest(x.rt, p, changeOri(ori));
                findNearest(x.lb, p, changeOri(ori));
            }
        } else {
            if (p.y() < x.p.y()) {
                findNearest(x.lb, p, changeOri(ori));
                findNearest(x.rt, p, changeOri(ori));
            } else {
                findNearest(x.rt, p, changeOri(ori));
                findNearest(x.lb, p, changeOri(ori));
            }
        }

    }
    // Nearest neighbor search. To find a closest point to a given query point,
    // start at the root and recursively search in both subtrees using the
    // following pruning rule: if the closest point discovered so far is closer
    // than the distance between the query point and the rectangle corresponding
    // to a node, there is no need to explore that node (or its subtrees). That
    // is, a node is searched only if it might contain a point that is closer
    // than the best one found so far. The effectiveness of the pruning rule
    // depends on quickly finding a nearby point. To do this, organize your
    // recursive method so that when there are two possible subtrees to go down,
    // you always choose the subtree that is on the same side of the splitting
    // line as the query point as the first subtree to explore—the closest point
    // found while exploring the first subtree may enable pruning of the second
    // subtree.

    private static class Node {
        private Point2D p; // the point
        private RectHV rect; // the axis-aligned rectangle corresponding to this
                             // node
        private Node lb; // the left/bottom subtree
        private Node rt; // the right/top subtree

        Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }

}