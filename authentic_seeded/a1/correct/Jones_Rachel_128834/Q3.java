import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the first point values x1 and y1
        System.out.print("Enter x1 and y1: ");
        double firstPointX1Value = userInputScanner.nextDouble(); // Read x1
        double firstPointY1Value = userInputScanner.nextDouble(); // Read y1

        // Step 3: Ask the user to enter the second point values x2 and y2
        System.out.print("Enter x2 and y2: ");
        double secondPointX2Value = userInputScanner.nextDouble(); // Read x2
        double secondPointY2Value = userInputScanner.nextDouble(); // Read y2

        // Step 4: Compute the difference in x values (x2 - x1)
        double differenceInXValues = secondPointX2Value - firstPointX1Value;

        // Step 5: Compute the difference in y values (y2 - y1)
        double differenceInYValues = secondPointY2Value - firstPointY1Value;

        // Step 6: Square the differences in x and y
        double squaredDifferenceInXValues = differenceInXValues * differenceInXValues;
        double squaredDifferenceInYValues = differenceInYValues * differenceInYValues;

        // Step 7: Add the squared differences together
        double sumOfSquaredDifferences = squaredDifferenceInXValues + squaredDifferenceInYValues;

        // Step 8: Take the square root of the sum to get the distance
        double distanceBetweenPoints = Math.sqrt(sumOfSquaredDifferences);

        // Step 9: Print the result to the user
        System.out.println("The distance of the two points is " + distanceBetweenPoints);

        // Step 10: Close the scanner because we are done reading input
        userInputScanner.close();
    }
}