import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user exactly as shown in the sample run
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user
        double startingVelocityV0Input = userInputScanner.nextDouble();

        // Read the final velocity v1 from the user
        double finalVelocityV1Input = userInputScanner.nextDouble();

        // Read the time t from the user
        double timeIntervalTInput = userInputScanner.nextDouble();

        // Create temporary variables to hold the copied values (just to be extra clear)
        double startingVelocityV0 = startingVelocityV0Input;
        double finalVelocityV1 = finalVelocityV1Input;
        double timeIntervalT = timeIntervalTInput;

        // Initialize the acceleration result to 0.0 as a default value
        double averageAccelerationResult = 0.0;

        // Check to avoid division by zero, even though the problem probably assumes non-zero t
        if (timeIntervalT != 0.0) {
            // Compute the change in velocity (v1 - v0)
            double changeInVelocity = finalVelocityV1 - startingVelocityV0;

            // Compute the average acceleration using the formula a = (v1 - v0) / t
            averageAccelerationResult = changeInVelocity / timeIntervalT;
        } else {
            // If time is zero, we cannot compute acceleration safely; keep the default value
            // (The assignment does not say what to do here, but we avoid a crash.)
            averageAccelerationResult = 0.0;
        }

        // Print the result in the required format
        System.out.println("The average acceleration is " + averageAccelerationResult);

        // Close the scanner to be polite with resources
        userInputScanner.close();
    }
}