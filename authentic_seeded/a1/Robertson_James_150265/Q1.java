import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter v0, v1, and t on the same line
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user
        double startingVelocityV0 = userInputScanner.nextDouble();

        // Read the final velocity v1 from the user
        double finalVelocityV1 = userInputScanner.nextDouble();

        // Read the time t from the user
        double timeIntervalT = userInputScanner.nextDouble();

        // Create a temporary variable to hold the numerator (v1 - v0)
        double velocityDifference = finalVelocityV1 - startingVelocityV0;

        // Initialize the average acceleration with a default value
        double averageAcceleration = 0.0;

        // Check that timeIntervalT is not zero to avoid division by zero
        if (timeIntervalT != 0.0) {
            // Perform the division to compute the average acceleration
            double temporaryAccelerationHolder = velocityDifference / timeIntervalT;

            // Assign the result to the averageAcceleration variable
            averageAcceleration = temporaryAccelerationHolder;
        }

        // Print the result of the average acceleration
        System.out.println("The average acceleration is " + averageAcceleration);

        // Close the Scanner to avoid resource leaks
        userInputScanner.close();
    }
}