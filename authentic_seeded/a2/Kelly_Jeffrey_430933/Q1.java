import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Tell the user to enter 5 integers all on one line
        System.out.print("Enter 5 integers: ");

        // Step 3: Make a variable to hold the running total of even numbers
        int sumOfEvenNumbers = 0;

        // Step 4: Read exactly 5 integers, one after another
        for (int inputCount = 0; inputCount < 5; inputCount++) {
            // Step 4a: Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // Step 4b: Check if the number is even (remainder when divided by 2 is 0)
            if (currentUserInputValue % 2 == 0) {
                // Step 4c: Add the even number to our running total
                sumOfEvenNumbers = sumOfEvenNumbers + currentUserInputValue;
            }
        }

        // Step 5: Print the sum of the even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Step 6: Close the scanner because we are done reading input
        userInputScanner.close();
    }
}