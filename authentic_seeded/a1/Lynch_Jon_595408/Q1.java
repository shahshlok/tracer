import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the starting velocity v0, final velocity v1, and time t
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user
        double startingVelocityV0 = userInputScanner.nextDouble();

        // Read the final velocity v1 from the user
        double finalVelocityV1 = userInputScanner.nextDouble();

        // Read the time t from the user
        double timeT = userInputScanner.nextDouble();

        // Declare math variables to match the formula a = (v1 - v0) / t
        double a; // this will store the average acceleration
        double b; // this will store the change in velocity (v1 - v0)
        double c; // this will store the time t for clarity

        // Compute the change in velocity using the formula part (v1 - v0)
        b = finalVelocityV1 - startingVelocityV0;

        // Store the time t in c (just to match the math-style breakdown)
        c = timeT;

        // Compute the acceleration a = b / c = (v1 - v0) / t
        a = b / c;

        // Display the result of the average acceleration to the user
        System.out.println("The average acceleration is " + a);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}