import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the starting velocity, final velocity, and time
        System.out.print("Enter v0, v1, and t: ");

        // Read the three values from the user
        double startingVelocityV0 = userInputScanner.nextDouble();
        double finalVelocityV1 = userInputScanner.nextDouble();
        double timeT = userInputScanner.nextDouble();

        // Declare intermediate math variables to follow the formula structure
        double aNumerator = finalVelocityV1 - startingVelocityV0; // v1 - v0
        double aDenominator = timeT; // t

        // Compute the average acceleration using the formula a = (v1 - v0) / t
        double averageAccelerationA = aNumerator / aDenominator;

        // Display the result of the average acceleration
        System.out.println("The average acceleration is " + averageAccelerationA);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}