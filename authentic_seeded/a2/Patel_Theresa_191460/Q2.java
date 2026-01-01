import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Create a Random object to generate random numbers
        Random randomNumberGenerator = new Random();

        // Generate the secret answer number between 1 and 100, inclusive
        int secretAnswerNumber = randomNumberGenerator.nextInt(100) + 1;

        // Variable to hold the current user guess
        int currentUserGuess = 0;

        // Variable to count how many guesses the user has taken
        int totalGuessCount = 0;

        // We will keep looping until the user guesses the correct number
        boolean hasUserGuessedCorrectly = false;

        // Main guessing loop
        while (!hasUserGuessedCorrectly) {
            // Prompt the user for a guess
            System.out.print("Guess a number (1-100): ");

            // Read the user guess from input
            int temporaryInputHolder = userInputScanner.nextInt();
            currentUserGuess = temporaryInputHolder;

            // Increase the guess counter
            totalGuessCount = totalGuessCount + 1;

            // Check that the input is within the expected range (1 to 100)
            // Even though the question does not say to validate, we are nervous about edge cases
            if (currentUserGuess < 1 || currentUserGuess > 100) {
                // Tell the user the guess is out of range and continue the loop
                System.out.println("Please enter a number between 1 and 100.");
                // We do not want to give high/low hints here since the guess is invalid
                continue;
            }

            // Compare the guess to the secret answer number
            if (currentUserGuess == secretAnswerNumber) {
                // The guess is correct
                hasUserGuessedCorrectly = true;
                // Output the success message with the number of guesses
                System.out.println("Correct! You took " + totalGuessCount + " guesses.");
            } else {
                // The guess is not correct, so we give a hint
                if (currentUserGuess > secretAnswerNumber) {
                    // The guess is higher than the answer
                    System.out.println("Too high!");
                } else if (currentUserGuess < secretAnswerNumber) {
                    // The guess is lower than the answer
                    System.out.println("Too low!");
                }
            }
        }

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}