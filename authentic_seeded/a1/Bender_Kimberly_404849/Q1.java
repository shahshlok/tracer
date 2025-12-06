import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter v0, v1, and t all on one line
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user as a double
        double startingVelocityInput = userInputScanner.nextDouble();

        // Read the final velocity v1 from the user as a double
        double finalVelocityInput = userInputScanner.nextDouble();

        // Read the time t from the user as a double
        double timeInput = userInputScanner.nextDouble();

        // Create holder variables to make the formula steps clear
        double velocityDifferenceHolder = 0.0;
        double averageAccelerationResult = 0.0;

        // Calculate the difference in velocity (v1 - v0)
        velocityDifferenceHolder = finalVelocityInput - startingVelocityInput;

        // Check that time is not zero before doing the division to avoid division by zero
        if (timeInput != 0.0) {
            // Calculate the average acceleration using the given formula
            averageAccelerationResult = velocityDifferenceHolder / timeInput;
        } else {
            // If time is zero, keep averageAccelerationResult as 0.0 (though this is not physically valid)
            averageAccelerationResult = 0.0;
        }

        // Print the result of the average acceleration
        System.out.println("The average acceleration is " + averageAccelerationResult);

        // Close the scanner to avoid resource leak, even though the program ends here
        userInputScanner.close();
    }
}