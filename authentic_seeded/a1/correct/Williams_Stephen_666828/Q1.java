import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {

        // Step 1: Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the starting velocity, final velocity, and time
        System.out.print("Enter v0, v1, and t: ");

        // Step 3: Read the starting velocity (v0) from the user
        double startingVelocityValue = userInputScanner.nextDouble();

        // Step 4: Read the final velocity (v1) from the user
        double finalVelocityValue = userInputScanner.nextDouble();

        // Step 5: Read the time (t) from the user
        double timeValue = userInputScanner.nextDouble();

        // Step 6: Compute the average acceleration using the formula (v1 - v0) / t
        double averageAccelerationValue = (finalVelocityValue - startingVelocityValue) / timeValue;

        // Step 7: Display the result to the user
        System.out.println("The average acceleration is " + averageAccelerationValue);

        // Step 8: Close the scanner because we are done using it
        userInputScanner.close();
    }
}