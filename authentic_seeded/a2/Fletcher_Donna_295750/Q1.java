import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read user input from the keyboard
        Scanner keyboardScanner = new Scanner(System.in);

        // Step 2: Tell the user to enter 5 integers on the same line
        System.out.print("Enter 5 integers: ");

        // Step 3: Create a variable to store the running total of even numbers
        int sumOfEvenNumbers = 0;

        // Step 4: Read 5 integers one by one and process each one
        for (int currentIndex = 0; currentIndex < 5; currentIndex++) {
            // Step 4a: Read the next integer from the user
            int currentUserInputValue = keyboardScanner.nextInt();

            // Step 4b: Check if the current number is even
            if (currentUserInputValue % 2 == 0) {
                // Step 4c: Add the even number to the running total
                sumOfEvenNumbers = sumOfEvenNumbers + currentUserInputValue;
            }
        }

        // Step 5: After reading all 5 integers, print the sum of even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Step 6: Close the scanner because we are done with input
        keyboardScanner.close();
    }
}