import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the starting velocity, final velocity, and time
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity from the user
        double startingVelocity = userInputScanner.nextDouble();

        // Read the final velocity from the user
        double finalVelocity = userInputScanner.nextDouble();

        // Read the time from the user
        double timeElapsed = userInputScanner.nextDouble();

        // Compute the average acceleration using the formula (v1 - v0) / t
        double averageAcceleration = (finalVelocity - startingVelocity) / timeElapsed;

        // Display the result to the user
        System.out.println("The average acceleration is " + averageAcceleration);

        // Close the scanner because we are done reading input
        userInputScanner.close();
    }
}