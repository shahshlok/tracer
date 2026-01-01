import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Create a Random object to generate the secret number
        Random randomNumberGenerator = new Random();

        // Generate the secret number between 1 and 100 (inclusive)
        int secretRandomNumber = randomNumberGenerator.nextInt(100) + 1;

        // Create a variable to hold the user's current guess
        int userGuessNumber = 0;

        // Create a variable to count how many guesses the user has taken
        int totalNumberOfGuesses = 0;

        // Use a boolean to control the guessing loop
        boolean hasUserGuessedCorrectly = false;

        // Loop until the user guesses the correct number
        while (hasUserGuessedCorrectly == false) {
            // Prompt the user for a guess each time through the loop
            System.out.print("Guess a number (1-100): ");

            // Read the user's guess as an integer
            if (userInputScanner.hasNextInt()) {
                int temporaryHolderForGuess = userInputScanner.nextInt();
                userGuessNumber = temporaryHolderForGuess;
            } else {
                // If the user does not enter an integer, consume the invalid input
                String temporaryInvalidInputHolder = userInputScanner.next();
                // Explain the problem to the user and restart the loop
                System.out.println("Please enter a valid whole number between 1 and 100.");
                // Continue to the next iteration without counting this as a guess
                continue;
            }

            // Increase the guess counter because the user has made a valid integer guess
            totalNumberOfGuesses = totalNumberOfGuesses + 1;

            // Extra nervous check: ensure the guess is within the 1-100 range
            if (userGuessNumber < 1 || userGuessNumber > 100) {
                System.out.println("Your guess is out of range! Please guess a number between 1 and 100.");
                // Do not give high/low hint for out-of-range guesses
                // Continue to the next iteration of the loop
                continue;
            }

            // Compare the user's guess to the secret number
            if (userGuessNumber == secretRandomNumber) {
                // If the guess is correct, set the boolean to true to end the loop
                hasUserGuessedCorrectly = true;
                // Print the success message with the number of guesses taken
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            } else {
                // If the guess is not correct, let the user know if it was too high or too low
                if (userGuessNumber > secretRandomNumber) {
                    System.out.println("Too high!");
                } else if (userGuessNumber < secretRandomNumber) {
                    System.out.println("Too low!");
                } else {
                    // Extra nervous else in case of unexpected situation
                    System.out.println("Unexpected comparison result. Please try again.");
                }
            }
        }

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}