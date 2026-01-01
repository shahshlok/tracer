import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Prompt the user to enter v0, v1, and t all on one line
        System.out.print("Enter v0, v1, and t: ");

        // Step 3: Read the starting velocity v0 from the user
        double startingVelocityV0 = userInputScanner.nextDouble();

        // Step 4: Read the final velocity v1 from the user
        double finalVelocityV1 = userInputScanner.nextDouble();

        // Step 5: Read the time t from the user
        double elapsedTimeT = userInputScanner.nextDouble();

        // Step 6: Compute the average acceleration using the formula (v1 - v0) / t
        double averageAcceleration = (finalVelocityV1 - startingVelocityV0) / elapsedTimeT;

        // Step 7: Display the result to the user
        System.out.println("The average acceleration is " + averageAcceleration);

        // Step 8: Close the Scanner because we are done with input
        userInputScanner.close();
    }
}