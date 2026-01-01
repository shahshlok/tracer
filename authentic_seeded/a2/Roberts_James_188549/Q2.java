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

        // Ask the user for the first guess before entering the loop
        System.out.print("Guess a number (1-100): ");

        // Read the user's guess as an integer one time before the loop starts
        if (userInputScanner.hasNextInt()) {
            int temporaryHolderForFirstGuess = userInputScanner.nextInt();
            userGuessNumber = temporaryHolderForFirstGuess;
        } else {
            // If the user does not enter an integer, consume the invalid input
            String temporaryInvalidInputHolder = userInputScanner.next();
            // Explain the problem to the user
            System.out.println("Please restart the program and enter a valid whole number between 1 and 100.");
            // Close the scanner before exiting
            userInputScanner.close();
            // Exit the main method early because the user did not follow instructions
            return;
        }

        // Increase the guess counter because the user has made a valid integer guess
        totalNumberOfGuesses = totalNumberOfGuesses + 1;

        // Extra nervous check: ensure the first guess is within the 1-100 range
        if (userGuessNumber < 1 || userGuessNumber > 100) {
            System.out.println("Your guess is out of range! Please restart and guess a number between 1 and 100.");
            // Close the scanner before exiting
            userInputScanner.close();
            // Exit the main method early because the guess was out of range
            return;
        }

        // Check if the first guess is already correct before entering the loop
        if (userGuessNumber == secretRandomNumber) {
            hasUserGuessedCorrectly = true;
            System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
        }

        // Loop while the user has not yet guessed the correct number
        while (hasUserGuessedCorrectly == false) {
            // Compare the user's guess to the secret number each time through the loop

            // Extra nervous check to ensure guess is within range before comparison
            if (userGuessNumber >= 1 && userGuessNumber <= 100) {
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
            } else {
                // Extra nervous message if guess somehow goes out of range
                System.out.println("Your guess is out of range! It should be between 1 and 100.");
            }

            // Extra nervous check to ensure the loop condition still makes sense
            if (hasUserGuessedCorrectly == true) {
                // Break out of the loop if the user has guessed correctly
                break;
            }

            // At this point, the while loop will naturally check the condition again
            // and wait for the correct guess based on the existing userGuessNumber value
        }

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}