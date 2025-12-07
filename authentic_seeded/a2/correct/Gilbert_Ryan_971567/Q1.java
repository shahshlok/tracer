import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner keyboardInput = new Scanner(System.in);

        // Tell the user to enter 5 integers on one line
        System.out.print("Enter 5 integers: ");

        // Declare variables to store each user input value
        int firstUserInputValue;
        int secondUserInputValue;
        int thirdUserInputValue;
        int fourthUserInputValue;
        int fifthUserInputValue;

        // Read 5 integers from the user
        firstUserInputValue = keyboardInput.nextInt();
        secondUserInputValue = keyboardInput.nextInt();
        thirdUserInputValue = keyboardInput.nextInt();
        fourthUserInputValue = keyboardInput.nextInt();
        fifthUserInputValue = keyboardInput.nextInt();

        // Declare a variable to hold the running sum of even numbers
        int sumOfEvenNumbers = 0;

        // We will use a, b, c as intermediate math variables to check evenness using modulus formula
        int a;
        int b;
        int c;

        // Check if the first number is even using the formula: number % 2 == 0
        a = firstUserInputValue;
        b = a % 2;
        if (b == 0) {
            sumOfEvenNumbers = sumOfEvenNumbers + a;
        }

        // Check if the second number is even
        a = secondUserInputValue;
        b = a % 2;
        if (b == 0) {
            sumOfEvenNumbers = sumOfEvenNumbers + a;
        }

        // Check if the third number is even
        a = thirdUserInputValue;
        b = a % 2;
        if (b == 0) {
            sumOfEvenNumbers = sumOfEvenNumbers + a;
        }

        // Check if the fourth number is even
        a = fourthUserInputValue;
        b = a % 2;
        if (b == 0) {
            sumOfEvenNumbers = sumOfEvenNumbers + a;
        }

        // Check if the fifth number is even
        a = fifthUserInputValue;
        b = a % 2;
        if (b == 0) {
            sumOfEvenNumbers = sumOfEvenNumbers + a;
        }

        // c can represent the final result after all the math steps
        c = sumOfEvenNumbers;

        // Print the sum of even numbers in the required format
        System.out.println("Sum of even numbers: " + c);

        // Close the scanner to avoid resource leak
        keyboardInput.close();
    }
}