import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter three points for a triangle.");
        System.out.print("(x1, y1):");
        int x1 = scanner.nextInt();
        int y1 = scanner.nextInt();

        System.out.print("(x2, y2):");
        int x2 = scanner.nextInt();
        int y2 = scanner.nextInt();

        System.out.print("(x3, y3):");
        int x3 = scanner.nextInt();
        int y3 = scanner.nextInt();

        double side1 = distance(x1, y1, x2, y2);
        double side2 = distance(x2, y2, x3, y3);
        double side3 = distance(x3, y3, x1, y1);

        double s = (side1 + side2 + side3) / 2.0;
        double area = Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));

        System.out.println("The area of the triangle is " + area);

        scanner.close();
    }

    public static double distance(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }
}