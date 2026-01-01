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

        double side1 = distanceBetweenPoints(x1, y1, x2, y2);
        double side2 = distanceBetweenPoints(x2, y2, x3, y3);
        double side3 = distanceBetweenPoints(x3, y3, x1, y1);

        double sumSides = side1 + side2 + side3;
        double s = sumSides / 2.0;
        double area = 0.0;

        if (s > 0.0) {
            double term1 = s - side1;
            double term2 = s - side2;
            double term3 = s - side3;

            if (term1 >= 0.0 && term2 >= 0.0 && term3 >= 0.0) {
                double product = s * term1 * term2 * term3;
                if (product >= 0.0) {
                    area = Math.sqrt(product);
                }
            }
        }

        System.out.println("The area of the triangle is " + area);

        scanner.close();
    }

    public static double distanceBetweenPoints(double x1, double y1, double x2, double y2) {
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