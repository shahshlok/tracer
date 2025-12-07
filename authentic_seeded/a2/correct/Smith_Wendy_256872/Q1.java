import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter 5 integers
        System.out.print("Enter 5 integers: ");

        // Declare variable to store the running sum of even numbers
        int sumOfEvenNumbers = 0;

        // We will read exactly 5 integers from the user
        int numberOfValuesToRead = 5;

        // Loop exactly 5 times to read all integers
        for (int currentIndex = 0; currentIndex < numberOfValuesToRead; currentIndex++) {

            // Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // Declare an intermediate variable to hold the remainder when divided by 2
            int remainderWhenDividedByTwo = currentUserInputValue % 2;

            // If remainder is 0, the number is even
            if (remainderWhenDividedByTwo == 0) {
                // Add the even number to the running sum
                sumOfEvenNumbers = sumOfEvenNumbers + currentUserInputValue;
            }
        }

        // After reading all 5 integers, print the sum of even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}