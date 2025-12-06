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

        double perimeterHalf = (side1 + side2 + side3) / 2.0;
        double s = perimeterHalf;

        double temp1 = s - side1;
        double temp2 = s - side2;
        double temp3 = s - side3;

        double areaPart = s * temp1 * temp2 * temp3;
        double area;
        if (areaPart > 0) {
            area = Math.sqrt(areaPart);
        } else if (areaPart == 0) {
            area = 0.0;
        } else {
            area = 0.0;
        }

        System.out.println("The area of the triangle is " + area);
    }

    public static double distanceBetweenPoints(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;

        double dxSquared = dx * dx;
        double dySquared = dy * dy;

        double sumSquares = dxSquared + dySquared;

        double distance;
        if (sumSquares >= 0) {
            distance = Math.sqrt(sumSquares);
        } else {
            distance = 0.0;
        }

        return distance;
    }
}