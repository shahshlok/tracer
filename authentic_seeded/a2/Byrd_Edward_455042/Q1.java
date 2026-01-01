import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user to enter 5 integers on one line
        System.out.print("Enter 5 integers: ");

        // Initialize the running sum of even numbers
        int sumOfEvenNumbers = 0;

        // We will read exactly 5 integers from the user
        int numberOfInputsToRead = 5;

        // Declare some math variables to structure the logic
        int a = 0;
        int b = 0;
        int c = 0;

        // Loop to read the integers, starting from 1 and going while less than 5
        // I want 5 numbers total, so this loop condition will control the count
        for (int currentInputIndex = 1; currentInputIndex < numberOfInputsToRead; currentInputIndex++) {
            // Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // Use a variable to hold the remainder when dividing by 2
            a = currentUserInputValue;
            b = 2;
            c = a % b;
            int remainderWhenDividedByTwo = c;

            // If the remainder is 0, the number is even
            if (remainderWhenDividedByTwo == 0) {
                // Add the even number to the running sum using a formula-like step
                a = sumOfEvenNumbers;
                b = currentUserInputValue;
                c = a + b;
                sumOfEvenNumbers = c;
            }
        }

        // After reading all the integers we controlled with the loop, print the sum of the even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner (good practice)
        userInputScanner.close();
    }
}