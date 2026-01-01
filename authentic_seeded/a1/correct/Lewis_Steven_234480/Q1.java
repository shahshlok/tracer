import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter v0, v1, and t all on one line
        System.out.print("Enter v0, v1, and t: ");

        // Step 3: Read the starting velocity, final velocity, and time as double values
        double startingVelocityV0 = userInputScanner.nextDouble();
        double finalVelocityV1 = userInputScanner.nextDouble();
        double timeElapsedT = userInputScanner.nextDouble();

        // Step 4: Compute the average acceleration using the formula (v1 - v0) / t
        double averageAcceleration = (finalVelocityV1 - startingVelocityV0) / timeElapsedT;

        // Step 5: Display the result to the user
        System.out.println("The average acceleration is " + averageAcceleration);

        // Step 6: Close the Scanner because we are done with user input
        userInputScanner.close();
    }
}