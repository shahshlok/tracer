import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter 5 integers
        System.out.print("Enter 5 integers: ");

        // This variable will hold the running total of even numbers
        int sumOfEvenNumbers = 0;

        // We know we must read exactly 5 integers, so we loop 5 times
        int totalNumbersToRead = 5;
        int currentLoopIndex = 0;

        // Loop to read each of the 5 integers
        while (currentLoopIndex < totalNumbersToRead) {
            // Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // Create a temporary variable to check if the number is even
            int remainderWhenDividedByTwo = currentUserInputValue % 2;

            // If remainder is 0, then the number is even
            if (remainderWhenDividedByTwo == 0) {
                // Just to be extra sure, check that we are actually adding an integer
                int numberToAddToSum = currentUserInputValue;
                sumOfEvenNumbers = sumOfEvenNumbers + numberToAddToSum;
            }

            // Move to the next position in the loop
            currentLoopIndex = currentLoopIndex + 1;
        }

        // After reading all numbers and summing the even ones, print the result
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner to be safe and prevent resource leaks
        userInputScanner.close();
    }
}