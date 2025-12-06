import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter v0, v1, and t all on one line
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user
        double startingVelocityV0 = userInputScanner.nextDouble();

        // Read the final velocity v1 from the user
        double finalVelocityV1 = userInputScanner.nextDouble();

        // Read the time t from the user
        double timeElapsedT = userInputScanner.nextDouble();

        // Create a temporary variable to hold the difference between v1 and v0
        double velocityDifferenceHolder = finalVelocityV1 - startingVelocityV0;

        // Create a variable to hold the average acceleration
        double averageAcceleration = 0.0;

        // Nervous check: make sure timeElapsedT is not zero before dividing
        if (timeElapsedT != 0) {
            // Calculate the average acceleration using the formula (v1 - v0) / t
            averageAcceleration = velocityDifferenceHolder / timeElapsedT;
        } else {
            // If time is zero, just keep acceleration as 0.0 (though this is not physically valid)
            averageAcceleration = 0.0;
        }

        // Print the result exactly in the required format
        System.out.println("The average acceleration is " + averageAcceleration);

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}