import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user to enter 5 integers, just like in the sample run
        System.out.print("Enter 5 integers: ");

        // This variable will hold the running sum of even numbers
        int sumOfEvenNumbers = 0;

        // We know we must read exactly 5 integers
        int totalNumbersToRead = 5;

        // Use a loop counter to make sure we read exactly 5 integers
        int loopCounter = 0;

        // Loop exactly 5 times to read 5 integers from the user
        while (loopCounter < totalNumbersToRead) {
            // Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // Use a temporary holder to check if the number is even
            int remainderWhenDividedByTwo = currentUserInputValue % 2;

            // If the remainder is 0, then the number is even
            if (remainderWhenDividedByTwo == 0) {
                // It is even, so we add it to the sum
                int newSumOfEvenNumbers = sumOfEvenNumbers + currentUserInputValue;

                // Update the main sum variable with the new sum
                sumOfEvenNumbers = newSumOfEvenNumbers;
            }

            // Increase the loop counter so that we eventually stop after 5 numbers
            int newLoopCounterValue = loopCounter + 1;
            loopCounter = newLoopCounterValue;
        }

        // After reading all 5 integers and summing the even ones, print the result
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the Scanner to be safe, even though the program is about to end
        userInputScanner.close();
    }
}