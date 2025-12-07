import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {

        // Step 1: Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Create a Random object to generate the secret number
        Random randomNumberGenerator = new Random();

        // Step 3: Generate a random number between 1 and 100 (inclusive)
        int secretAnswerValue = randomNumberGenerator.nextInt(100) + 1;

        // Step 4: Create a variable to count how many guesses the user takes
        int totalNumberOfGuesses = 0;

        // Step 5: Create a variable to store the user's current guess
        int userGuessValue = 0;

        // Step 6: Use a loop to keep asking for guesses until the user is correct
        while (userGuessValue != secretAnswerValue) {

            // Step 7: Ask the user to guess a number between 1 and 100
            System.out.print("Guess a number (1-100): ");

            // Step 8: Read the user's guess from the keyboard
            userGuessValue = userInputScanner.nextInt();

            // Step 9: Increase the guess counter by 1 since the user just made a guess
            totalNumberOfGuesses++;

            // Step 10: Check if the guess is too low, too high, or correct
            if (userGuessValue < secretAnswerValue) {
                // Step 11: Tell the user their guess was too low
                System.out.println("Too low!");
            } else if (userGuessValue > secretAnswerValue) {
                // Step 12: Tell the user their guess was too high
                System.out.println("Too high!");
            } else {
                // Step 13: The guess is correct, so tell the user and show how many guesses it took
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            }
        }

        // Step 14: Close the Scanner because we are done with input
        userInputScanner.close();
    }
}