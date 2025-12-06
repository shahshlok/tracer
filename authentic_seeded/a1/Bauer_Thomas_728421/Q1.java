import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter v0, v1, and t
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user
        double startingVelocityV0Input = userInputScanner.nextDouble();
        double startingVelocityV0 = startingVelocityV0Input; // Temporary holder variable

        // Read the final velocity v1 from the user
        double finalVelocityV1Input = userInputScanner.nextDouble();
        double finalVelocityV1 = finalVelocityV1Input; // Temporary holder variable

        // Read the time t from the user
        double timeTInput = userInputScanner.nextDouble();
        double timeT = timeTInput; // Temporary holder variable

        // Check that time is not zero to avoid division by zero
        double safeTimeT = timeT;
        if (safeTimeT == 0) {
            // If time is zero, print a message and avoid dividing
            System.out.println("The average acceleration is " + "Infinity (time cannot be zero)");
        } else {
            // Calculate the change in velocity (v1 - v0)
            double changeInVelocity = finalVelocityV1 - startingVelocityV0;

            // Calculate the average acceleration using the formula a = (v1 - v0) / t
            double averageAcceleration = changeInVelocity / safeTimeT;

            // Print the result to the user
            System.out.println("The average acceleration is " + averageAcceleration);
        }

        // Close the Scanner to release system resources
        userInputScanner.close();
    }
}