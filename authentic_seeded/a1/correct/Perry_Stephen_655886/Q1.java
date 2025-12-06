import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter v0, v1, and t on the same line
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user
        double startingVelocityV0Input = userInputScanner.nextDouble();
        double startingVelocityV0 = startingVelocityV0Input; // Temporary holder variable

        // Read the final velocity v1 from the user
        double finalVelocityV1Input = userInputScanner.nextDouble();
        double finalVelocityV1 = finalVelocityV1Input; // Temporary holder variable

        // Read the time t from the user
        double timeTInput = userInputScanner.nextDouble();
        double timeT = timeTInput; // Temporary holder variable

        // Initialize the acceleration variable
        double averageAcceleration = 0.0;

        // Check that time is not zero to avoid division by zero
        if (timeT != 0.0) {
            // Compute the change in velocity (v1 - v0)
            double changeInVelocity = finalVelocityV1 - startingVelocityV0;

            // Compute the average acceleration using the formula (v1 - v0) / t
            double computedAcceleration = changeInVelocity / timeT;
            averageAcceleration = computedAcceleration;
        } else {
            // If time is zero, keep acceleration as 0.0 (very defensive programming)
            double zeroAccelerationHolder = 0.0;
            averageAcceleration = zeroAccelerationHolder;
        }

        // Display the result to the user
        System.out.println("The average acceleration is " + averageAcceleration);

        // Close the scanner to be safe
        userInputScanner.close();
    }
}