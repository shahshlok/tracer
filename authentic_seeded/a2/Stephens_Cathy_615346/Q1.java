import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Tell the user to enter 5 integers on the same line
        System.out.print("Enter 5 integers: ");

        // Step 3: Create a variable to hold the running total of even numbers
        int sumOfEvenNumbers = 0;

        // Step 4: Read 5 integers one by one and add the even ones to the sum
        for (int integerCounter = 1; integerCounter < 5; integerCounter++) {
            // Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // Check if the current number is even
            if (currentUserInputValue % 2 == 0) {
                // If it is even, add it to the sum
                sumOfEvenNumbers = sumOfEvenNumbers + currentUserInputValue;
            }
        }

        // Step 5: Print the final sum of even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Step 6: Close the scanner because we are done with input
        userInputScanner.close();
    }
}