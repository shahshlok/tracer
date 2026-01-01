import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user to enter 5 integers on one line or multiple lines
        System.out.print("Enter 5 integers: ");

        // Variable to store the running sum of even numbers
        int sumOfEvenNumbers = 0;

        // We will read exactly 5 integers using a loop
        for (int currentIndex = 0; currentIndex < 5; currentIndex++) {
            // Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // Declare intermediate math variable to check evenness
            int remainderWhenDividedByTwo = currentUserInputValue % 2;

            // If the remainder is zero, the number is even
            if (remainderWhenDividedByTwo == 0) {
                // Add this even number to the running sum
                sumOfEvenNumbers = sumOfEvenNumbers + currentUserInputValue;
            }
        }

        // Print the final sum of the even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}