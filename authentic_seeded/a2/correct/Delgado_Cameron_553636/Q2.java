import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {

        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Create a Random object to generate a random number
        Random randomNumberGenerator = new Random();

        // Generate the secret random number between 1 and 100 (inclusive)
        int secretRandomNumber = randomNumberGenerator.nextInt(100) + 1;

        // Variable to hold the user's current guess
        int userGuessValue = 0;

        // Variable to count how many guesses the user has taken
        int numberOfGuessesTaken = 0;

        // Boolean flag to track if the correct number has been guessed
        boolean hasUserGuessedCorrectly = false;

        // Loop until the user guesses the correct number
        while (hasUserGuessedCorrectly == false) {

            // Prompt the user for a guess
            System.out.print("Guess a number (1-100): ");

            // Read the user's guess
            userGuessValue = userInputScanner.nextInt();

            // Increase the guess counter every time the user makes a guess
            numberOfGuessesTaken = numberOfGuessesTaken + 1;

            // Check if the userGuessValue is equal to the secretRandomNumber
            if (userGuessValue == secretRandomNumber) {
                // The user guessed correctly
                hasUserGuessedCorrectly = true;

                // Print the success message and how many guesses it took
                System.out.println("Correct! You took " + numberOfGuessesTaken + " guesses.");
            } else {
                // The user did not guess correctly, so we must tell them if it is too high or too low

                // Check if the guess is higher than the secret number
                if (userGuessValue > secretRandomNumber) {
                    // The guess is too high
                    System.out.println("Too high!");
                } else {
                    // If it is not higher and also not equal, it must be lower
                    if (userGuessValue < secretRandomNumber) {
                        // The guess is too low
                        System.out.println("Too low!");
                    }
                }
            }
        }

        // Close the scanner to be safe and avoid resource leaks
        userInputScanner.close();
    }
}