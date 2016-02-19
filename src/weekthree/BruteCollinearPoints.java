package weekthree;

import java.util.ArrayList;
import java.util.Arrays;

/*Throw a java.lang.NullPointerException either the argument 
 * to the constructor is null or if any point in the array is null. 
 * Throw a java.lang.IllegalArgumentException if the argument to the 
 * constructor contains a repeated point.*/
public class BruteCollinearPoints {

    private ArrayList<LineSegment> segments;
    private Point[] mPoints;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new NullPointerException();
        }
        for (Point p : points) {
            if (p == null) {
                throw new NullPointerException();
            }
        }

        mPoints = Arrays.copyOf(points, points.length);
        Arrays.sort(mPoints);
        if (hasRepeatedPoint(mPoints)) {
            throw new IllegalArgumentException();
        }

        segments = new ArrayList<>();

        bruteAlgo(mPoints);
    }

    private void bruteAlgo(Point[] points) {
        int len = points.length;
        for (int a = 0; a < len; a++)
            for (int b = a + 1; b < len; b++)
                for (int c = b + 1; c < len; c++)
                    for (int d = c + 1; d < len; d++) {
                        double ab = points[a].slopeTo(points[b]);
                        double ac = points[a].slopeTo(points[c]);
                        double ad = points[a].slopeTo(points[d]);
                        if (ab == ac && ab == ad) {
                            segments.add(new LineSegment(points[a], points[d]));
                            break;
                        }
                    }

    }

    private boolean hasRepeatedPoint(Point[] points) {
        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(points[i - 1]) == 0) {
                return true;
            }
        }
        return false;
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }
}
