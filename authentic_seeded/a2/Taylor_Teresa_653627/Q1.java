import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user to enter 5 integers on one line
        System.out.print("Enter 5 integers: ");

        // Create a variable to keep track of the sum of even numbers
        int sumOfEvenNumbers = 0;

        // Step 1: Read the first integer and check if it is even
        int firstInputValue = userInputScanner.nextInt();
        if (firstInputValue % 2 == 0) {
            // If the number is even, add it to sumOfEvenNumbers
            sumOfEvenNumbers = sumOfEvenNumbers + firstInputValue;
        }

        // Step 2: Read the second integer and check if it is even
        int secondInputValue = userInputScanner.nextInt();
        if (secondInputValue % 2 == 0) {
            // If the number is even, add it to sumOfEvenNumbers
            sumOfEvenNumbers = sumOfEvenNumbers + secondInputValue;
        }

        // Step 3: Read the third integer and check if it is even
        int thirdInputValue = userInputScanner.nextInt();
        if (thirdInputValue % 2 == 0) {
            // If the number is even, add it to sumOfEvenNumbers
            sumOfEvenNumbers = sumOfEvenNumbers + thirdInputValue;
        }

        // Step 4: Read the fourth integer and check if it is even
        int fourthInputValue = userInputScanner.nextInt();
        if (fourthInputValue % 2 == 0) {
            // If the number is even, add it to sumOfEvenNumbers
            sumOfEvenNumbers = sumOfEvenNumbers + fourthInputValue;
        }

        // Step 5: Read the fifth integer and check if it is even
        int fifthInputValue = userInputScanner.nextInt();
        if (fifthInputValue % 2 == 0) {
            // If the number is even, add it to sumOfEvenNumbers
            sumOfEvenNumbers = sumOfEvenNumbers + fifthInputValue;
        }

        // After reading all 5 integers and updating the sum, print the result
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the Scanner because we are done with user input
        userInputScanner.close();
    }
}