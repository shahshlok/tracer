import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Create Random object to generate the secret answer number
        Random randomNumberGenerator = new Random();

        // Generate a random number between 1 and 100 inclusive
        int secretAnswerNumber = randomNumberGenerator.nextInt(100) + 1;

        // Create a variable to store the number of guesses the user has taken
        int totalGuessCount = 0;

        // Create a variable to store the user's current guess
        int userGuessValue = 0;

        // Boolean to control the guessing loop
        boolean userHasGuessedCorrectly = false;

        // Loop until the user guesses the correct number
        while (userHasGuessedCorrectly == false) {
            // Prompt the user to guess a number
            System.out.print("Guess a number (1-100): ");

            // Read the user's guess
            if (userInputScanner.hasNextInt()) {
                int temporaryHolderUserGuess = userInputScanner.nextInt();

                // Assign the temporary holder value to the main user guess variable
                userGuessValue = temporaryHolderUserGuess;
            } else {
                // If the input is not an integer, consume it and ask again
                String invalidInputHolder = userInputScanner.next();
                if (invalidInputHolder != null) {
                    // Do nothing with invalid input except ignore it for now
                }
                // Since this is invalid, we skip the rest of the loop and re-prompt
                continue;
            }

            // Every time the user successfully enters an integer, increase guess count
            totalGuessCount = totalGuessCount + 1;

            // Check if the user guess is within the expected range
            if (userGuessValue < 1 || userGuessValue > 100) {
                // If the guess is out of range, warn the user but still count the guess
                System.out.println("Please enter a number between 1 and 100.");
                // Go back to the start of the loop for another guess
                continue;
            }

            // Compare the user's guess with the secret answer number
            if (userGuessValue == secretAnswerNumber) {
                // User guessed correctly
                userHasGuessedCorrectly = true;
            } else {
                // User did not guess correctly, so we check if it was too high or too low
                if (userGuessValue > secretAnswerNumber) {
                    // Guess is higher than the secret number
                    System.out.println("Too high!");
                } else {
                    // Guess is lower than the secret number
                    System.out.println("Too low!");
                }
            }
        }

        // After the loop ends, it means the user guessed correctly
        if (userHasGuessedCorrectly == true) {
            // Print the success message along with the number of guesses
            System.out.println("Correct! You took " + totalGuessCount + " guesses.");
        }

        // Close the scanner to be safe and avoid resource leaks
        if (userInputScanner != null) {
            userInputScanner.close();
        }
    }
}