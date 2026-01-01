import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Tell the user to enter 5 integers on one line
        System.out.print("Enter 5 integers: ");

        // Step 3: Create a variable to store the sum of even numbers
        int sumOfEvenNumbers = 0;

        // Step 4: Repeat 5 times to read 5 integers
        for (int currentIndex = 0; currentIndex < 5; currentIndex++) {
            // Step 5: Read the next integer from the user
            int currentUserInputValue = userInputScanner.nextInt();

            // Step 6: Check if the current number is even
            if (currentUserInputValue % 2 == 0) {
                // Step 7: If it is even, add it to the sum
                sumOfEvenNumbers = sumOfEvenNumbers + currentUserInputValue;
            }
        }

        // Step 8: Print the final sum of the even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Step 9: Close the scanner because we are done with input
        userInputScanner.close();
    }
}