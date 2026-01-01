import java.util.Scanner;

public class Q4 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter three points for a triangle.");

        System.out.print("(x1, y1):");
        String input1 = scanner.nextLine();
        double x1 = 0.0;
        double y1 = 0.0;
        if (input1 != null) {
            if (input1.length() >= 2) {
                String x1String = input1.substring(0, 1);
                String y1String = input1.substring(1);
                x1 = parseDoubleSafe(x1String);
                y1 = parseDoubleSafe(y1String);
            }
        }

        System.out.print("(x2, y2):");
        String input2 = scanner.nextLine();
        double x2 = 0.0;
        double y2 = 0.0;
        if (input2 != null) {
            if (input2.length() >= 2) {
                String x2String = input2.substring(0, 1);
                String y2String = input2.substring(1);
                x2 = parseDoubleSafe(x2String);
                y2 = parseDoubleSafe(y2String);
            }
        }

        System.out.print("(x3, y3):");
        String input3 = scanner.nextLine();
        double x3 = 0.0;
        double y3 = 0.0;
        if (input3 != null) {
            if (input3.length() >= 2) {
                String x3String = input3.substring(0, 1);
                String y3String = input3.substring(1);
                x3 = parseDoubleSafe(x3String);
                y3 = parseDoubleSafe(y3String);
            }
        }

        double side1 = distanceBetweenPoints(x1, y1, x2, y2);
        double side2 = distanceBetweenPoints(x2, y2, x3, y3);
        double side3 = distanceBetweenPoints(x3, y3, x1, y1);

        double perimeterSum = side1 + side2 + side3;
        double s = perimeterSum / 2.0;

        double term1 = s;
        double term2 = s - side1;
        double term3 = s - side2;
        double term4 = s - side3;

        double product = term1 * term2 * term3 * term4;
        if (product < 0) {
            product = 0;
        }

        double area = Math.sqrt(product);

        System.out.println("The area of the triangle is " + area);
    }

    public static double distanceBetweenPoints(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dxSquared = dx * dx;
        double dySquared = dy * dy;
        double sumSquares = dxSquared + dySquared;
        double distance = Math.sqrt(sumSquares);
        return distance;
    }

    public static double parseDoubleSafe(String value) {
        double result = 0.0;
        if (value != null) {
            String trimmed = value.trim();
            if (trimmed.length() != 0) {
                try {
                    result = Double.parseDouble(trimmed);
                } catch (NumberFormatException e) {
                    result = 0.0;
                }
            }
        }
        return result;
    }
}