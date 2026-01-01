import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user to enter 5 integers on the same line or in sequence
        System.out.print("Enter 5 integers: ");

        // Initialize the sum of even numbers to zero
        int sumOfEvenNumbers = 0;

        // We know we need to read exactly 5 integers
        int totalNumbersToRead = 5;

        // Use a loop to read 5 integers from the user
        for (int currentIndex = 0; currentIndex < totalNumbersToRead; currentIndex++) {

            // Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // We will use a formula to check if a number is even:
            // A number is even if currentUserInputValue % 2 == 0
            int remainderWhenDividedByTwo = currentUserInputValue % 2;

            // If the remainder is zero, then the number is even
            if (remainderWhenDividedByTwo == 0) {
                // Add this even number to our running sum
                sumOfEvenNumbers = sumOfEvenNumbers + currentUserInputValue;
            }
        }

        // After reading all 5 numbers, print the sum of only the even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner to free resources
        userInputScanner.close();
    }
}