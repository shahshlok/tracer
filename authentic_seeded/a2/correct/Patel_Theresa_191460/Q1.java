import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user what to enter
        System.out.print("Enter 5 integers: ");

        // Variable to hold the sum of even numbers
        int sumOfEvenNumbers = 0;

        // We know we need exactly 5 integers, so we will loop 5 times
        int numberOfValuesToRead = 5;
        int currentLoopIndex = 0;

        // Loop to read each integer
        while (currentLoopIndex < numberOfValuesToRead) {
            // Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // Check if the current number is even
            int remainderWhenDividedByTwo = currentUserInputValue % 2;
            if (remainderWhenDividedByTwo == 0) {
                // If it is even, add it to the sum
                int newSumOfEvenNumbers = sumOfEvenNumbers + currentUserInputValue;
                sumOfEvenNumbers = newSumOfEvenNumbers;
            }

            // Move to the next loop iteration
            int nextLoopIndex = currentLoopIndex + 1;
            currentLoopIndex = nextLoopIndex;
        }

        // After processing all numbers, print the sum of even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner to be safe
        if (userInputScanner != null) {
            userInputScanner.close();
        }
    }
}