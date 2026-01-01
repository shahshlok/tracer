import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the starting velocity, final velocity, and time
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from user input
        double startingVelocityV0 = userInputScanner.nextDouble();

        // Read the final velocity v1 from user input
        double finalVelocityV1 = userInputScanner.nextDouble();

        // Read the time t from user input
        double timeIntervalT = userInputScanner.nextDouble();

        // Declare intermediate math variables corresponding to the formula
        // a = (v1 - v0) / t
        double a; // acceleration
        double b; // represents (v1 - v0)
        double c; // represents t

        // Assign values to intermediate variables based on the user input
        b = finalVelocityV1 - startingVelocityV0; // compute change in velocity (v1 - v0)
        c = timeIntervalT; // time t

        // Compute acceleration using the formula a = b / c
        a = b / c;

        // Display the average acceleration
        System.out.println("The average acceleration is " + a);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}