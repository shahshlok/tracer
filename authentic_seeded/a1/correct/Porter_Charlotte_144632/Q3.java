import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {

        // Step 1: Create a Scanner object so we can read input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter x1 and y1
        System.out.print("Enter x1 and y1: ");

        // Step 3: Read x1 and y1 from the user
        double firstPointXValue = userInputScanner.nextDouble();
        double firstPointYValue = userInputScanner.nextDouble();

        // Step 4: Ask the user to enter x2 and y2
        System.out.print("Enter x2 and y2: ");

        // Step 5: Read x2 and y2 from the user
        double secondPointXValue = userInputScanner.nextDouble();
        double secondPointYValue = userInputScanner.nextDouble();

        // Step 6: Compute the difference in x values (x2 - x1)
        double differenceInXValues = secondPointXValue - firstPointXValue;

        // Step 7: Compute the difference in y values (y2 - y1)
        double differenceInYValues = secondPointYValue - firstPointYValue;

        // Step 8: Square the difference in x values
        double squaredDifferenceInXValues = differenceInXValues * differenceInXValues;

        // Step 9: Square the difference in y values
        double squaredDifferenceInYValues = differenceInYValues * differenceInYValues;

        // Step 10: Add the squared differences together
        double sumOfSquaredDifferences = squaredDifferenceInXValues + squaredDifferenceInYValues;

        // Step 11: Take the square root of the sum to get the distance
        double distanceBetweenPoints = Math.sqrt(sumOfSquaredDifferences);

        // Step 12: Print out the final distance
        System.out.println("The distance of the two points is " + distanceBetweenPoints);

        // Step 13: Close the Scanner
        userInputScanner.close();
    }
}