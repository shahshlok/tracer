import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter 5 integers
        System.out.print("Enter 5 integers: ");

        // Declare a variable to hold the running sum of even numbers
        int sumOfEvenNumbers = 0;

        // We know we need exactly 5 integers, so we loop 5 times
        for (int loopCounter = 0; loopCounter < 5; loopCounter++) {
            // Read the next integer from the user
            int userInputValue = userInputScanner.nextInt();

            // Declare intermediate math variable for checking evenness
            int remainderWhenDividedByTwo = userInputValue % 2;

            // If the remainder is 0, the number is even
            if (remainderWhenDividedByTwo == 0) {
                // Add the even number to the running total
                int newSumOfEvenNumbers = sumOfEvenNumbers + userInputValue;
                sumOfEvenNumbers = newSumOfEvenNumbers;
            }
        }

        // Print the final sum of even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}