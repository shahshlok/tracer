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

        double side1 = distance(x1, y1, x2, y2);
        double side2 = distance(x2, y2, x3, y3);
        double side3 = distance(x3, y3, x1, y1);

        int tempSum = (int)(side1 + side2 + side3);
        int tempS = tempSum / 2;
        double s = (double)tempS;

        double area = Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));

        System.out.println("The area of the triangle is " + area);

        scanner.close();
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }
}