import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Create a variable to keep track of the sum of even numbers
        int sumOfEvenNumbers = 0;

        // Step 3: Tell the user to enter 5 integers on one line
        System.out.print("Enter 5 integers: ");

        // Step 4: Use a loop to read exactly 5 integers from the user
        for (int inputCount = 0; inputCount < 5; inputCount++) {
            // Step 5: Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // Step 6: Check if the current number is even by using the remainder operator
            if (currentUserInputValue % 2 == 0) {
                // Step 7: If the number is even, add it to the sum
                sumOfEvenNumbers = sumOfEvenNumbers + currentUserInputValue;
            }
        }

        // Step 8: After reading all 5 integers, print the sum of the even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Step 9: Close the scanner because we are done with input
        userInputScanner.close();
    }
}