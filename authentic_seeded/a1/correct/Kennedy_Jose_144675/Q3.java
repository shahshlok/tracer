import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Prompt the user to enter x1 and y1
        System.out.print("Enter x1 and y1: ");

        // Step 3: Read x1 and y1 as double values
        double firstPointXCoordinate = userInputScanner.nextDouble();
        double firstPointYCoordinate = userInputScanner.nextDouble();

        // Step 4: Prompt the user to enter x2 and y2
        System.out.print("Enter x2 and y2: ");

        // Step 5: Read x2 and y2 as double values
        double secondPointXCoordinate = userInputScanner.nextDouble();
        double secondPointYCoordinate = userInputScanner.nextDouble();

        // Step 6: Compute the difference in x-coordinates (x2 - x1)
        double differenceInXCoordinates = secondPointXCoordinate - firstPointXCoordinate;

        // Step 7: Compute the difference in y-coordinates (y2 - y1)
        double differenceInYCoordinates = secondPointYCoordinate - firstPointYCoordinate;

        // Step 8: Square the differences ( (x2 - x1)^2 and (y2 - y1)^2 )
        double squaredDifferenceInX = differenceInXCoordinates * differenceInXCoordinates;
        double squaredDifferenceInY = differenceInYCoordinates * differenceInYCoordinates;

        // Step 9: Add the squared differences together
        double sumOfSquaredDifferences = squaredDifferenceInX + squaredDifferenceInY;

        // Step 10: Take the square root of the sum to get the distance
        double distanceBetweenPoints = Math.sqrt(sumOfSquaredDifferences);

        // Step 11: Print the result in the required format
        System.out.println("The distance of the two points is " + distanceBetweenPoints);

        // Step 12: Close the Scanner to avoid resource leak
        userInputScanner.close();
    }
}