import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user to enter 5 integers
        System.out.print("Enter 5 integers: ");

        // Create a variable to hold the sum of even numbers
        int sumOfEvenNumbers = 0;

        // Repeat 5 times to read 5 integers
        for (int currentIndex = 0; currentIndex < 5; currentIndex++) {
            // Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // Check if the current number is even
            if (currentUserInputValue % 2 == 0) {
                // If it is even, add it to the sum
                sumOfEvenNumbers = sumOfEvenNumbers + currentUserInputValue;
            }
        }

        // After reading all 5 integers, print the sum of even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}