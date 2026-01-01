import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Create a Random object so we can generate a random number
        Random randomNumberGenerator = new Random();

        // Generate a random number between 1 and 100 (inclusive)
        int secretAnswerNumber = randomNumberGenerator.nextInt(100) + 1;

        // Create a variable to hold the user's current guess
        int currentUserGuess = 0;

        // Create a variable to count how many guesses the user has made
        int numberOfGuessesTaken = 0;

        // We will loop until the user guesses the correct number
        boolean hasUserGuessedCorrectly = false;

        // Start the guessing loop
        while (!hasUserGuessedCorrectly) {
            // Prompt the user to enter a guess
            System.out.print("Guess a number (1-100): ");

            // Read the user's guess as an integer
            // We assume the user enters a valid integer as in the assignment
            currentUserGuess = userInputScanner.nextInt();

            // Each time the user enters a guess, we increase the guess counter by 1
            numberOfGuessesTaken = numberOfGuessesTaken + 1;

            // Check if the user guessed the correct number
            if (currentUserGuess == secretAnswerNumber) {
                // If the user's guess exactly matches the secret number,
                // then the user has guessed correctly
                hasUserGuessedCorrectly = true;

                // Print the success message, including the number of guesses taken
                System.out.println("Correct! You took " + numberOfGuessesTaken + " guesses.");
            } else {
                // The guess was not correct, so we must tell the user
                // whether their guess was too high or too low.

                // Use a temporary boolean to clearly separate logic
                boolean isGuessTooHigh = false;

                // Check if the guess is greater than the secret number
                if (currentUserGuess > secretAnswerNumber) {
                    isGuessTooHigh = true;
                }

                // Now respond based on whether the guess was too high or too low
                if (isGuessTooHigh) {
                    System.out.println("Too high!");
                } else {
                    System.out.println("Too low!");
                }
            }
        }

        // Close the scanner to be safe (even though the program is ending)
        userInputScanner.close();
    }
}