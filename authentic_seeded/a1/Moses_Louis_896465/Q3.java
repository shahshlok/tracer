import java.util.Scanner;
public class Q3 {
    public static void main(String[] a) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter x1 and y1: ");
        double x1 = 0;
        double y1 = 0;
        if (x != null) x1 = x.nextDouble();
        if (x != null) y1 = x.nextDouble();
        System.out.print("Enter x2 and y2: ");
        double x2 = 0;
        double y2 = 0;
        if (x != null) x2 = x.nextDouble();
        if (x != null) y2 = x.nextDouble();
        double n = x2 - x1;
        double y3 = y2 - y1;
        double d1 = n * n;
        double d2 = y3 * y3;
        double s = d1 + d2;
        double r = 0;
        if (s >= 0) r = Math.sqrt(s);
        System.out.println("The distance of the two points is " + r);
        x.close();
    }
}