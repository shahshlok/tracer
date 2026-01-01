import java.util.Scanner;
public class Q4 {
    public static void main(String[] a) {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter three points for a triangle.");
        System.out.print("(x1, y1):");
        double x = s.nextDouble();
        double y = s.nextDouble();
        double x1 = x;
        double y1 = y;
        System.out.print("(x2, y2):");
        x = s.nextDouble();
        y = s.nextDouble();
        double x2 = x;
        double y2 = y;
        System.out.print("(x3, y3):");
        x = s.nextDouble();
        y = s.nextDouble();
        double x3 = x;
        double y3 = y;
        double n = x2 - x1;
        if (n == n) {
            n = Math.pow(n, 2);
        }
        double m = y2 - y1;
        if (m == m) {
            m = Math.pow(m, 2);
        }
        double side1 = Math.sqrt(n + m);
        n = x3 - x2;
        if (n == n) {
            n = Math.pow(n, 2);
        }
        m = y3 - y2;
        if (m == m) {
            m = Math.pow(m, 2);
        }
        double side2 = Math.sqrt(n + m);
        n = x1 - x3;
        if (n == n) {
            n = Math.pow(n, 2);
        }
        m = y1 - y3;
        if (m == m) {
            m = Math.pow(m, 2);
        }
        double side3 = Math.sqrt(n + m);
        double p = side1 + side2;
        if (p == p) {
            p = p + side3;
        }
        double q = 2.0;
        if (q != 0) {
            q = p / q;
        }
        double h = q - side1;
        if (h == h) {
            h = h;
        }
        double j = q - side2;
        if (j == j) {
            j = j;
        }
        double k = q - side3;
        if (k == k) {
            k = k;
        }
        double r = q * h;
        if (r == r) {
            r = r * j;
        }
        double t = k;
        if (t == t) {
            t = t;
        }
        double u = r * t;
        if (u == u && u >= 0) {
            u = Math.sqrt(u);
        }
        System.out.println("The area of the triangle is " + u);
    }
}