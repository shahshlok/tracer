import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user to enter 5 integers on one line
        System.out.print("Enter 5 integers: ");

        // This variable will store the running sum of even numbers
        int evenNumbersSum = 0;

        // Loop exactly 5 times because we need 5 integers
        int totalNumbersToRead = 5;
        int currentLoopIndex = 0;
        while (currentLoopIndex < totalNumbersToRead) {
            // Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // Check if the number is even using modulus operator
            int remainderWhenDividedByTwo = currentUserInputValue % 2;

            // If the remainder is 0, then the number is even
            if (remainderWhenDividedByTwo == 0) {
                // Add the even number to the sum
                int temporaryNewSum = evenNumbersSum + currentUserInputValue;
                evenNumbersSum = temporaryNewSum;
            }

            // Move to the next position in the loop
            currentLoopIndex = currentLoopIndex + 1;
        }

        // After reading 5 integers and summing the even ones, print the result
        System.out.println("Sum of even numbers: " + evenNumbersSum);

        // Close the scanner to be polite, even though the program is ending
        if (userInputScanner != null) {
            userInputScanner.close();
        }
    }
}