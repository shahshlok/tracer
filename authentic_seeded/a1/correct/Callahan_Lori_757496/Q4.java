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

        double dx1 = x2 - x1;
        double dy1 = y2 - y1;
        double side1Squared = dx1 * dx1 + dy1 * dy1;
        double side1 = Math.sqrt(side1Squared);

        double dx2 = x3 - x2;
        double dy2 = y3 - y2;
        double side2Squared = dx2 * dx2 + dy2 * dy2;
        double side2 = Math.sqrt(side2Squared);

        double dx3 = x1 - x3;
        double dy3 = y1 - y3;
        double side3Squared = dx3 * dx3 + dy3 * dy3;
        double side3 = Math.sqrt(side3Squared);

        double a = side1 + side2 + side3;
        double s = a / 2.0;

        double b = s - side1;
        double c = s - side2;
        double d = s - side3;
        double areaSquared = s * b * c * d;
        double area = Math.sqrt(areaSquared);

        System.out.println("The area of the triangle is " + area);
    }
}