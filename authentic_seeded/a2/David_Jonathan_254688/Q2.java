import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Create a Random object to generate a secret number
        Random randomNumberGenerator = new Random();

        // Generate the secret number between 1 and 100 (inclusive)
        int secretAnswerNumber = randomNumberGenerator.nextInt(100) + 1;

        // Create a variable to store the user's guess
        int userGuessNumber = 0;

        // Create a variable to count how many guesses the user has taken
        int totalNumberOfGuesses = 0;

        // Use a boolean to control the guessing loop
        boolean hasUserGuessedCorrectly = false;

        // Loop until the user guesses the correct number
        while (hasUserGuessedCorrectly == false) {
            // Prompt the user to guess a number
            System.out.print("Guess a number (1-100): ");

            // Read the user's guess from the keyboard
            if (userInputScanner.hasNextInt()) {
                int temporaryUserGuessHolder = userInputScanner.nextInt();
                userGuessNumber = temporaryUserGuessHolder;
            } else {
                // If the user does not enter an integer, consume the invalid input
                String invalidInputHolder = userInputScanner.next();
                // Tell the user that the input was invalid
                System.out.println("Please enter an integer between 1 and 100.");
                // Continue to the next loop iteration without counting this as a guess
                continue;
            }

            // Increase the number of guesses by 1 for this attempt
            totalNumberOfGuesses = totalNumberOfGuesses + 1;

            // Extra nervous check: ensure guess is within 1-100 range
            if (userGuessNumber < 1 || userGuessNumber > 100) {
                System.out.println("Your guess is out of range. Please guess between 1 and 100.");
                // Do not end the game, just continue to the next guess
                continue;
            }

            // Check if the user's guess is equal to the secret number
            if (userGuessNumber == secretAnswerNumber) {
                // The user guessed correctly
                hasUserGuessedCorrectly = true;
                // Print the success message with the total number of guesses
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            } else {
                // The user guessed incorrectly, so give a hint
                if (userGuessNumber > secretAnswerNumber) {
                    // The user's guess is too high
                    System.out.println("Too high!");
                } else if (userGuessNumber < secretAnswerNumber) {
                    // The user's guess is too low
                    System.out.println("Too low!");
                }
            }
        }

        // Close the Scanner to be safe
        userInputScanner.close();
    }
}