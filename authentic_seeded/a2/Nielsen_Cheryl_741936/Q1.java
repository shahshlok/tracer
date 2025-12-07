import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user to enter 5 integers, just like in the sample run
        System.out.print("Enter 5 integers: ");

        // Declare a variable to keep track of the sum of the even numbers
        int sumOfEvenNumbers = 0;

        // We know we need exactly 5 integers, so we can loop 5 times
        for (int loopCounter = 0; loopCounter < 5; loopCounter++) {
            // Read the next integer from the user
            int userInputValue = userInputScanner.nextInt();

            // Use a mathematical check to see if the number is even
            // A number is even if the remainder when divided by 2 is zero
            int divisor = 2;
            int remainder = userInputValue % divisor;

            // If the remainder is zero, the number is even, so we add it to our sum
            if (remainder == 0) {
                // Add this even number to the running total
                sumOfEvenNumbers = sumOfEvenNumbers + userInputValue;
            }
        }

        // After processing all 5 integers, print the sum of only the even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner to be polite with system resources
        userInputScanner.close();
    }
}