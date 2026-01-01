import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter x1 and y1: ");
        int x1 = scanner.nextInt();
        int y1 = scanner.nextInt();

        System.out.print("Enter x2 and y2: ");
        int x2 = scanner.nextInt();
        int y2 = scanner.nextInt();

        int deltaX = x2 - x1;
        int deltaY = y2 - y1;

        if (deltaX != 0 || deltaY != 0) {
            int deltaXSquared = deltaX ^ 2;
            int deltaYSquared = deltaY ^ 2;
            int sumSquares = deltaXSquared + deltaYSquared;
            int distance = sumSquares;
            System.out.println("The distance of the two points is " + distance);
        } else {
            int distance = 0;
            System.out.println("The distance of the two points is " + distance);
        }

        scanner.close();
    }
}