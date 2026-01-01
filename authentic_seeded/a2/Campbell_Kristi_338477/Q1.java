import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Print a prompt message to the user
        System.out.print("Enter 5 integers: ");

        // Declare a variable to store the running sum of even numbers
        int sumOfEvenNumbers = 0;

        // We know we need to read exactly 5 integers
        int totalNumbersToRead = 5;

        // Use a loop to read 5 integers from the user
        for (int loopCounter = 0; loopCounter < totalNumbersToRead; loopCounter++) {
            // Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // Use math to check if the number is even
            int remainderWhenDividedByTwo = currentUserInputValue % 2;

            // If the remainder is 0, the number is even
            if (remainderWhenDividedByTwo == 0) {
                // Add the even number to our running sum
                sumOfEvenNumbers = sumOfEvenNumbers + currentUserInputValue;
            }
        }

        // Print the final sum of even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner to be polite with system resources
        userInputScanner.close();
    }
}