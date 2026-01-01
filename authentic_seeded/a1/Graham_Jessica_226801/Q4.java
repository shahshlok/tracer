import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter three points for a triangle.");
        System.out.print("(x1, y1):");
        double x1 = input.nextDouble();
        double y1 = input.nextDouble();

        System.out.print("(x2, y2):");
        double x2 = input.nextDouble();
        double y2 = input.nextDouble();

        System.out.print("(x3, y3):");
        double x3 = input.nextDouble();
        double y3 = input.nextDouble();

        double a1 = x2 - x1;
        double b1 = y2 - y1;
        double side1 = Math.sqrt(a1 * a1 + b1 * b1);

        double a2 = x3 - x2;
        double b2 = y3 - y2;
        double side2 = Math.sqrt(a2 * a2 + b2 * b2);

        double a3 = x1 - x3;
        double b3 = y1 - y3;
        double side3 = Math.sqrt(a3 * a3 + b3 * b3);

        double sNumerator = side1 + side2 + side3;
        double s = sNumerator / 2.0;

        double t1 = s - side1;
        double t2 = s - side2;
        double t3 = s - side3;

        double areaInside = s * t1 * t2 * t3;
        double area = Math.sqrt(areaInside);

        System.out.println("The area of the triangle is " + area);

        input.close();
    }
}