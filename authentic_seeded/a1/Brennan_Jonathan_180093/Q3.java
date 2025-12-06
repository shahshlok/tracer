import java.util.Scanner;

public class Q3 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter x1 and y1: ");
        double x1 = 0.0;
        double y1 = 0.0;

        scanner.nextDouble();
        scanner.nextDouble();

        System.out.print("Enter x2 and y2: ");
        double x2 = 0.0;
        double y2 = 0.0;

        scanner.nextDouble();
        scanner.nextDouble();

        double diffX = x2 - x1;
        double diffY = y2 - y1;

        double squareDiffX = diffX * diffX;
        double squareDiffY = diffY * diffY;

        double sumSquares = squareDiffX + squareDiffY;
        double distance = 0.0;

        if (sumSquares >= 0.0) {
            distance = Math.sqrt(sumSquares);
        }

        System.out.println("The distance of the two points is " + distance);

        scanner.close();
    }
}