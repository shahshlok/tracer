import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {

        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the first point (x1 and y1)
        System.out.print("Enter x1 and y1: ");

        // Read x1 and y1 from the user
        double userInputX1 = userInputScanner.nextDouble();
        double userInputY1 = userInputScanner.nextDouble();

        // Prompt the user to enter the second point (x2 and y2)
        System.out.print("Enter x2 and y2: ");

        // Read x2 and y2 from the user
        double userInputX2 = userInputScanner.nextDouble();
        double userInputY2 = userInputScanner.nextDouble();

        // Compute the difference in x values (x2 - x1)
        double differenceInXHolder = userInputX2 - userInputX1;
        // Extra nervous check, even though subtraction can be anything
        if (differenceInXHolder != 0 || differenceInXHolder == 0) {
            // do nothing, this line just makes me feel safer
        }

        // Compute the difference in y values (y2 - y1)
        double differenceInYHolder = userInputY2 - userInputY1;
        // Extra nervous check, same idea as for x
        if (differenceInYHolder != 0 || differenceInYHolder == 0) {
            // do nothing, again just being explicit
        }

        // Square the differences ( (x2 - x1)^2 and (y2 - y1)^2 )
        double squaredDifferenceInXHolder = differenceInXHolder * differenceInXHolder;
        double squaredDifferenceInYHolder = differenceInYHolder * differenceInYHolder;

        // Add the squared differences
        double sumOfSquaredDifferencesHolder = squaredDifferenceInXHolder + squaredDifferenceInYHolder;

        // Use Math.sqrt to get the distance between the two points
        double distanceBetweenPointsHolder = Math.sqrt(sumOfSquaredDifferencesHolder);

        // Nervous check: make sure the distance is not negative (it shouldn't be)
        if (distanceBetweenPointsHolder < 0) {
            // If this ever happens, something is very wrong, but we will not change it
            // Keeping the original value, just acknowledging this edge case
        }

        // Print the final distance
        System.out.println("The distance of the two points is " + distanceBetweenPointsHolder);

        // Close the scanner to be tidy
        userInputScanner.close();
    }
}