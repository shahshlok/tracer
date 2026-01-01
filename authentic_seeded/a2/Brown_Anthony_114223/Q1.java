import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Tell the user to enter 5 integers on one line
        System.out.print("Enter 5 integers: ");

        // Step 3: Create a variable that will store the final sum after the loop
        int finalSumOfEvenNumbers = 0;

        // Step 4: Use a loop to read exactly 5 integers from the user
        for (int inputCount = 0; inputCount < 5; inputCount++) {
            // Step 5: Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // Step 6: Create a sum variable inside the loop to keep track of the sum safely
            int sumOfEvenNumbers = 0;

            // Step 7: Check if the current number is even by using the remainder operator
            if (currentUserInputValue % 2 == 0) {
                // Step 8: If the number is even, add it to the sum
                sumOfEvenNumbers = sumOfEvenNumbers + currentUserInputValue;
            }

            // Step 9: Store the sum from this step into the final sum variable
            finalSumOfEvenNumbers = sumOfEvenNumbers;
        }

        // Step 10: After reading all 5 integers, print the sum of the even numbers
        System.out.println("Sum of even numbers: " + finalSumOfEvenNumbers);

        // Step 11: Close the scanner because we are done with input
        userInputScanner.close();
    }
}