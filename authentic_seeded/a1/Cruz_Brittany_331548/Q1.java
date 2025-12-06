import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Declare the starting velocity v0, final velocity v1, and time t with initial values
        double startingVelocityV0 = 0.0;
        double finalVelocityV1 = 0.0;
        double timeT = 0.0;

        // Declare intermediate math variables to match the formula a = (v1 - v0) / t
        double aAcceleration;
        double bVelocityDifference;
        double cTimeValue;

        // Compute the difference in velocities using the initial values: v1 - v0
        bVelocityDifference = finalVelocityV1 - startingVelocityV0;

        // Store the time value separately using the initial time
        cTimeValue = timeT;

        // Compute the acceleration using the formula a = (v1 - v0) / t with the initial values
        aAcceleration = bVelocityDifference / cTimeValue;

        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the starting velocity v0, the final velocity v1, and the time t
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user
        startingVelocityV0 = userInputScanner.nextDouble();

        // Read the final velocity v1 from the user
        finalVelocityV1 = userInputScanner.nextDouble();

        // Read the time t from the user
        timeT = userInputScanner.nextDouble();

        // Display the result of the acceleration calculation
        System.out.println("The average acceleration is " + aAcceleration);

        // Close the Scanner to free associated resources
        userInputScanner.close();
    }
}