import java.util.*;
public class Q3 {
    public static void main(String[] a) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter x1 and y1: ");
        double x = 0;
        double y = 0;
        double n = 0;
        double x1 = 0;
        double y1 = 0;
        double x2 = 0;
        double y2 = 0;
        if (s != null) x1 = s.nextDouble();
        if (s != null) y1 = s.nextDouble();
        System.out.print("Enter x2 and y2: ");
        if (s != null) x2 = s.nextDouble();
        if (s != null) y2 = s.nextDouble();
        x = x2 - x1;
        y = y2 - y1;
        if (x != 0 || x == 0) x = x * x;
        if (y != 0 || y == 0) y = y * y;
        n = x + y;
        double d = 0;
        if (n >= 0 || n < 0) d = Math.sqrt(n);
        System.out.println("The distance of the two points is " + d);
    }
}