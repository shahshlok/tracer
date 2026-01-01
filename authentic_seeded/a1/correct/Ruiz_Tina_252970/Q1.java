import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter v0, v1, and t
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user
        double startingVelocityV0Input = userInputScanner.nextDouble();

        // Read the final velocity v1 from the user
        double finalVelocityV1Input = userInputScanner.nextDouble();

        // Read the time t from the user
        double timeTInput = userInputScanner.nextDouble();

        // Use a temporary variable to hold the difference (v1 - v0)
        double velocityDifferenceHolder = finalVelocityV1Input - startingVelocityV0Input;

        // Initialize the average acceleration with a default value
        double averageAccelerationResult = 0.0;

        // Extra cautious check to avoid division by zero, even though the assignment probably assumes valid input
        if (timeTInput != 0.0) {
            // Compute the average acceleration using the formula (v1 - v0) / t
            averageAccelerationResult = velocityDifferenceHolder / timeTInput;
        } else {
            // If time is zero, we will just keep the default value (0.0) to avoid crashing
            averageAccelerationResult = 0.0;
        }

        // Print the result of the average acceleration
        System.out.println("The average acceleration is " + averageAccelerationResult);

        // Close the scanner to be polite to system resources
        userInputScanner.close();
    }
}