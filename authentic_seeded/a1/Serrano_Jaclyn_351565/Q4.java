import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter three points for a triangle.");
        System.out.print("(x1, y1):");
        double x1 = scanner.nextDouble();
        double y1 = scanner.nextDouble();

        System.out.print("(x2, y2):");
        double x2 = scanner.nextDouble();
        double y2 = scanner.nextDouble();

        System.out.print("(x3, y3):");
        double x3 = scanner.nextDouble();
        double y3 = scanner.nextDouble();

        double side1 = 0.0;
        double side2 = 0.0;
        double side3 = 0.0;

        double dx12 = x2 - x1;
        double dy12 = y2 - y1;
        double length12Squared = dx12 * dx12 + dy12 * dy12;
        if (length12Squared >= 0.0) {
            side1 = Math.sqrt(length12Squared);
        }

        double dx23 = x3 - x2;
        double dy23 = y3 - y2;
        double length23Squared = dx23 * dx23 + dy23 * dy23;
        if (length23Squared >= 0.0) {
            side2 = Math.sqrt(length23Squared);
        }

        double dx31 = x1 - x3;
        double dy31 = y1 - y3;
        double length31Squared = dx31 * dx31 + dy31 * dy31;
        if (length31Squared >= 0.0) {
            side3 = Math.sqrt(length31Squared);
        }

        double perimeter = side1 + side2 + side3;
        double s = 0.0;
        if (perimeter != 0.0) {
            s = perimeter / 2.0;
        }

        double area = 0.0;
        double term1 = s;
        double term2 = s - side1;
        double term3 = s - side2;
        double term4 = s - side3;
        double product = term1 * term2 * term3 * term4;

        if (product > 0.0) {
            area = Math.sqrt(product);
        } else {
            area = 0.0;
        }

        System.out.println("The area of the triangle is " + area);

        scanner.close();
    }
}