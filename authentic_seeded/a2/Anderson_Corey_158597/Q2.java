import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Random object to generate the secret number
        Random randomNumberGenerator = new Random();

        // Step 2: Generate a random number between 1 and 100 (inclusive)
        int secretAnswerValue = randomNumberGenerator.nextInt(100) + 1;

        // Step 3: Create a Scanner object to read user input from the keyboard
        Scanner keyboardInputScanner = new Scanner(System.in);

        // Step 4: Create a variable to keep track of how many guesses the user has made
        int numberOfGuessesTaken = 0;

        // Step 5: Create a variable to store the user's current guess
        int currentUserGuessValue = 0;

        // Step 6: Keep looping until the user guesses the correct number
        while (currentUserGuessValue != secretAnswerValue) {
            // Step 7: Ask the user for a guess
            System.out.print("Guess a number (1-100): ");

            // Step 8: Read the user's guess from the keyboard
            currentUserGuessValue = keyboardInputScanner.nextInt();

            // Step 9: Increase the guess counter by 1 because the user just guessed
            numberOfGuessesTaken++;

            // Step 10: Check if the guess is too low, too high, or correct
            if (currentUserGuessValue < secretAnswerValue) {
                // Step 11: Tell the user their guess is too low
                System.out.println("Too low!");
            } else if (currentUserGuessValue > secretAnswerValue) {
                // Step 12: Tell the user their guess is too high
                System.out.println("Too high!");
            } else {
                // Step 13: The user guessed correctly, so print the success message
                System.out.println("Correct! You took " + numberOfGuessesTaken + " guesses.");
            }
        }

        // Step 14: Close the scanner because we are done with input
        keyboardInputScanner.close();
    }
}