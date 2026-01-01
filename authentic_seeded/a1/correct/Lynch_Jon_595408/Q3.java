import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read values from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the first point coordinates x1 and y1
        System.out.print("Enter x1 and y1: ");
        double firstPointXCoordinate = userInputScanner.nextDouble();
        double firstPointYCoordinate = userInputScanner.nextDouble();

        // Ask the user to enter the second point coordinates x2 and y2
        System.out.print("Enter x2 and y2: ");
        double secondPointXCoordinate = userInputScanner.nextDouble();
        double secondPointYCoordinate = userInputScanner.nextDouble();

        // Compute the differences in x and y coordinates
        double a = secondPointXCoordinate - firstPointXCoordinate; // (x2 - x1)
        double b = secondPointYCoordinate - firstPointYCoordinate; // (y2 - y1)

        // Compute squares of the differences
        double c = a * a; // (x2 - x1)^2
        double d = b * b; // (y2 - y1)^2

        // Sum the squares
        double e = c + d; // (x2 - x1)^2 + (y2 - y1)^2

        // Take the square root to get the distance
        double distanceBetweenTwoPoints = Math.sqrt(e);

        // Display the result to the user
        System.out.println("The distance of the two points is " + distanceBetweenTwoPoints);

        // Close the scanner
        userInputScanner.close();
    }
}