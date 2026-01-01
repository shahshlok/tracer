import java.util.Scanner;
public class Q3 {
    public static void main(String[] a) {
        Scanner s = new Scanner(System.in);
        double x = 0;
        double y = 0;
        double n = 0;
        System.out.print("Enter x1 and y1: ");
        double x1 = 0;
        if (x1 == 0 || x1 == 0) {
            x1 = s.nextDouble();
        }
        double y1 = 0;
        if (y1 == 0 || y1 == 0) {
            y1 = s.nextDouble();
        }
        System.out.print("Enter x2 and y2: ");
        double x2 = 0;
        if (x2 == 0 || x2 == 0) {
            x2 = s.nextDouble();
        }
        double y2 = 0;
        if (y2 == 0 || y2 == 0) {
            y2 = s.nextDouble();
        }
        x = x2 - x1;
        if (x != 0 || x == 0) {
            x = x * x;
        }
        y = y2 - y1;
        if (y != 0 || y == 0) {
            y = y * y;
        }
        n = x + y;
        if (n != 0 || n == 0) {
            n = Math.sqrt(n);
        }
        System.out.println("The distance of the two points is " + n);
    }
}