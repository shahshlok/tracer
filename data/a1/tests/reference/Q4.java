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

        // Calculate side lengths using distance formula
        double side1 = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        double side2 = Math.sqrt(Math.pow(x3 - x2, 2) + Math.pow(y3 - y2, 2));
        double side3 = Math.sqrt(Math.pow(x1 - x3, 2) + Math.pow(y1 - y3, 2));

        // Heron's formula
        double s = (side1 + side2 + side3) / 2;
        double area = Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));

        System.out.println("The area of the triangle is " + area);
        scanner.close();
    }
}
