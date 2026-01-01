import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user to enter 5 integers on one line or multiple lines
        System.out.print("Enter 5 integers: ");

        // We will read exactly 5 integers using a loop
        int finalSumOfEvenNumbers = 0;

        for (int currentIndex = 0; currentIndex < 5; currentIndex++) {
            // Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // Declare intermediate math variable to check evenness
            int remainderWhenDividedByTwo = currentUserInputValue % 2;

            // If the remainder is zero, the number is even
            if (remainderWhenDividedByTwo == 0) {
                // Start a new sum for this even number
                int sumOfEvenNumbers = 0;

                // Add this even number to the sum
                sumOfEvenNumbers = sumOfEvenNumbers + currentUserInputValue;

                // Store this sum so we can print it after the loop
                finalSumOfEvenNumbers = sumOfEvenNumbers;
            }
        }

        // Print the final sum of the even numbers
        System.out.println("Sum of even numbers: " + finalSumOfEvenNumbers);

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}