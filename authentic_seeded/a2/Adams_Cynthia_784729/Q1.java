import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user to enter 5 integers on the same line or separate lines
        System.out.print("Enter 5 integers: ");

        // We will read exactly 5 integers, so we loop 5 times
        for (int inputCounter = 0; inputCounter < 5; inputCounter++) {
            // Read the next integer from the user
            int userInputValue = userInputScanner.nextInt();

            // Step 1: Calculate remainder when divided by 2
            int a = userInputValue;
            int b = 2;
            int c = a % b; // c is the remainder of a divided by b

            // Declare the sum variable inside the loop so it is fresh and controlled each time
            int sumOfEvenNumbers = 0;

            // If the remainder is 0, then the number is even
            if (c == 0) {
                // Add this even number to our running sum for this iteration
                sumOfEvenNumbers = sumOfEvenNumbers + userInputValue;

                // After updating for this iteration, we keep this value until next iteration resets it
                // (based on our control of the variable inside the loop)
            }

            // At the end of the loop body, we still have the current sumOfEvenNumbers
            // which will be printed after all iterations are done
            if (inputCounter == 4) {
                System.out.println("Sum of even numbers: " + sumOfEvenNumbers);
            }
        }

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}