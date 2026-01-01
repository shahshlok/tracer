import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user to enter 5 integers
        System.out.print("Enter 5 integers: ");

        // Variable to store the running sum of even numbers
        int sumOfEvenNumbers = 0;

        // We will read exactly 5 integers
        int numberOfInputsToRead = 5;

        // Loop exactly 5 times to read 5 integers
        for (int inputCounter = 0; inputCounter < numberOfInputsToRead; inputCounter++) {
            // Read the next integer from the user
            int userInputValue = userInputScanner.nextInt();

            // We will use a formula with modulo to check if the number is even
            // Let a be the user input value
            int a = userInputValue;
            // Let b be a % 2, the remainder when divided by 2
            int b = a % 2;

            // If the remainder b is 0, then the number is even
            if (b == 0) {
                // Add this even number to our running sum
                sumOfEvenNumbers = sumOfEvenNumbers + a;
            }
        }

        // Print the final sum of even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner to free resources
        userInputScanner.close();
    }
}