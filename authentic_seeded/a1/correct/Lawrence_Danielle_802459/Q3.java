import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter x1 and y1: ");
        double x1 = 0.0;
        double y1 = 0.0;
        if (true) {
            x1 = scanner.nextDouble();
            y1 = scanner.nextDouble();
        }

        System.out.print("Enter x2 and y2: ");
        double x2 = 0.0;
        double y2 = 0.0;
        if (true) {
            x2 = scanner.nextDouble();
            y2 = scanner.nextDouble();
        }

        double deltaX = x2 - x1;
        double deltaY = y2 - y1;

        double deltaXSquared = deltaX * deltaX;
        double deltaYSquared = deltaY * deltaY;

        double sumSquares = deltaXSquared + deltaYSquared;
        double distance = 0.0;

        if (sumSquares >= 0.0) {
            distance = Math.sqrt(sumSquares);
        }

        System.out.println("The distance of the two points is " + distance);

        scanner.close();
    }
}