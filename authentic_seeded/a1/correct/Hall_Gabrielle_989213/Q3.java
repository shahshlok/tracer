import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user for the first point's x and y values
        System.out.print("Enter x1 and y1: ");
        double pointOneXValue = userInputScanner.nextDouble();
        double pointOneYValue = userInputScanner.nextDouble();

        // Step 3: Ask the user for the second point's x and y values
        System.out.print("Enter x2 and y2: ");
        double pointTwoXValue = userInputScanner.nextDouble();
        double pointTwoYValue = userInputScanner.nextDouble();

        // Step 4: Compute the difference in x values (x2 - x1)
        double differenceInXValues = pointTwoXValue - pointOneXValue;

        // Step 5: Compute the difference in y values (y2 - y1)
        double differenceInYValues = pointTwoYValue - pointOneYValue;

        // Step 6: Square the difference in x values
        double squareOfDifferenceInXValues = differenceInXValues * differenceInXValues;

        // Step 7: Square the difference in y values
        double squareOfDifferenceInYValues = differenceInYValues * differenceInYValues;

        // Step 8: Add the squared differences together
        double sumOfSquaredDifferences = squareOfDifferenceInXValues + squareOfDifferenceInYValues;

        // Step 9: Take the square root of the sum to get the distance
        double distanceBetweenPoints = Math.sqrt(sumOfSquaredDifferences);

        // Step 10: Print out the result to the user
        System.out.println("The distance of the two points is " + distanceBetweenPoints);

        // Step 11: Close the scanner to free system resources
        userInputScanner.close();
    }
}