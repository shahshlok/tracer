import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object so we can read input from the keyboard
        Scanner keyboardScanner = new Scanner(System.in);

        // Step 1: Ask the user to enter the first point (x1 and y1)
        System.out.print("Enter x1 and y1: ");

        // Step 2: Read x1 and y1 from the user
        double firstPointXValue = keyboardScanner.nextDouble();
        double firstPointYValue = keyboardScanner.nextDouble();

        // Step 3: Ask the user to enter the second point (x2 and y2)
        System.out.print("Enter x2 and y2: ");

        // Step 4: Read x2 and y2 from the user
        double secondPointXValue = keyboardScanner.nextDouble();
        double secondPointYValue = keyboardScanner.nextDouble();

        // Step 5: Compute the difference in x values (x2 - x1)
        double differenceInXValues = secondPointXValue - firstPointXValue;

        // Step 6: Compute the difference in y values (y2 - y1)
        double differenceInYValues = secondPointYValue - firstPointYValue;

        // Step 7: Square the differences: (x2 - x1)^2 and (y2 - y1)^2
        double squaredDifferenceInXValues = differenceInXValues * differenceInXValues;
        double squaredDifferenceInYValues = differenceInYValues * differenceInYValues;

        // Step 8: Add the squared differences
        double sumOfSquaredDifferences = squaredDifferenceInXValues + squaredDifferenceInYValues;

        // Step 9: Take the square root of the sum to get the distance
        double distanceBetweenPoints = Math.sqrt(sumOfSquaredDifferences);

        // Step 10: Display the result to the user
        System.out.println("The distance of the two points is " + distanceBetweenPoints);

        // Step 11: Close the scanner to free resources
        keyboardScanner.close();
    }
}