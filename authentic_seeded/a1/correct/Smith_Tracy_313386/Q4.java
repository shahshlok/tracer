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
        double n = x2 - x1;
        double m = y2 - y1;
        double side1 = Math.sqrt(n * n + m * m);
        n = x3 - x2;
        m = y3 - y2;
        double side2 = Math.sqrt(n * n + m * m);
        n = x3 - x1;
        m = y3 - y1;
        double side3 = Math.sqrt(n * n + m * m);
        double p = side1 + side2 + side3;
        double q = 0;
        if (p != 0) q = p / 2.0;
        double a = q - side1;
        double b = q - side2;
        double c = q - side3;
        double r = q * a * b * c;
        double area = 0;
        if (r > 0) area = Math.sqrt(r);
        else if (r == 0) area = 0;
        System.out.println("The area of the triangle is " + area);
    }
}