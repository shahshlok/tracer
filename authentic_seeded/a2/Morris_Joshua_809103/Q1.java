import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user to enter 5 integers on the same line
        System.out.print("Enter 5 integers: ");

        // Create a variable to keep track of the sum of even numbers
        int sumOfEvenNumbers = 0;

        // Step 1: Read the first integer and check if it is even, then add if it is
        int firstUserInputValue = userInputScanner.nextInt();
        if (firstUserInputValue % 2 == 0) {
            sumOfEvenNumbers = sumOfEvenNumbers + firstUserInputValue;
        }

        // Step 2: Read the second integer and check if it is even, then add if it is
        int secondUserInputValue = userInputScanner.nextInt();
        if (secondUserInputValue % 2 == 0) {
            sumOfEvenNumbers = sumOfEvenNumbers + secondUserInputValue;
        }

        // Step 3: Read the third integer and check if it is even, then add if it is
        int thirdUserInputValue = userInputScanner.nextInt();
        if (thirdUserInputValue % 2 == 0) {
            sumOfEvenNumbers = sumOfEvenNumbers + thirdUserInputValue;
        }

        // Step 4: Read the fourth integer and check if it is even, then add if it is
        int fourthUserInputValue = userInputScanner.nextInt();
        if (fourthUserInputValue % 2 == 0) {
            sumOfEvenNumbers = sumOfEvenNumbers + fourthUserInputValue;
        }

        // Step 5: Read the fifth integer and check if it is even, then add if it is
        int fifthUserInputValue = userInputScanner.nextInt();
        if (fifthUserInputValue % 2 == 0) {
            sumOfEvenNumbers = sumOfEvenNumbers + fifthUserInputValue;
        }

        // After reading all 5 integers and summing the even ones, print the result
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner because we are done with input
        userInputScanner.close();
    }
}