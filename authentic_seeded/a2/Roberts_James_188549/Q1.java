import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user to enter 5 integers
        System.out.print("Enter 5 integers: ");

        // Initialize a variable to store the running sum of even numbers
        int sumOfEvenNumbers = 0;

        // We know we need exactly 5 integers, so we loop 5 times
        int totalNumbersToRead = 5;
        int loopCounter = 0;

        // Use a while loop to carefully read exactly 5 integers
        while (loopCounter < totalNumbersToRead) {
            // Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // Check if the current number is even
            int remainderWhenDividedByTwo = currentUserInputValue % 2;

            // If the remainder is zero, the number is even
            if (remainderWhenDividedByTwo == 0) {
                // Add the even number to the running sum
                int newSumCandidate = sumOfEvenNumbers + currentUserInputValue;

                // Extra nervous check: we actually update the sum only through this variable
                sumOfEvenNumbers = newSumCandidate;
            }

            // Increase the loop counter to avoid an infinite loop
            loopCounter = loopCounter + 1;
        }

        // After reading 5 integers and summing the evens, print the final result
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner to be polite and avoid resource leaks
        userInputScanner.close();
    }
}