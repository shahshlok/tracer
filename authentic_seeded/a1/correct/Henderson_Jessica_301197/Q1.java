import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the starting velocity v0, final velocity v1, and time t
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user
        double startingVelocityV0 = userInputScanner.nextDouble();

        // Read the final velocity v1 from the user
        double finalVelocityV1 = userInputScanner.nextDouble();

        // Read the time t from the user
        double timeT = userInputScanner.nextDouble();

        // Declare intermediate variables a, b, c to represent parts of the formula
        // a will represent v1
        double a = finalVelocityV1;

        // b will represent v0
        double b = startingVelocityV0;

        // c will represent t (time)
        double c = timeT;

        // Use the formula a = (v1 - v0) / t
        // Here we compute the numerator (v1 - v0)
        double numeratorDifference = a - b;

        // Now compute the acceleration by dividing the numerator by time t
        double averageAcceleration = numeratorDifference / c;

        // Display the result of the average acceleration
        System.out.println("The average acceleration is " + averageAcceleration);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}