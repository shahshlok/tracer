import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user to enter 5 integers on one line
        System.out.print("Enter 5 integers: ");

        // Create a variable to hold the running sum of even numbers
        int sumOfEvenNumbers = 0;

        // We will read exactly 5 integers, one after another
        for (int integerCounter = 0; integerCounter < 5; integerCounter++) {
            // Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // Check if the current number is even using the remainder operator
            if (currentUserInputValue % 2 == 0) {
                // If it is even, add it to the sum of even numbers
                sumOfEvenNumbers = sumOfEvenNumbers + currentUserInputValue;
            }
        }

        // After reading all 5 integers, print the sum of the even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the Scanner because we are done with input
        userInputScanner.close();
    }
}