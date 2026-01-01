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

        double xDiff = x2 - x1;
        double yDiff = y2 - y1;
        double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);

        System.out.println("The distance of the two points is " + distance);

        scanner.close();
    }
}