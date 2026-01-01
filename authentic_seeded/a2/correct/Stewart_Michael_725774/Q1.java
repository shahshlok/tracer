import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter 5 integers
        System.out.print("Enter 5 integers: ");

        // Declare a variable to hold the running sum of even numbers
        int sumOfEvenNumbers = 0;

        // Loop exactly 5 times to read 5 integers
        for (int loopCounter = 0; loopCounter < 5; loopCounter++) {

            // Read the next integer from the user
            int userInputValue = userInputScanner.nextInt();

            // Declare an intermediate variable to hold the remainder when dividing by 2
            int remainderWhenDividedByTwo = userInputValue % 2;

            // If the remainder is 0, the number is even
            if (remainderWhenDividedByTwo == 0) {

                // Add the even number to the running sum
                sumOfEvenNumbers = sumOfEvenNumbers + userInputValue;
            }
        }

        // Print the final sum of even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}