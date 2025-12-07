import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user to enter 5 integers on one line
        System.out.print("Enter 5 integers: ");

        // Declare a variable to hold the running sum of even numbers
        int sumOfEvenNumbers = 0;

        // We know we need to read exactly 5 integers
        int totalNumberOfInputs = 5;

        // Loop exactly 5 times to read 5 integers
        for (int inputCounter = 1; inputCounter <= totalNumberOfInputs; inputCounter++) {
            // Read the next integer from the user
            int userInputValue = userInputScanner.nextInt();

            // Declare an intermediate math variable to hold the remainder when divided by 2
            int remainderWhenDividedByTwo = userInputValue % 2;

            // If the remainder is 0, then the number is even
            if (remainderWhenDividedByTwo == 0) {
                // Add this even number to our running sum
                sumOfEvenNumbers = sumOfEvenNumbers + userInputValue;
            }
        }

        // After processing all 5 numbers, print the sum of even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}