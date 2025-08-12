
import java.util.Scanner;

public class Point {
    private int x,y;
    Point(int x,int y) {
        this.x = x;
        this.y = y;
    }

    static double distance(Point p1,Point p2) {
        int dx = p1.x-p2.x;
        int dy = p1.y-p2.y;
        int anssq = dx*dx + dy*dy;
        double ans = Math.sqrt(anssq);
        return ans;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int x1 = sc.nextInt();
        int y1 = sc.nextInt();
        Point p1 = new Point(x1, y1);

        
        int x2 = sc.nextInt();
        int y2 = sc.nextInt();
        Point p2 = new Point(x2, y2);

        double dist = distance(p1,p2);
        System.out.println("Distance: " + dist);
        sc.close();
    }
}
