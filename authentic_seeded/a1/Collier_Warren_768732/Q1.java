import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter v0, v1, and t
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user
        double startingVelocityV0Input = userInputScanner.nextDouble();
        double startingVelocityV0 = startingVelocityV0Input; // temporary holder

        // Read the final velocity v1 from the user
        double finalVelocityV1Input = userInputScanner.nextDouble();
        double finalVelocityV1 = finalVelocityV1Input; // temporary holder

        // Read the time t from the user
        double timeTInput = userInputScanner.nextDouble();
        double timeT = timeTInput; // temporary holder

        // It is very important that time is not zero to avoid division by zero
        if (timeT != 0) {
            // Calculate the change in velocity (v1 - v0)
            double changeInVelocityInput = finalVelocityV1 - startingVelocityV0;
            double changeInVelocity = changeInVelocityInput; // temporary holder

            // Calculate the average acceleration using (v1 - v0) / t
            double averageAccelerationInput = changeInVelocity / timeT;
            double averageAcceleration = averageAccelerationInput; // temporary holder

            // Display the result
            System.out.println("The average acceleration is " + averageAcceleration);
        } else {
            // If time is zero, we cannot compute acceleration safely
            System.out.println("The average acceleration is undefined because time t is zero.");
        }

        // Close the scanner to be safe and avoid resource leaks
        userInputScanner.close();
    }
}