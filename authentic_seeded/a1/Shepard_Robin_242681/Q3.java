import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the first point's coordinates x1 and y1
        System.out.print("Enter x1 and y1: ");
        double userInputX1 = userInputScanner.nextDouble(); // Read x1
        double userInputY1 = userInputScanner.nextDouble(); // Read y1

        // Prompt the user to enter the second point's coordinates x2 and y2
        System.out.print("Enter x2 and y2: ");
        double userInputX2 = userInputScanner.nextDouble(); // Read x2
        double userInputY2 = userInputScanner.nextDouble(); // Read y2

        // Calculate the difference in x coordinates
        double differenceInX = userInputX2 - userInputX1;
        // Use a temporary holder to store the squared difference in x
        double squaredDifferenceInX = differenceInX * differenceInX;

        // Calculate the difference in y coordinates
        double differenceInY = userInputY2 - userInputY1;
        // Use a temporary holder to store the squared difference in y
        double squaredDifferenceInY = differenceInY * differenceInY;

        // Add the squared differences together
        double sumOfSquaredDifferences = squaredDifferenceInX + squaredDifferenceInY;

        // Just to be extra safe, check if sumOfSquaredDifferences is not negative before sqrt
        if (sumOfSquaredDifferences < 0) {
            // If something went wrong and it is negative, set it to 0 to avoid Math.sqrt issues
            sumOfSquaredDifferences = 0;
        }

        // Compute the distance using the square root of the sum of squared differences
        double distanceBetweenPoints = Math.sqrt(sumOfSquaredDifferences);

        // Output the result exactly as required
        System.out.println("The distance of the two points is " + distanceBetweenPoints);

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}