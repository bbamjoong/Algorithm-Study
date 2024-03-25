import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

public class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;

    static int n;
    static List<Point> points;
    static Point root;
    static Stack<Point> stack;

    static class Point implements Comparable<Point> {
        long x;
        long y;

        Point(long x, long y) {
            this.x = x;
            this.y = y;
        }


        // 각도에 따라 정렬할 것
        @Override
        public int compareTo(Point p) {
            long result = ccw(root, this, p);
            if (result > 0) { // 반시계 -> 오름차순
                return -1;
            } else if (result < 0) { // 시계방향 -> 내림차순
                return 1;
            } else if (dist(root, this) < dist(root, p)) { // 일직선에 위치할 때 다음 점이 더 멀면 -> 오름차순
                return -1;
            } else if (dist(root, this) > dist(root, p)) {
                return 1;
            }
            return 0;
        }
    }

    static void setInputs() throws IOException {
        n = Integer.parseInt(br.readLine());

        points = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            points.add(new Point(x, y));
        }
    }

    static void grahamScan() {
        findRoot();
        Collections.sort(points);

        stack = new Stack<>();
        stack.add(root);

        for (int i = 1; i < points.size(); i++) {
            // 시계방향이거나, 일자 방향이 나오면 pop
            while (stack.size() > 1
                    && ccw(stack.get(stack.size() - 2), stack.get(stack.size() - 1), points.get(i)) <= 0) {
                stack.pop();
            }
            stack.add(points.get(i));
        }
    }

    // y가 가장 작고, x가 가장 작은 점
    static void findRoot() {
        root = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);

        for (Point point : points) {
            if (point.y < root.y) {
                root = point;
            } else if (point.y == root.y) { // y가 같다면 x가 더 작은 점이 root
                if (point.x < root.x) {
                    root = point;
                }
            }
        }
    }

    static long ccw(Point p1, Point p2, Point p3) {
        return (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x);
    }

    static long dist(Point p1, Point p2) {
        return (long) (Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    // 첫 점은 A. A의 다음 점은 B.  두 번째 점은 C. C의 다음 점은 D라고 정의한다.
    static double rotatingCalipers() {
        int size = stack.size();
        double dist = 0;

        int c = 1;
        for (int a = 0; a < size; a++) { // 각 점을 모두 방문할 거야
            int b = (a + 1) % size;

            while (true) {
                int d = (c + 1) % size;

                Point positionVector1 = new Point(stack.get(b).x - stack.get(a).x, stack.get(b).y - stack.get(a).y);
                Point positionVector2 = new Point(stack.get(d).x - stack.get(c).x, stack.get(d).y - stack.get(c).y);

                long ccw = ccw(new Point(0, 0), positionVector1, positionVector2);
                if (ccw > 0) {  // 반시계 방향이면 오른쪽에 있는 점을 다음으로
                    c = d;
                } else {  // 시계 방향이면 왼쪽에 있는 점을 다음으로 -> 왼쪽은 A임.
                    break;
                }
            }

            // 최대 거리 구하기
            dist = Math.max(dist(stack.get(a), stack.get(c)), dist);
        }
        return Math.sqrt(dist);
    }

    public static void main(String[] args) throws IOException {
        setInputs();
        grahamScan();
        System.out.println(rotatingCalipers());
    }
}