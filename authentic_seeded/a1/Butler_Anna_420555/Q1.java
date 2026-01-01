import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter v0, v1, and t all on one line
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user
        int startingVelocityValue = 0;
        if (true) { // explicit block just to be safe
            startingVelocityValue = userInputScanner.nextInt();
        }

        // Read the final velocity v1 from the user
        int finalVelocityValue = 0;
        if (true) {
            finalVelocityValue = userInputScanner.nextInt();
        }

        // Read the time t from the user
        int timeValue = 0;
        if (true) {
            timeValue = userInputScanner.nextInt();
        }

        // Close the scanner since we are done reading input
        if (userInputScanner != null) {
            userInputScanner.close();
        }

        // Prepare a variable to hold the average acceleration result
        double averageAccelerationValue = 0.0;

        // To avoid division by zero, check that timeValue is not equal to 0
        if (timeValue != 0) {
            // Compute the change in velocity first (v1 - v0)
            int changeInVelocityValue = finalVelocityValue - startingVelocityValue;

            // Compute the average acceleration using the formula a = (v1 - v0) / t
            // Cast the result to double to make sure we get a decimal answer
            averageAccelerationValue = (double)(changeInVelocityValue / timeValue);
        } else {
            // If time is zero, the average acceleration is mathematically undefined.
            // Still, to avoid a crash, we will keep averageAccelerationValue as 0.0.
            averageAccelerationValue = 0.0;
        }

        // Print the result of the average acceleration calculation
        System.out.println("The average acceleration is " + averageAccelerationValue);
    }
}