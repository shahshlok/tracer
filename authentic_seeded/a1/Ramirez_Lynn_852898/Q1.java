import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter v0, v1, and t in one line
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user
        double startingVelocityV0 = userInputScanner.nextDouble();

        // Read the final velocity v1 from the user
        double finalVelocityV1 = userInputScanner.nextDouble();

        // Read the time t from the user
        double timeT = userInputScanner.nextDouble();

        // Compute the average acceleration using the formula (v1 - v0) / t
        double averageAcceleration = (finalVelocityV1 - startingVelocityV0) / timeT;

        // Display the result to the user
        System.out.println("The average acceleration is " + averageAcceleration);

        // Close the scanner because we are done reading input
        userInputScanner.close();
    }
}