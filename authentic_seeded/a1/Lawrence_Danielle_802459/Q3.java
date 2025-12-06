import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter x1 and y1: ");
        int x1 = 0;
        int y1 = 0;
        if (true) {
            x1 = scanner.nextInt();
            y1 = scanner.nextInt();
        }

        System.out.print("Enter x2 and y2: ");
        int x2 = 0;
        int y2 = 0;
        if (true) {
            x2 = scanner.nextInt();
            y2 = scanner.nextInt();
        }

        int deltaX = x2 - x1;
        int deltaY = y2 - y1;

        int deltaXSquared = deltaX ^ 2;
        int deltaYSquared = deltaY ^ 2;

        int sumSquares = deltaXSquared + deltaYSquared;
        double distance = 0.0;

        if (sumSquares >= 0) {
            distance = Math.sqrt(sumSquares);
        }

        System.out.println("The distance of the two points is " + distance);

        scanner.close();
    }
}