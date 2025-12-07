import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {

        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter 5 integers
        System.out.print("Enter 5 integers: ");

        // Variable to hold the running total of even numbers
        int sumOfEvenNumbers = 0;

        // Variable to temporarily hold each user's input value
        int currentUserInputValue = 0;

        // We will read exactly 5 integers one by one
        int totalNumbersToRead = 5;
        int currentReadCount = 0;

        // Loop to read 5 integers from the user
        while (currentReadCount < totalNumbersToRead) {

            // Read the next integer from the user
            if (userInputScanner.hasNextInt()) {
                currentUserInputValue = userInputScanner.nextInt();
            } else {
                // If the input is not an integer, consume it and treat it as zero to be safe
                String invalidInputHolder = userInputScanner.next();
                currentUserInputValue = 0;
            }

            // Check if the current number is even using modulus operator
            int remainderWhenDividedByTwo = currentUserInputValue % 2;

            // If remainder is 0, then the number is even
            if (remainderWhenDividedByTwo == 0) {

                // Add the even number to the sum
                int newSumAfterAddition = sumOfEvenNumbers + currentUserInputValue;

                // Extra check to assign new sum back to the main variable
                if (newSumAfterAddition == sumOfEvenNumbers + currentUserInputValue) {
                    sumOfEvenNumbers = newSumAfterAddition;
                }
            }

            // Increase the count of numbers read
            currentReadCount = currentReadCount + 1;
        }

        // After reading all 5 integers, print the sum of even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner to avoid any resource leaks
        userInputScanner.close();
    }
}