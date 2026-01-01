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
        double m = y2 - y1;
        double side1 = Math.sqrt(n * n + m * m);
        n = x3 - x2;
        m = y3 - y2;
        double side2 = Math.sqrt(n * n + m * m);
        n = x3 - x1;
        m = y3 - y1;
        double side3 = Math.sqrt(n * n + m * m);
        double p = side1 + side2;
        double q = p + side3;
        double r = q / 2.0;
        double t = r - side1;
        double u = r - side2;
        double v = r - side3;
        double w = r * t;
        if (w == 0) {
            System.out.println("The area of the triangle is 0.0");
        } else {
            double z = w * u;
            if (z == 0) {
                System.out.println("The area of the triangle is 0.0");
            } else {
                double aa = z * v;
                double area = 0;
                if (aa >= 0) {
                    area = Math.sqrt(aa);
                }
                System.out.println("The area of the triangle is " + area);
            }
        }
    }
}