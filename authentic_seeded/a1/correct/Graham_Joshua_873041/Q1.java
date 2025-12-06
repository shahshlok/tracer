import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user for starting velocity v0, final velocity v1, and time t
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity as a double
        double startingVelocityV0Input = userInputScanner.nextDouble();
        double startingVelocityV0 = startingVelocityV0Input;

        // Read the final velocity as a double
        double finalVelocityV1Input = userInputScanner.nextDouble();
        double finalVelocityV1 = finalVelocityV1Input;

        // Read the time as a double
        double timeIntervalTInput = userInputScanner.nextDouble();
        double timeIntervalT = timeIntervalTInput;

        // Close the scanner to avoid resource leaks
        userInputScanner.close();

        // Initialize the average acceleration result
        double averageAccelerationResult = 0.0;

        // Check to make sure time is not zero to avoid division by zero
        if (timeIntervalT != 0.0) {
            // Compute the change in velocity
            double changeInVelocityHolder = finalVelocityV1 - startingVelocityV0;

            // Compute the average acceleration using the formula (v1 - v0) / t
            double computedAccelerationHolder = changeInVelocityHolder / timeIntervalT;

            // Store the computed result in the result variable
            averageAccelerationResult = computedAccelerationHolder;
        } else {
            // If time is zero, keep the acceleration as 0.0
            double zeroTimeSafetyHolder = 0.0;
            averageAccelerationResult = zeroTimeSafetyHolder;
        }

        // Print the result of the average acceleration calculation
        System.out.println("The average acceleration is " + averageAccelerationResult);
    }
}