import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user to enter 5 integers
        System.out.print("Enter 5 integers: ");

        // Create a variable to store the sum of even numbers
        int sumOfEvenNumbers = 0;

        // Repeat the same steps 5 times to read 5 integers
        for (int numberCount = 0; numberCount < 5; numberCount++) {
            // Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // Check if the current number is even
            if (currentUserInputValue % 2 == 0) {
                // Add the even number to the running total
                sumOfEvenNumbers = sumOfEvenNumbers + currentUserInputValue;
            }
        }

        // Print the final sum of even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner because we are done using it
        userInputScanner.close();
    }
}