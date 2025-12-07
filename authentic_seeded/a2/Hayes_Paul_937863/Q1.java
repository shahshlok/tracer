import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user to enter 5 integers, just like in the sample run
        System.out.print("Enter 5 integers: ");

        // Declare variables to store each of the 5 integers the user will type
        int firstUserInputValue;
        int secondUserInputValue;
        int thirdUserInputValue;
        int fourthUserInputValue;
        int fifthUserInputValue;

        // Read the 5 integers from the user
        firstUserInputValue = userInputScanner.nextInt();
        secondUserInputValue = userInputScanner.nextInt();
        thirdUserInputValue = userInputScanner.nextInt();
        fourthUserInputValue = userInputScanner.nextInt();
        fifthUserInputValue = userInputScanner.nextInt();

        // Declare a variable that will store the running sum of even numbers
        int sumOfEvenNumbers = 0;

        // For each number, we will check if it is even using the modulus formula:
        // if (number % 2 == 0), then the number is even.

        // Check the first number
        int a = firstUserInputValue % 2; // remainder when divided by 2
        if (a == 0) {
            sumOfEvenNumbers = sumOfEvenNumbers + firstUserInputValue;
        }

        // Check the second number
        int b = secondUserInputValue % 2;
        if (b == 0) {
            sumOfEvenNumbers = sumOfEvenNumbers + secondUserInputValue;
        }

        // Check the third number
        int c = thirdUserInputValue % 2;
        if (c == 0) {
            sumOfEvenNumbers = sumOfEvenNumbers + thirdUserInputValue;
        }

        // Check the fourth number
        int d = fourthUserInputValue % 2;
        if (d == 0) {
            sumOfEvenNumbers = sumOfEvenNumbers + fourthUserInputValue;
        }

        // Check the fifth number
        int e = fifthUserInputValue % 2;
        if (e == 0) {
            sumOfEvenNumbers = sumOfEvenNumbers + fifthUserInputValue;
        }

        // Now print the final sum of all even numbers entered by the user
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner to be polite with system resources
        userInputScanner.close();
    }
}