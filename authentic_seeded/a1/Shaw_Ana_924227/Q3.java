import java.util.*;
public class Q3 {
    public static void main(String[] a) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter x1 and y1: ");
        double x1 = 0;
        double y1 = 0;
        if (x.hasNextDouble()) x1 = x.nextDouble();
        if (x.hasNextDouble()) y1 = x.nextDouble();
        System.out.print("Enter x2 and y2: ");
        double x2 = 0;
        double y2 = 0;
        if (x.hasNextDouble()) x2 = x.nextDouble();
        if (x.hasNextDouble()) y2 = x.nextDouble();
        double n = x2 - x1;
        double y = y2 - y1;
        double d = n * n;
        double e = y * y;
        double f = d + e;
        double g = 0;
        if (f >= 0) g = Math.sqrt(f);
        System.out.println("The distance of the two points is " + g);
    }
}