import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object for reading user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter v0, v1, and t on one line
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from user input
        double startingVelocityV0 = 0.0;
        if (userInputScanner.hasNextDouble()) {
            startingVelocityV0 = userInputScanner.nextDouble();
        }

        // Read the final velocity v1 from user input
        double finalVelocityV1 = 0.0;
        if (userInputScanner.hasNextDouble()) {
            finalVelocityV1 = userInputScanner.nextDouble();
        }

        // Read the time t from user input
        double timeIntervalT = 0.0;
        if (userInputScanner.hasNextDouble()) {
            timeIntervalT = userInputScanner.nextDouble();
        }

        // Close the Scanner to avoid resource leaks
        userInputScanner.close();

        // Prepare a variable to hold the average acceleration
        double averageAccelerationResult = 0.0;

        // Check that time is not zero to avoid division by zero
        if (timeIntervalT != 0.0) {
            // Compute the change in velocity v1 - v0
            double changeInVelocity = finalVelocityV1 - startingVelocityV0;

            // Compute the average acceleration using the formula (v1 - v0) / t
            averageAccelerationResult = changeInVelocity / timeIntervalT;
        }

        // Display the average acceleration to the user
        System.out.println("The average acceleration is " + averageAccelerationResult);
    }
}