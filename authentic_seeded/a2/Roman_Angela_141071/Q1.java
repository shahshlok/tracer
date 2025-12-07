import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter 5 integers
        System.out.print("Enter 5 integers: ");

        // Declare a variable to store the running sum of even numbers
        int sumOfEvenNumbers = 0;

        // We will read exactly 5 integers using a loop
        for (int integerIndex = 0; integerIndex < 5; integerIndex++) {
            // Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // Declare an intermediate variable to compute remainder when divided by 2
            int remainderWhenDividedByTwo = currentUserInputValue % 2;

            // If the remainder is 0, then the number is even
            if (remainderWhenDividedByTwo == 0) {
                // Add this even number to our running sum
                sumOfEvenNumbers = sumOfEvenNumbers + currentUserInputValue;
            }
        }

        // After reading all 5 integers, print the sum of the even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner
        userInputScanner.close();
    }
}