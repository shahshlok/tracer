import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter 5 integers
        System.out.print("Enter 5 integers: ");

        // Declare a variable to hold the running sum of all even numbers
        int sumOfEvenNumbers = 0;

        // We will loop exactly 5 times to read 5 integers
        for (int inputCounter = 0; inputCounter < 5; inputCounter++) {

            // Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // We will use a variable to store the remainder when divided by 2
            int remainderWhenDividedByTwo = currentUserInputValue % 2;

            // If the remainder is 0, then the number is even
            if (remainderWhenDividedByTwo == 0) {
                // Add the even number to our running sum
                sumOfEvenNumbers = sumOfEvenNumbers + currentUserInputValue;
            }
        }

        // After reading 5 integers and summing the even ones, print the result
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}