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

        // Declare intermediate math variables to follow the formula a = (v1 - v0) / t
        double differenceInVelocity = finalVelocityV1 - startingVelocityV0; // v1 - v0
        double accelerationA = differenceInVelocity / timeT; // (v1 - v0) / t

        // Display the average acceleration result using the exact numeric value
        System.out.println("The average acceleration is " + accelerationA);

        // Close the scanner to free resources
        userInputScanner.close();
    }
}