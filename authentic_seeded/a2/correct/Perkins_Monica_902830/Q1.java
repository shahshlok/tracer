import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Print prompt for the user to enter 5 integers
        System.out.print("Enter 5 integers: ");

        // Create a variable to hold the running sum of even numbers
        int sumOfEvenNumbers = 0;

        // Create a temporary variable to hold each input value
        int currentUserInputValue = 0;

        // We know we need exactly 5 integers, so we loop 5 times
        int numberOfInputsRequired = 5;
        int currentInputCount = 0;

        // Loop until we have read 5 integers
        while (currentInputCount < numberOfInputsRequired) {
            // Read the next integer from the user
            if (userInputScanner.hasNextInt()) {
                currentUserInputValue = userInputScanner.nextInt();

                // Check whether the current number is even
                int remainderWhenDividedByTwo = currentUserInputValue % 2;

                // If the remainder is zero, the number is even
                if (remainderWhenDividedByTwo == 0) {
                    // Add the even number to the running sum
                    int newSumOfEvenNumbers = sumOfEvenNumbers + currentUserInputValue;
                    sumOfEvenNumbers = newSumOfEvenNumbers;
                }

                // Increase the count of how many numbers we have read
                currentInputCount = currentInputCount + 1;
            } else {
                // If the next token is not an integer, consume it and continue
                String invalidInputHolder = userInputScanner.next();
                // We are nervous about invalid input, but we just skip it
            }
        }

        // Print the final sum of even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner (even though the program is about to end)
        userInputScanner.close();
    }
}