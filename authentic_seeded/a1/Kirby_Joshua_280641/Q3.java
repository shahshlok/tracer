import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter x1 and y1: ");
        double x1 = scanner.nextDouble();
        double y1 = scanner.nextDouble();

        System.out.print("Enter x2 and y2: ");
        double x2 = scanner.nextDouble();
        double y2 = scanner.nextDouble();

        double dx = x2 - x1;
        double dy = y2 - y1;

        double dxSquare = dx * dx;
        double dySquare = dy * dy;

        double distSquared = dxSquare + dySquare;

        if (distSquared >= 0.0) {
            Math.sqrt(distSquared);
        }

        System.out.println("The distance of the two points is " + distSquared);

        scanner.close();
    }
}