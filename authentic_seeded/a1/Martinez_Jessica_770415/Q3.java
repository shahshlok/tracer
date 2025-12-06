import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read user input from the keyboard
        Scanner keyboardScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the first point's coordinates x1 and y1
        System.out.print("Enter x1 and y1: ");

        // Step 3: Read x1 and y1 as double values from the user
        double firstPointXCoordinate = keyboardScanner.nextDouble();
        double firstPointYCoordinate = keyboardScanner.nextDouble();

        // Step 4: Ask the user to enter the second point's coordinates x2 and y2
        System.out.print("Enter x2 and y2: ");

        // Step 5: Read x2 and y2 as double values from the user
        double secondPointXCoordinate = keyboardScanner.nextDouble();
        double secondPointYCoordinate = keyboardScanner.nextDouble();

        // Step 6: Compute the difference in x values (x2 - x1)
        double differenceInXCoordinates = secondPointXCoordinate - firstPointXCoordinate;

        // Step 7: Compute the difference in y values (y2 - y1)
        double differenceInYCoordinates = secondPointYCoordinate - firstPointYCoordinate;

        // Step 8: Square the difference in x values ( (x2 - x1)^2 )
        double squaredDifferenceInXCoordinates = differenceInXCoordinates * differenceInXCoordinates;

        // Step 9: Square the difference in y values ( (y2 - y1)^2 )
        double squaredDifferenceInYCoordinates = differenceInYCoordinates * differenceInYCoordinates;

        // Step 10: Add the squared differences together
        double sumOfSquaredDifferences = squaredDifferenceInXCoordinates + squaredDifferenceInYCoordinates;

        // Step 11: Take the square root of the sum to get the distance
        double distanceBetweenPoints = Math.sqrt(sumOfSquaredDifferences);

        // Step 12: Print out the distance between the two points
        System.out.println("The distance of the two points is " + distanceBetweenPoints);

        // Step 13: Close the scanner because we are done reading input
        keyboardScanner.close();
    }
}