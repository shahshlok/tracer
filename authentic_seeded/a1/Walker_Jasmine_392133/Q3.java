import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner keyboardInput = new Scanner(System.in);

        // Prompt the user for the first point (x1 and y1)
        System.out.print("Enter x1 and y1: ");
        double firstPointXValue = keyboardInput.nextDouble();
        double firstPointYValue = keyboardInput.nextDouble();

        // Prompt the user for the second point (x2 and y2)
        System.out.print("Enter x2 and y2: ");
        double secondPointXValue = keyboardInput.nextDouble();
        double secondPointYValue = keyboardInput.nextDouble();

        // Calculate the difference in x values (x2 - x1)
        double differenceInXValues = secondPointXValue - firstPointXValue;

        // Calculate the difference in y values (y2 - y1)
        double differenceInYValues = secondPointYValue - firstPointYValue;

        // Calculate the square of the difference in x values
        double squaredDifferenceInXValues = differenceInXValues * differenceInXValues;

        // Calculate the square of the difference in y values
        double squaredDifferenceInYValues = differenceInYValues * differenceInYValues;

        // Add the squared differences together to get the value under the square root
        double sumOfSquaredDifferences = squaredDifferenceInXValues + squaredDifferenceInYValues;

        // Just to be extra safe, check that the sumOfSquaredDifferences is not negative
        // (it should not be negative mathematically, but we are being careful)
        if (sumOfSquaredDifferences < 0) {
            // If this happens, we will set it to 0.0 to avoid issues with Math.sqrt
            sumOfSquaredDifferences = 0.0;
        }

        // Compute the distance using Math.sqrt on the sum of squared differences
        double distanceBetweenPoints = Math.sqrt(sumOfSquaredDifferences);

        // Print the result exactly as the sample output format
        System.out.println("The distance of the two points is " + distanceBetweenPoints);

        // Close the scanner to be tidy, even though not strictly required here
        keyboardInput.close();
    }
}