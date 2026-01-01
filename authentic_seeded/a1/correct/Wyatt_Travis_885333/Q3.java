import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter x1 and y1
        System.out.print("Enter x1 and y1: ");
        // Step 3: Read x1 and y1 as doubles
        double x1Value = userInputScanner.nextDouble();
        double y1Value = userInputScanner.nextDouble();

        // Step 4: Ask the user to enter x2 and y2
        System.out.print("Enter x2 and y2: ");
        // Step 5: Read x2 and y2 as doubles
        double x2Value = userInputScanner.nextDouble();
        double y2Value = userInputScanner.nextDouble();

        // Step 6: Compute the difference in x values (x2 - x1)
        double xDifferenceValue = x2Value - x1Value;
        // Step 7: Compute the difference in y values (y2 - y1)
        double yDifferenceValue = y2Value - y1Value;

        // Step 8: Square the x difference
        double xDifferenceSquaredValue = xDifferenceValue * xDifferenceValue;
        // Step 9: Square the y difference
        double yDifferenceSquaredValue = yDifferenceValue * yDifferenceValue;

        // Step 10: Add the squared differences together
        double sumOfSquaresValue = xDifferenceSquaredValue + yDifferenceSquaredValue;

        // Step 11: Take the square root of the sum of squares to get the distance
        double distanceBetweenPointsValue = Math.sqrt(sumOfSquaresValue);

        // Step 12: Print the distance
        System.out.println("The distance of the two points is " + distanceBetweenPointsValue);

        // Step 13: Close the scanner
        userInputScanner.close();
    }
}