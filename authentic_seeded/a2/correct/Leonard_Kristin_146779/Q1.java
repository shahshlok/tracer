import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Print prompt exactly as in the sample run
        System.out.print("Enter 5 integers: ");

        // Declare variable to store the running sum of even numbers
        int sumOfEvenNumbers = 0;

        // We will read exactly 5 integers, so we use a loop that runs 5 times
        for (int loopCounter = 0; loopCounter < 5; loopCounter++) {
            // Read the next integer from the user
            int userInputValue = userInputScanner.nextInt();

            // Declare intermediate math variable to check evenness
            int remainderWhenDividedByTwo = userInputValue % 2;

            // If the remainder when divided by 2 is zero, the number is even
            if (remainderWhenDividedByTwo == 0) {
                // Add this even number to the running sum
                sumOfEvenNumbers = sumOfEvenNumbers + userInputValue;
            }
        }

        // After processing all 5 integers, print the sum of even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}