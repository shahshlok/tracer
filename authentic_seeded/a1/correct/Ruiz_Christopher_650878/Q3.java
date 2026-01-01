import java.util.Scanner;
public class Q3 {
    public static void main(String[] a) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter x1 and y1: ");
        double x = s.nextDouble();
        double y = s.nextDouble();
        double n = 0.0;
        if (x == x || y == y) {
            double x2 = s.nextDouble();
            double y2 = s.nextDouble();
            double dx = x2 - x;
            double dy = y2 - y;
            double t = dx * dx;
            double u = dy * dy;
            double v = t + u;
            if (v >= 0.0) n = Math.sqrt(v);
        }
        System.out.println("The distance of the two points is " + n);
    }
}