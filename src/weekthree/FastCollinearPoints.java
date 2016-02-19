package weekthree;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private LineSegment[] result; // result segments
    private Point[] mPoints;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new NullPointerException();
        }
        for (Point p : points) {
            if (p == null) {
                throw new NullPointerException();
            }
        }

        mPoints = Arrays.copyOf(points, points.length);
        Arrays.sort(mPoints); // sort points

        if (hasRepeatedPoint(mPoints)) {
            throw new IllegalArgumentException();
        }

        fastAlgo(mPoints);
    }

    private void fastAlgo(Point[] points) {
        ArrayList<LineSegment> segments = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            Point[] temp = Arrays.copyOf(points, points.length);
            Arrays.sort(temp, points[i].slopeOrder()); // stable sort for slope
            collectLineSegements(temp, points[i], segments);
        }
        result = segments.toArray(new LineSegment[segments.size()]);
    }

    private void collectLineSegements(Point[] temp, Point p, ArrayList<LineSegment> segments) {
        int count = 1;
        Point start = temp[0];
        Point end = null;
        for (int i = 1; i < temp.length; i++) {
            if (p.slopeTo(temp[i - 1]) == p.slopeTo(temp[i])) {
                count++;
                if (count >= 3) { // fit condition, update end point
                    end = temp[i];
                }
            } else {
                if (count >= 3) { // got a segment has more than 3 point
                    if (p.compareTo(start) < 0) { // tricky
                        segments.add(new LineSegment(p, end));
                    }
                }
                count = 1;
                start = temp[i];
                end = null;
            }
        }

        // don't forget this check!!!
        if (count >= 3) { // got a segment has more than 3 point
            if (p.compareTo(start) < 0) {
                segments.add(new LineSegment(p, end));
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
        return result.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return result.clone();
    }

}
