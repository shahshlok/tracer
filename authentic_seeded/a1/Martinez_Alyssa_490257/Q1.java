import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter v0, v1, and t all on the same line
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user
        double startingVelocityV0 = userInputScanner.nextDouble();
        double temporaryHolderForV0 = startingVelocityV0; // temporary holder just in case

        // Read the final velocity v1 from the user
        double finalVelocityV1 = userInputScanner.nextDouble();
        double temporaryHolderForV1 = finalVelocityV1; // temporary holder just in case

        // Read the time t from the user
        double timeDurationT = userInputScanner.nextDouble();
        double temporaryHolderForT = timeDurationT; // temporary holder just in case

        // It is very important to check that time is not zero to avoid division by zero
        if (temporaryHolderForT == 0) {
            // If time is zero, we cannot compute acceleration safely
            System.out.println("The average acceleration is Infinity");
        } else {
            // Compute the change in velocity (v1 - v0) using temporary holders
            double changeInVelocity = temporaryHolderForV1 - temporaryHolderForV0;

            // Compute the average acceleration using the formula (v1 - v0) / t
            double averageAcceleration = changeInVelocity / temporaryHolderForT;

            // Extra nervous check just to ensure we are not dividing by zero (again)
            if (temporaryHolderForT != 0) {
                // Display the result to the user
                System.out.println("The average acceleration is " + averageAcceleration);
            }
        }

        // Close the scanner to free resources
        userInputScanner.close();
    }
}