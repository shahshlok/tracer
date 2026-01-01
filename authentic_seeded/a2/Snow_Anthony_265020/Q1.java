import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read user input from the keyboard
        Scanner keyboardInputScanner = new Scanner(System.in);

        // Step 2: Tell the user to enter 5 integers
        System.out.print("Enter 5 integers: ");

        // Step 3: Create a variable to keep track of the sum of even numbers
        int sumOfEvenNumbers = 0;

        // Step 4: Read 5 integers one by one and check if each one is even
        for (int integerCounter = 0; integerCounter < 5; integerCounter++) {
            // Step 4a: Read the next integer from the user
            int userInputValue = keyboardInputScanner.nextInt();

            // Step 4b: Check if this number is even by using the remainder operator
            if (userInputValue % 2 == 0) {
                // Step 4c: If the number is even, add it to the sum
                sumOfEvenNumbers = sumOfEvenNumbers + userInputValue;
            }
        }

        // Step 5: Print the final sum of the even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Step 6: Close the scanner because we are done with input
        keyboardInputScanner.close();
    }
}