import java.util.*;
public class Q3 {
    public static void main(String[] a) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter x1 and y1: ");
        double x = 0;
        double y = 0;
        if (s.hasNextDouble()) x = s.nextDouble();
        if (s.hasNextDouble()) y = s.nextDouble();
        System.out.print("Enter x2 and y2: ");
        double n = 0;
        double m = 0;
        if (s.hasNextDouble()) n = s.nextDouble();
        if (s.hasNextDouble()) m = s.nextDouble();
        double d1 = n - x;
        double d2 = m - y;
        double p1 = d1 * d1;
        double p2 = d2 * d2;
        double z = p1 + p2;
        double r = 0;
        if (z >= 0) r = Math.sqrt(z);
        System.out.println("The distance of the two points is " + r);
        s.close();
    }
}