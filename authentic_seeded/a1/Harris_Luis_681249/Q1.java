import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user for starting velocity v0, final velocity v1, and time t
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user
        double startingVelocityV0Input = userInputScanner.nextDouble();
        double startingVelocityV0 = startingVelocityV0Input; // temporary holder assignment

        // Read the final velocity v1 from the user
        double finalVelocityV1Input = userInputScanner.nextDouble();
        double finalVelocityV1 = finalVelocityV1Input; // temporary holder assignment

        // Read the time t from the user
        double timeTInput = userInputScanner.nextDouble();
        double timeT = timeTInput; // temporary holder assignment

        // Initialize the average acceleration variable
        double averageAcceleration = 0.0;

        // Check to make sure time is not zero to avoid division by zero
        if (timeT != 0.0) {
            // Compute the change in velocity v1 - v0
            double changeInVelocity = finalVelocityV1 - startingVelocityV0;

            // Compute the average acceleration using the formula (v1 - v0) / t
            averageAcceleration = changeInVelocity / timeT;
        } else {
            // If time is zero, we will keep averageAcceleration as 0.0
            // This is a basic guard against division by zero
            averageAcceleration = 0.0;
        }

        // Print the result for the user
        System.out.println("The average acceleration is " + averageAcceleration);

        // Close the Scanner to be safe
        userInputScanner.close();
    }
}