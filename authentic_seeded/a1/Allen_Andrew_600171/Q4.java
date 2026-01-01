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

        double side1 = calculateDistance(x1, y1, x2, y2);
        double side2 = calculateDistance(x2, y2, x3, y3);
        double side3 = calculateDistance(x3, y3, x1, y1);

        double perimeter = side1 + side2 + side3;
        double s = perimeter / 2.0;

        double term1 = s;
        double term2 = s - side1;
        double term3 = s - side2;
        double term4 = s - side3;

        double product = term1 * term2 * term3 * term4;

        double area = 0.0;
        if (product > 0.0) {
            area = Math.sqrt(product);
        } else {
            area = 0.0;
        }

        System.out.println("The area of the triangle is " + area);

        scanner.close();
    }

    public static double calculateDistance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;

        double dxSquared = dx * dx;
        double dySquared = dy * dy;

        double sum = dxSquared + dySquared;
        double distance = 0.0;

        if (sum >= 0.0) {
            distance = Math.sqrt(sum);
        }

        return distance;
    }
}