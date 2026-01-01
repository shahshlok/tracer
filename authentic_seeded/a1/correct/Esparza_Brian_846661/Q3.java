import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter x1 and y1
        System.out.print("Enter x1 and y1: ");
        double x1Value = userInputScanner.nextDouble();
        double y1Value = userInputScanner.nextDouble();

        // Step 3: Ask the user to enter x2 and y2
        System.out.print("Enter x2 and y2: ");
        double x2Value = userInputScanner.nextDouble();
        double y2Value = userInputScanner.nextDouble();

        // Step 4: Compute the difference in x coordinates (x2 - x1)
        double xDifferenceValue = x2Value - x1Value;

        // Step 5: Compute the difference in y coordinates (y2 - y1)
        double yDifferenceValue = y2Value - y1Value;

        // Step 6: Square the x difference
        double xDifferenceSquaredValue = xDifferenceValue * xDifferenceValue;

        // Step 7: Square the y difference
        double yDifferenceSquaredValue = yDifferenceValue * yDifferenceValue;

        // Step 8: Add the squared differences together
        double sumOfSquaresValue = xDifferenceSquaredValue + yDifferenceSquaredValue;

        // Step 9: Take the square root of the sum to get the distance
        double distanceBetweenPointsValue = Math.sqrt(sumOfSquaresValue);

        // Step 10: Print the distance result
        System.out.println("The distance of the two points is " + distanceBetweenPointsValue);

        // Step 11: Close the scanner
        userInputScanner.close();
    }
}