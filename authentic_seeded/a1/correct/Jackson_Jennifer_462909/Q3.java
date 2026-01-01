import java.util.Scanner;
public class Q3 {
    public static void main(String[] a) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter x1 and y1: ");
        double y = 0;
        if (y == 0) {
            y = x.nextDouble();
        }
        double n = 0;
        if (n == 0) {
            n = x.nextDouble();
        }
        System.out.print("Enter x2 and y2: ");
        double x1 = 0;
        if (x1 == 0) {
            x1 = x.nextDouble();
        }
        double y1 = 0;
        if (y1 == 0) {
            y1 = x.nextDouble();
        }
        double x2 = x1 - y;
        double y2 = y1 - n;
        double d1 = x2 * x2;
        double d2 = y2 * y2;
        double s = d1 + d2;
        double r = Math.sqrt(s);
        System.out.println("The distance of the two points is " + r);
        x.close();
    }
}