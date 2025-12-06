import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter three points for a triangle.");
        System.out.print("(x1, y1):");
        String p1 = scanner.nextLine();
        System.out.print("(x2, y2):");
        String p2 = scanner.nextLine();
        System.out.print("(x3, y3):");
        String p3 = scanner.nextLine();

        if (p1 == null) {
            p1 = "";
        }
        if (p2 == null) {
            p2 = "";
        }
        if (p3 == null) {
            p3 = "";
        }

        double x1 = 0.0;
        double y1 = 0.0;
        double x2 = 0.0;
        double y2 = 0.0;
        double x3 = 0.0;
        double y3 = 0.0;

        if (p1.length() >= 2) {
            String sx1 = p1.substring(0, 1);
            String sy1 = p1.substring(1);
            if (sx1.length() > 0) {
                x1 = Double.parseDouble(sx1);
            }
            if (sy1.length() > 0) {
                y1 = Double.parseDouble(sy1);
            }
        }

        if (p2.length() >= 2) {
            String sx2 = p2.substring(0, 1);
            String sy2 = p2.substring(1);
            if (sx2.length() > 0) {
                x2 = Double.parseDouble(sx2);
            }
            if (sy2.length() > 0) {
                y2 = Double.parseDouble(sy2);
            }
        }

        if (p3.length() >= 2) {
            String sx3 = p3.substring(0, 1);
            String sy3 = p3.substring(1);
            if (sx3.length() > 0) {
                x3 = Double.parseDouble(sx3);
            }
            if (sy3.length() > 0) {
                y3 = Double.parseDouble(sy3);
            }
        }

        double side1Double = distance(x1, y1, x2, y2);
        double side2Double = distance(x2, y2, x3, y3);
        double side3Double = distance(x3, y3, x1, y1);

        int side1 = (int) side1Double;
        int side2 = (int) side2Double;
        int side3 = (int) side3Double;

        int perimeterInt = side1 + side2 + side3;
        double s = perimeterInt / 2;

        double temp1 = s - side1;
        double temp2 = s - side2;
        double temp3 = s - side3;

        double product = s * temp1 * temp2 * temp3;
        double area = 0.0;
        if (product >= 0.0) {
            area = Math.sqrt(product);
        }

        double areaRounded = Math.round(area * 10.0) / 10.0;

        System.out.println("The area of the triangle is " + areaRounded);
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dx2 = dx * dx;
        double dy2 = dy * dy;
        double sum = dx2 + dy2;
        double result = Math.sqrt(sum);
        return result;
    }
}