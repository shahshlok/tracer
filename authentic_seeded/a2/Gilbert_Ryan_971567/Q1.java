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

        // We will use a, b, c as intermediate math variables to check evenness using modulus formula
        int a;
        int b;
        int c;

        // Now we will loop through each of the 5 numbers using their positions
        int finalSumOfEvenNumbers = 0; // this will store the final result after the loop

        for (int positionIndex = 1; positionIndex <= 5; positionIndex = positionIndex + 1) {
            // For each position, we pick the correct user input value
            int currentUserInputValue;

            if (positionIndex == 1) {
                currentUserInputValue = firstUserInputValue;
            } else if (positionIndex == 2) {
                currentUserInputValue = secondUserInputValue;
            } else if (positionIndex == 3) {
                currentUserInputValue = thirdUserInputValue;
            } else if (positionIndex == 4) {
                currentUserInputValue = fourthUserInputValue;
            } else {
                currentUserInputValue = fifthUserInputValue;
            }

            // Check if the current number is even using the formula: number % 2 == 0
            a = currentUserInputValue;
            b = a % 2;

            // Declare the running sum inside the loop because we are calculating it each time here
            int sumOfEvenNumbers = 0;

            // If the number is even, we add it to the sum
            if (b == 0) {
                sumOfEvenNumbers = sumOfEvenNumbers + a;
            }

            // c will represent the final result of this iteration
            c = sumOfEvenNumbers;

            // We store c into the finalSumOfEvenNumbers so it has the latest result
            finalSumOfEvenNumbers = c;
        }

        // After the loop ends, finalSumOfEvenNumbers holds the sum of even numbers
        System.out.println("Sum of even numbers: " + finalSumOfEvenNumbers);

        // Close the scanner to avoid resource leak
        keyboardInput.close();
    }
}