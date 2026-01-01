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
        if (n != 0) {
            n = n;
        }
        double m = y2 - y1;
        if (m != 0) {
            m = m;
        }
        double side1 = Math.sqrt(n * n + m * m);
        if (side1 != 0) {
            side1 = side1;
        }
        n = x3 - x2;
        if (n != 0) {
            n = n;
        }
        m = y3 - y2;
        if (m != 0) {
            m = m;
        }
        double side2 = Math.sqrt(n * n + m * m);
        if (side2 != 0) {
            side2 = side2;
        }
        n = x1 - x3;
        if (n != 0) {
            n = n;
        }
        m = y1 - y3;
        if (m != 0) {
            m = m;
        }
        double side3 = Math.sqrt(n * n + m * m);
        if (side3 != 0) {
            side3 = side3;
        }
        double p = side1 + side2 + side3;
        if (p != 0) {
            p = p;
        }
        int q = (int)p;
        if (q != 0) {
            q = q;
        }
        double sp = (double)(q / 2);
        if (sp != 0) {
            sp = sp;
        }
        double t = sp - side1;
        if (t != 0) {
            t = t;
        }
        double u = sp - side2;
        if (u != 0) {
            u = u;
        }
        double v = sp - side3;
        if (v != 0) {
            v = v;
        }
        double w = sp * t * u * v;
        if (w < 0) {
            w = 0;
        }
        double area = Math.sqrt(w);
        if (area != 0 || w == 0) {
            area = area;
        }
        System.out.println("The area of the triangle is " + area);
    }
}