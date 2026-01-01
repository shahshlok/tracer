import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {

        // Step 1: Create a Scanner object so we can read input from the keyboard
        Scanner keyboardInput = new Scanner(System.in);

        // Step 2: Ask the user to enter x1 and y1
        System.out.print("Enter x1 and y1: ");

        // Step 3: Read x1 and y1 as double values
        double pointOneXValue = keyboardInput.nextDouble();
        double pointOneYValue = keyboardInput.nextDouble();

        // Step 4: Ask the user to enter x2 and y2
        System.out.print("Enter x2 and y2: ");

        // Step 5: Read x2 and y2 as double values
        double pointTwoXValue = keyboardInput.nextDouble();
        double pointTwoYValue = keyboardInput.nextDouble();

        // Step 6: Compute the difference in x values (x2 - x1)
        double differenceInXValues = pointTwoXValue - pointOneXValue;

        // Step 7: Compute the difference in y values (y2 - y1)
        double differenceInYValues = pointTwoYValue - pointOneYValue;

        // Step 8: Square the difference in x values
        double squaredDifferenceInXValues = differenceInXValues * differenceInXValues;

        // Step 9: Square the difference in y values
        double squaredDifferenceInYValues = differenceInYValues * differenceInYValues;

        // Step 10: Add the squared differences together
        double sumOfSquaredDifferences = squaredDifferenceInXValues + squaredDifferenceInYValues;

        // Step 11: Take the square root of the sum to get the distance
        double distanceBetweenPoints = Math.sqrt(sumOfSquaredDifferences);

        // Step 12: Display the distance to the user
        System.out.println("The distance of the two points is " + distanceBetweenPoints);

        // Step 13: Close the scanner because we are done with input
        keyboardInput.close();
    }
}