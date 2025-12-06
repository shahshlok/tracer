import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the starting velocity v0, final velocity v1, and time t
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user
        double startingVelocityV0 = userInputScanner.nextDouble();

        // Read the final velocity v1 from the user
        double finalVelocityV1 = userInputScanner.nextDouble();

        // Read the time t from the user
        double timeT = userInputScanner.nextDouble();

        // Declare a, b, c as intermediate math variables to follow the formula a = (v1 - v0) / t
        double a;
        double b;
        double c;

        // Assign v1 to b and v0 to c to prepare for subtraction
        b = finalVelocityV1;
        c = startingVelocityV0;

        // Compute the change in velocity (v1 - v0)
        double changeInVelocity = b - c;

        // Assign the time t to a time variable for the denominator
        a = timeT;

        // Compute the average acceleration using the formula a = (v1 - v0) / t
        double averageAcceleration = changeInVelocity / a;

        // Display the result to the user with the required message
        System.out.println("The average acceleration is " + averageAcceleration);

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}