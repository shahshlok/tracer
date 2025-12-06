import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {

        // Create a Scanner object to read input from the user
        Scanner keyboardInputScanner = new Scanner(System.in);

        // Prompt the user to enter v0, v1, and t on the same line
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user
        double startingVelocityInput = keyboardInputScanner.nextDouble();

        // Read the final velocity v1 from the user
        double finalVelocityInput = keyboardInputScanner.nextDouble();

        // Read the time t from the user
        double timeInput = keyboardInputScanner.nextDouble();

        // Create temporary variables to hold the values, in case we need to reuse them
        double startingVelocityValue = startingVelocityInput;
        double finalVelocityValue = finalVelocityInput;
        double timeValue = timeInput;

        // Initialize the average acceleration to zero as a default value
        double averageAccelerationResult = 0.0;

        // Check that time is not zero to avoid division by zero
        if (timeValue != 0.0) {
            // Calculate the change in velocity using a temporary holder variable
            double changeInVelocityValue = finalVelocityValue - startingVelocityValue;

            // Compute the average acceleration using the formula (v1 - v0) / t
            averageAccelerationResult = changeInVelocityValue / timeValue;
        } else {
            // If time is zero, average acceleration is not mathematically defined
            // For this assignment, we will just leave the averageAccelerationResult as 0.0
            averageAccelerationResult = 0.0;
        }

        // Display the result to the user
        System.out.println("The average acceleration is " + averageAccelerationResult);

        // Close the Scanner to avoid potential resource leaks
        keyboardInputScanner.close();
    }
}