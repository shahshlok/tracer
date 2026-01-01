import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Create a variable to keep track of how many integers we need to read
        int totalNumberOfInputsRequired = 5;

        // Create a variable to store the running sum of even numbers
        int runningSumOfEvenNumbers = 0;

        // Prompt the user to enter 5 integers, just like in the sample run
        System.out.print("Enter 5 integers: ");

        // Use a loop to read exactly 5 integers from the user
        int currentLoopIndex = 0;
        while (currentLoopIndex < totalNumberOfInputsRequired) {
            // Read the next integer from the user
            int currentUserInputInteger = userInputScanner.nextInt();

            // Check if the number is even
            int remainderWhenDividedByTwo = currentUserInputInteger % 2;
            if (remainderWhenDividedByTwo == 0) {
                // The number is even, so we add it to our running sum
                int temporaryNewSum = runningSumOfEvenNumbers + currentUserInputInteger;
                runningSumOfEvenNumbers = temporaryNewSum;
            }

            // Move to the next iteration of the loop
            currentLoopIndex = currentLoopIndex + 1;
        }

        // After reading all 5 integers, print the sum of the even numbers
        System.out.println();
        System.out.println("Sum of even numbers: " + runningSumOfEvenNumbers);

        // Close the scanner to be safe, even though the program is ending
        if (userInputScanner != null) {
            userInputScanner.close();
        }
    }
}