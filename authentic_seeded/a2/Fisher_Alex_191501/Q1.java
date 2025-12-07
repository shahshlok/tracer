import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Variable to hold the total sum of even numbers
        int totalEvenSum = 0;

        // Variable to hold the current integer entered by the user
        int currentUserInteger = 0;

        // Ask the user to enter 5 integers, all on one line or separated by spaces/newlines
        System.out.print("Enter 5 integers: ");

        // Loop exactly 5 times because the assignment says to read 5 integers
        int numberOfInputsToRead = 5;
        int currentLoopIndex = 0;
        while (currentLoopIndex < numberOfInputsToRead) {
            // Read the next integer from the user
            if (userInputScanner.hasNextInt()) {
                currentUserInteger = userInputScanner.nextInt();

                // Check if the current integer is even
                int remainderWhenDividedByTwo = currentUserInteger % 2;
                if (remainderWhenDividedByTwo == 0) {
                    // Add even number to the total sum
                    int temporaryNewSum = totalEvenSum + currentUserInteger;
                    totalEvenSum = temporaryNewSum;
                }
            } else {
                // If the next token is not an integer, consume it and skip
                String invalidInputToken = userInputScanner.next();
                // Since this is unexpected, we will not increment the loop counter here
                // This way we still try to read exactly 5 integers
                continue;
            }

            // Increment the loop index after successfully processing an integer
            currentLoopIndex = currentLoopIndex + 1;
        }

        // Print the sum of even numbers
        System.out.println("Sum of even numbers: " + totalEvenSum);

        // Close the scanner (even though the program is ending, just to be safe)
        userInputScanner.close();
    }
}