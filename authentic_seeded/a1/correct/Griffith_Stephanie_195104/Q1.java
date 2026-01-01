import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter initial velocity v0, final velocity v1, and time t
        System.out.print("Enter v0, v1, and t: ");

        // Read the starting velocity v0 from the user
        double startingVelocityV0 = userInputScanner.nextDouble();

        // Read the final velocity v1 from the user
        double finalVelocityV1 = userInputScanner.nextDouble();

        // Read the time t from the user
        double timeT = userInputScanner.nextDouble();

        // Declare intermediate math variables a, b, c to structure the formula
        // a will represent v1
        double a = finalVelocityV1;

        // b will represent v0
        double b = startingVelocityV0;

        // c will represent t
        double c = timeT;

        // Use the acceleration formula a_acceleration = (v1 - v0) / t
        // First compute the change in velocity (v1 - v0)
        double changeInVelocity = a - b;

        // Then compute the average acceleration by dividing change in velocity by time
        double averageAcceleration = changeInVelocity / c;

        // Display the result to the user
        System.out.println("The average acceleration is " + averageAcceleration);

        // Close the Scanner to release system resources
        userInputScanner.close();
    }
}