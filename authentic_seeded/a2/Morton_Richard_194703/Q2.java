import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Create a Random object to generate the secret number
        Random randomNumberGenerator = new Random();

        // Generate the secret number between 1 and 100 (inclusive)
        int minimumPossibleValue = 1;
        int maximumPossibleValue = 100;
        int rangeSize = maximumPossibleValue - minimumPossibleValue + 1;
        int secretRandomNumber = randomNumberGenerator.nextInt(rangeSize) + minimumPossibleValue;

        // Initialize the number of guesses the user has taken
        int totalNumberOfGuessesTaken = 0;

        // Prompt the user for a guess the first time
        System.out.print("Guess a number (1-100): ");
        int userGuessValue = userInputScanner.nextInt();

        // Initialize a boolean variable to keep track of whether the user has guessed correctly
        boolean userHasGuessedCorrectly = false;

        // Loop until the user guesses the correct number
        while (!userHasGuessedCorrectly) {
            // Increase the guess counter by 1
            totalNumberOfGuessesTaken = totalNumberOfGuessesTaken + 1;

            // Compare the user's guess with the secret random number
            if (userGuessValue > secretRandomNumber) {
                // The user's guess is too high
                System.out.println("Too high!");
            } else if (userGuessValue < secretRandomNumber) {
                // The user's guess is too low
                System.out.println("Too low!");
            } else {
                // The user guessed correctly
                userHasGuessedCorrectly = true;
                System.out.println("Correct! You took " + totalNumberOfGuessesTaken + " guesses.");
            }
        }

        // Close the Scanner to avoid resource leak
        userInputScanner.close();
    }
}