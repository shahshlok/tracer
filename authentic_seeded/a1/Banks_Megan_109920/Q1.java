import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter starting velocity v0, final velocity v1, and time t
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user as an int
        int startingVelocityV0 = userInputScanner.nextInt();

        // Read the final velocity v1 from the user as an int
        int finalVelocityV1 = userInputScanner.nextInt();

        // Read the time t from the user as an int
        int timeT = userInputScanner.nextInt();

        // Declare intermediate variables a, b, c to structure the formula
        int a = finalVelocityV1;      // a will represent v1
        int b = startingVelocityV0;   // b will represent v0
        int c = timeT;                // c will represent t

        // Compute the numerator (v1 - v0)
        int velocityDifference = a - b;

        // Compute the average acceleration using the formula a = (v1 - v0) / t
        // I will cast the result of the division to double to make sure it is a decimal
        double averageAcceleration = (double)(velocityDifference / c);

        // Display the result of the average acceleration
        System.out.println("The average acceleration is " + averageAcceleration);

        // Close the scanner to avoid resource leak
        userInputScanner.close();
    }
}