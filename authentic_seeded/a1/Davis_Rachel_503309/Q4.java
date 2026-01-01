import java.util.Scanner;
public class Q4 {
    public static void main(String[] args) {
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
        double a = 0;
        double b = 0;
        double c = 0;
        double n = x2 - x1;
        if (n != 0) {
            a = n;
        } else {
            a = 0;
        }
        double m = y2 - y1;
        if (m != 0) {
            b = m;
        } else {
            b = 0;
        }
        double d = Math.sqrt(a * a + b * b);
        double e = 0;
        double f = 0;
        n = x3 - x2;
        if (n != 0) {
            e = n;
        } else {
            e = 0;
        }
        m = y3 - y2;
        if (m != 0) {
            f = m;
        } else {
            f = 0;
        }
        double g = Math.sqrt(e * e + f * f);
        double h = 0;
        double i = 0;
        n = x1 - x3;
        if (n != 0) {
            h = n;
        } else {
            h = 0;
        }
        m = y1 - y3;
        if (m != 0) {
            i = m;
        } else {
            i = 0;
        }
        double j = Math.sqrt(h * h + i * i);
        double k = d + g + j;
        double l = k / 2.0;
        double p = 0;
        if (l > 0) {
            p = l;
        }
        double q = p - d;
        double r = p - g;
        double t = p - j;
        double u = p * q * r * t;
        double v = 0;
        if (u >= 0) {
            v = Math.sqrt(u);
        } else {
            v = 0;
        }
        System.out.println("The area of the triangle is " + v);
    }
}