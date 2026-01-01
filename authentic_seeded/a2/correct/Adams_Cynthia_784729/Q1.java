import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user to enter 5 integers on the same line or separate lines
        System.out.print("Enter 5 integers: ");

        // Declare a variable to store the running sum of even numbers
        int sumOfEvenNumbers = 0;

        // We will read exactly 5 integers, so we loop 5 times
        for (int inputCounter = 0; inputCounter < 5; inputCounter++) {
            // Read the next integer from the user
            int userInputValue = userInputScanner.nextInt();

            // Step 1: Calculate remainder when divided by 2
            int a = userInputValue;
            int b = 2;
            int c = a % b; // c is the remainder of a divided by b

            // If the remainder is 0, then the number is even
            if (c == 0) {
                // Add this even number to our running sum
                sumOfEvenNumbers = sumOfEvenNumbers + userInputValue;
            }
        }

        // After reading 5 integers, print the sum of even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}