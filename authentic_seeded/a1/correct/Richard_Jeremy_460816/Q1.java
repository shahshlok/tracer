import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter starting velocity v0, final velocity v1, and time t
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity value from the user as a double
        double startingVelocityV0 = userInputScanner.nextDouble();

        // Read the final velocity value from the user as a double
        double finalVelocityV1 = userInputScanner.nextDouble();

        // Read the time value from the user as a double
        double timeT = userInputScanner.nextDouble();

        // Compute the average acceleration using the formula (v1 - v0) / t
        double averageAcceleration = (finalVelocityV1 - startingVelocityV0) / timeT;

        // Display the result to the user
        System.out.println("The average acceleration is " + averageAcceleration);

        // Close the scanner because we are done using it
        userInputScanner.close();
    }
}