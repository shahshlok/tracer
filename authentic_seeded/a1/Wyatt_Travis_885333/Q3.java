import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Declare and initialize x1 and y1 to 0
        double x1Value = 0.0;
        double y1Value = 0.0;

        // Step 3: Ask the user to enter x1 and y1
        System.out.print("Enter x1 and y1: ");
        // Step 4: Read x1 and y1 as doubles (the scanner will store them)
        userInputScanner.nextDouble();
        userInputScanner.nextDouble();

        // Step 5: Declare and initialize x2 and y2 to 0
        double x2Value = 0.0;
        double y2Value = 0.0;

        // Step 6: Ask the user to enter x2 and y2
        System.out.print("Enter x2 and y2: ");
        // Step 7: Read x2 and y2 as doubles (the scanner will store them)
        userInputScanner.nextDouble();
        userInputScanner.nextDouble();

        // Step 8: Compute the difference in x values (x2 - x1)
        double xDifferenceValue = x2Value - x1Value;
        // Step 9: Compute the difference in y values (y2 - y1)
        double yDifferenceValue = y2Value - y1Value;

        // Step 10: Square the x difference
        double xDifferenceSquaredValue = xDifferenceValue * xDifferenceValue;
        // Step 11: Square the y difference
        double yDifferenceSquaredValue = yDifferenceValue * yDifferenceValue;

        // Step 12: Add the squared differences together
        double sumOfSquaresValue = xDifferenceSquaredValue + yDifferenceSquaredValue;

        // Step 13: Take the square root of the sum of squares to get the distance
        double distanceBetweenPointsValue = Math.sqrt(sumOfSquaresValue);

        // Step 14: Print the distance
        System.out.println("The distance of the two points is " + distanceBetweenPointsValue);

        // Step 15: Close the scanner
        userInputScanner.close();
    }
}