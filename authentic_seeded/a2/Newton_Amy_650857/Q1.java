import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner object so we can read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Tell the user to enter 5 integers on one line
        System.out.print("Enter 5 integers: ");

        // Step 3: Create a variable to keep track of the sum of even numbers
        int sumOfEvenNumbers = 0;

        // Step 4: Read 5 integers one by one and update the sum if the number is even
        for (int numberCount = 0; numberCount < 5; numberCount++) {
            // Read the next integer from the user
            int userInputValue = userInputScanner.nextInt();

            // Check if the current number is even
            if (userInputValue % 2 == 0) {
                // If it is even, add it to the running sum
                sumOfEvenNumbers = sumOfEvenNumbers + userInputValue;
            }
        }

        // Step 5: After reading all 5 numbers, print the sum of even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Step 6: Close the scanner because we are done with input
        userInputScanner.close();
    }
}