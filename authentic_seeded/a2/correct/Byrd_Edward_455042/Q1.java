import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user to enter 5 integers on one line
        System.out.print("Enter 5 integers: ");

        // Initialize the running sum of even numbers
        int sumOfEvenNumbers = 0;

        // We will read exactly 5 integers from the user
        int numberOfInputsToRead = 5;

        // Loop exactly 5 times to read 5 integers
        for (int currentInputIndex = 1; currentInputIndex <= numberOfInputsToRead; currentInputIndex++) {
            // Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // Use a variable to hold the remainder when dividing by 2
            int remainderWhenDividedByTwo = currentUserInputValue % 2;

            // If the remainder is 0, the number is even
            if (remainderWhenDividedByTwo == 0) {
                // Add the even number to the running sum
                sumOfEvenNumbers = sumOfEvenNumbers + currentUserInputValue;
            }
        }

        // After reading all 5 integers, print the sum of the even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner (good practice)
        userInputScanner.close();
    }
}