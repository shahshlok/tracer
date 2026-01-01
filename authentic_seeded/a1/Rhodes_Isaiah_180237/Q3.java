import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter x1 and y1
        System.out.print("Enter x1 and y1: ");
        double firstPointXValue = userInputScanner.nextDouble();
        double firstPointYValue = userInputScanner.nextDouble();

        // Step 3: Ask the user to enter x2 and y2
        System.out.print("Enter x2 and y2: ");
        double secondPointXValue = userInputScanner.nextDouble();
        double secondPointYValue = userInputScanner.nextDouble();

        // Step 4: Compute the difference in x values (x2 - x1)
        double differenceInXValues = secondPointXValue - firstPointXValue;

        // Step 5: Compute the difference in y values (y2 - y1)
        double differenceInYValues = secondPointYValue - firstPointYValue;

        // Step 6: Square the difference in x values
        double squaredDifferenceInXValues = differenceInXValues * differenceInXValues;

        // Step 7: Square the difference in y values
        double squaredDifferenceInYValues = differenceInYValues * differenceInYValues;

        // Step 8: Add the squared differences together
        double sumOfSquaredDifferences = squaredDifferenceInXValues + squaredDifferenceInYValues;

        // Step 9: Take the square root of the sum to get the distance
        Math.sqrt(sumOfSquaredDifferences);

        // Step 10: Display the result to the user
        System.out.println("The distance of the two points is " + sumOfSquaredDifferences);

        // Step 11: Close the Scanner
        userInputScanner.close();
    }
}