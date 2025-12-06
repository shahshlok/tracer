import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read from the keyboard
        Scanner keyboardScanner = new Scanner(System.in);

        // Prompt the user to enter v0, v1, and t, exactly as in the sample run
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user
        double startingVelocityV0 = keyboardScanner.nextDouble();

        // Read the final velocity v1 from the user
        double finalVelocityV1 = keyboardScanner.nextDouble();

        // Read the time t from the user
        double timeIntervalT = keyboardScanner.nextDouble();

        // I am nervous about division by zero, so I will check that time is not zero
        // even though the assignment probably assumes a valid nonzero time
        double safeTimeIntervalT = timeIntervalT;
        if (safeTimeIntervalT == 0.0) {
            // If time is zero, the acceleration would be undefined.
            // I will just print a message and end the program.
            System.out.println("The time t cannot be zero. Acceleration is undefined.");
            keyboardScanner.close();
            return;
        }

        // Calculate the change in velocity (v1 - v0) using a temporary variable
        double changeInVelocity = finalVelocityV1 - startingVelocityV0;

        // Calculate the average acceleration using another temporary variable
        double averageAcceleration = changeInVelocity / safeTimeIntervalT;

        // Print the result exactly as required in the sample run
        System.out.println("The average acceleration is " + averageAcceleration);

        // Close the scanner to be polite to system resources
        keyboardScanner.close();
    }
}