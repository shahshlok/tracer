import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {

        // Step 1: Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Create a Random object to generate a random number
        Random randomNumberGenerator = new Random();

        // Step 3: Generate a secret number between 1 and 100 (inclusive)
        int secretAnswerNumber = randomNumberGenerator.nextInt(100) + 1;

        // Step 4: Create a variable to count how many guesses the user makes
        int totalNumberOfGuesses = 0;

        // Step 5: Create a variable to store the user's current guess
        int currentUserGuessValue = 0;

        // Step 6: Use a loop that keeps going until the user guesses the secret number
        while (currentUserGuessValue != secretAnswerNumber) {

            // Step 7: Prompt the user to guess a number
            System.out.print("Guess a number (1-100): ");

            // Step 8: Read the user's guess from the keyboard
            currentUserGuessValue = userInputScanner.nextInt();

            // Step 9: Increase the guess counter by 1 because the user just made a guess
            totalNumberOfGuesses++;

            // Step 10: Check if the guess is too low, too high, or correct
            if (currentUserGuessValue < secretAnswerNumber) {
                // The guess is smaller than the secret number
                System.out.println("Too low!");
            } else if (currentUserGuessValue > secretAnswerNumber) {
                // The guess is larger than the secret number
                System.out.println("Too high!");
            } else {
                // The guess is equal to the secret number, so it is correct
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            }
        }

        // Step 11: Close the Scanner because we are done reading input
        userInputScanner.close();
    }
}