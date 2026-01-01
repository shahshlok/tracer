import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Random object to generate the secret number
        Random randomNumberGenerator = new Random();

        // Create a Scanner object to read user guesses from the keyboard
        Scanner keyboardInputScanner = new Scanner(System.in);

        // Generate the secret number between 1 and 100 (inclusive)
        int secretAnswerValue = randomNumberGenerator.nextInt(100) + 1;

        // Declare a variable to store the user's current guess
        int userGuessValue = 0;

        // Declare a variable to count how many guesses the user has taken
        int numberOfGuessesTaken = 0;

        // Loop until the userGuessValue equals the secretAnswerValue
        while (userGuessValue != secretAnswerValue) {
            // Prompt the user to guess a number between 1 and 100
            System.out.print("Guess a number (1-100): ");

            // Read the user's guess from the keyboard
            userGuessValue = keyboardInputScanner.nextInt();

            // Each time the user enters a guess, increment the guess counter
            numberOfGuessesTaken = numberOfGuessesTaken + 1;

            // Calculate the difference between guess and answer as a math-style step
            int a = userGuessValue;
            int b = secretAnswerValue;
            int c = a - b; // c will be positive if guess is too high, negative if too low, zero if correct

            // Check whether the guess is too high, too low, or correct
            if (c > 0) {
                // The guess is greater than the secret number
                System.out.println("Too high!");
            } else if (c < 0) {
                // The guess is less than the secret number
                System.out.println("Too low!");
            } else {
                // The guess is exactly equal to the secret number
                System.out.println("Correct! You took " + numberOfGuessesTaken + " guesses.");
            }
        }

        // Close the scanner to free system resources
        keyboardInputScanner.close();
    }
}