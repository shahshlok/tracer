import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Tell the user to enter 5 integers on the same line
        System.out.print("Enter 5 integers: ");

        // Step 3: Create a variable to keep track of the sum of even numbers
        int sumOfEvenNumbers = 0;

        // Step 4: Use a loop to read exactly 5 integers
        for (int numberCount = 0; numberCount < 5; numberCount++) {
            // Step 5: Read the next integer from the user
            int userInputValue = userInputScanner.nextInt();

            // Step 6: Check if this number is even
            if (userInputValue % 2 == 0) {
                // Step 7: If it is even, add it to the sum
                sumOfEvenNumbers = sumOfEvenNumbers + userInputValue;
            }
        }

        // Step 8: Print the final sum of the even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Step 9: Close the scanner
        userInputScanner.close();
    }
}