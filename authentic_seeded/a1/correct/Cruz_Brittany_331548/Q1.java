import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the starting velocity v0, the final velocity v1, and the time t
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user
        double startingVelocityV0 = userInputScanner.nextDouble();

        // Read the final velocity v1 from the user
        double finalVelocityV1 = userInputScanner.nextDouble();

        // Read the time t from the user
        double timeT = userInputScanner.nextDouble();

        // Declare intermediate math variables to match the formula a = (v1 - v0) / t
        double aAcceleration;
        double bVelocityDifference;
        double cTimeValue;

        // Compute the difference in velocities: v1 - v0
        bVelocityDifference = finalVelocityV1 - startingVelocityV0;

        // Store the time value separately
        cTimeValue = timeT;

        // Compute the acceleration using the formula a = (v1 - v0) / t
        aAcceleration = bVelocityDifference / cTimeValue;

        // Display the result of the acceleration calculation
        System.out.println("The average acceleration is " + aAcceleration);

        // Close the Scanner to free associated resources
        userInputScanner.close();
    }
}