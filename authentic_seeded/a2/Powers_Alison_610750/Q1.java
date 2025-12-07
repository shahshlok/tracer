import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Print prompt exactly as in the sample run
        System.out.print("Enter 5 integers: ");

        // Declare a variable to hold the running sum of even numbers
        int sumOfEvenNumbers = 0;

        // We will read exactly 5 integers, so use a loop that runs 5 times
        for (int loopCounter = 0; loopCounter < 5; loopCounter++) {
            // Read the next integer from the user
            int userInputValue = userInputScanner.nextInt();

            // Declare intermediate math variables to help with the even test
            int a = userInputValue;          // a holds the current number
            int b = 2;                       // b is the divisor for checking evenness
            int c = a % b;                   // c is the remainder when a is divided by b

            // If the remainder c is 0, then the number is even
            if (c == 0) {
                // Add this even number to the running sum
                sumOfEvenNumbers = sumOfEvenNumbers + a;
            }
        }

        // Print the final sum of even numbers exactly as in the sample run
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}