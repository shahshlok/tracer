import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read user input from the keyboard
        Scanner keyboardScanner = new Scanner(System.in);

        // Step 2: Create a Random object to generate a secret number
        Random randomNumberGenerator = new Random();

        // Step 3: Generate a random secret number between 1 and 100
        int secretAnswerNumber = randomNumberGenerator.nextInt(100) + 1;

        // Step 4: Create a variable to keep track of how many guesses the user has made
        int totalNumberOfGuesses = 0;

        // Step 5: Create a variable to store the user's current guess
        int currentUserGuessNumber = 0;

        // Step 6: Keep asking the user to guess until they guess the secret number correctly
        while (currentUserGuessNumber != secretAnswerNumber) {
            // Step 7: Ask the user for their guess
            System.out.print("Guess a number (1-100): ");
            currentUserGuessNumber = keyboardScanner.nextInt();

            // Step 8: Increase the guess counter because the user just made a guess
            totalNumberOfGuesses = totalNumberOfGuesses + 1;

            // Step 9: Check if the guess is too low, too high, or correct
            if (currentUserGuessNumber < secretAnswerNumber) {
                // The guess is lower than the secret number
                System.out.println("Too low!");
            } else if (currentUserGuessNumber > secretAnswerNumber) {
                // The guess is higher than the secret number
                System.out.println("Too high!");
            } else {
                // The guess is exactly equal to the secret number
                // Step 10: Tell the user they are correct and how many guesses they took
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            }
        }

        // Step 11: Close the Scanner because we are done using it
        keyboardScanner.close();
    }
}