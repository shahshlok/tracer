import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter v0, v1, and t on a single line
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user
        double startingVelocityV0 = 0.0;
        startingVelocityV0 = userInputScanner.nextDouble();

        // Read the final velocity v1 from the user
        double finalVelocityV1 = 0.0;
        finalVelocityV1 = userInputScanner.nextDouble();

        // Read the time t from the user
        double timeIntervalT = 0.0;
        timeIntervalT = userInputScanner.nextDouble();

        // Close the scanner to avoid potential resource leaks
        userInputScanner.close();

        // Create temporary variables for the numerator and denominator to be extra safe
        double velocityDifferenceNumerator = 0.0;
        velocityDifferenceNumerator = finalVelocityV1 - startingVelocityV0;

        double timeDenominator = 0.0;
        timeDenominator = timeIntervalT;

        // Check that the time is not zero to avoid division by zero
        double averageAcceleration = 0.0;
        if (timeDenominator != 0.0) {
            // Perform the calculation only if timeDenominator is not zero
            averageAcceleration = velocityDifferenceNumerator / timeDenominator;
        } else {
            // If time is zero, we will just keep averageAcceleration as 0.0
            // In a more advanced program, we might handle this differently
            averageAcceleration = 0.0;
        }

        // Print the result of the average acceleration to the console
        System.out.println("The average acceleration is " + averageAcceleration);
    }
}