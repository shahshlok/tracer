import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Create a Random object to generate random numbers
        Random randomNumberGenerator = new Random();

        // Generate the secret number between 1 and 100 (inclusive)
        int secretAnswerNumber = randomNumberGenerator.nextInt(100) + 1;

        // Create a variable to hold the user's current guess
        int userGuessNumber = 0;

        // Create a variable to count how many guesses the user has taken
        int totalNumberOfGuesses = 0;

        // We will use a boolean to keep track of whether the correct guess has been made
        boolean hasUserGuessedCorrectly = false;

        // Loop until the user guesses the correct secret number
        while (hasUserGuessedCorrectly == false) {
            // Prompt the user to enter a guess between 1 and 100
            System.out.print("Guess a number (1-100): ");

            // Read the user's guess from the keyboard
            if (userInputScanner.hasNextInt()) {
                // Store the user's guess in a temporary holder variable
                int temporaryUserGuessHolder = userInputScanner.nextInt();

                // Assign the temporary holder to the main guess variable
                userGuessNumber = temporaryUserGuessHolder;
            } else {
                // If the user does not enter an integer, consume the invalid input
                String invalidInputString = userInputScanner.next();
                // Inform the user that the input was invalid
                System.out.println("Please enter a valid integer between 1 and 100.");
                // Continue to the next loop iteration without counting this as a guess
                continue;
            }

            // Increase the number of guesses taken by the user
            totalNumberOfGuesses = totalNumberOfGuesses + 1;

            // Extra cautious check: ensure the secret answer is actually between 1 and 100
            if (secretAnswerNumber >= 1 && secretAnswerNumber <= 100) {
                // Extra cautious check: ensure the user's guess is in a reasonable range
                if (userGuessNumber >= 1 && userGuessNumber <= 100) {

                    // Compare the user's guess to the secret number
                    if (userGuessNumber == secretAnswerNumber) {
                        // The user guessed correctly, so set the boolean flag
                        hasUserGuessedCorrectly = true;

                        // Print the success message including the number of guesses taken
                        System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
                    } else {
                        // The user did not guess correctly

                        // Check if the guess is too low
                        if (userGuessNumber < secretAnswerNumber) {
                            System.out.println("Too low!");
                        }

                        // Check if the guess is too high
                        if (userGuessNumber > secretAnswerNumber) {
                            System.out.println("Too high!");
                        }
                    }
                } else {
                    // The user's guess is out of the valid 1-100 range
                    System.out.println("Your guess is out of range. Please guess a number between 1 and 100.");
                }
            } else {
                // This should never happen, but we are nervous about edge cases
                System.out.println("Internal error: secret number out of range.");
                // Break out of the loop just in case
                break;
            }
        }

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}