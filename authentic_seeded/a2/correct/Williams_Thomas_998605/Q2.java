import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read user input from the keyboard
        Scanner keyboardScanner = new Scanner(System.in);

        // Step 2: Create a Random object so we can generate a random secret number
        Random randomNumberGenerator = new Random();

        // Step 3: Generate a random number between 1 and 100 (inclusive)
        int secretAnswerNumber = randomNumberGenerator.nextInt(100) + 1;

        // Step 4: Create a variable to store the user's current guess
        int userGuessNumber = 0;

        // Step 5: Create a variable to count how many guesses the user has taken
        int totalNumberOfGuesses = 0;

        // Step 6: Keep asking the user to guess until their guess matches the secret number
        while (userGuessNumber != secretAnswerNumber) {
            // Step 6a: Prompt the user to guess a number between 1 and 100
            System.out.print("Guess a number (1-100): ");

            // Step 6b: Read the user's guess from the keyboard
            userGuessNumber = keyboardScanner.nextInt();

            // Step 6c: Increase the guess counter by 1 because the user has made a guess
            totalNumberOfGuesses++;

            // Step 6d: Check if the guess is too low, too high, or correct
            if (userGuessNumber < secretAnswerNumber) {
                // The user's guess is smaller than the secret number
                System.out.println("Too low!");
            } else if (userGuessNumber > secretAnswerNumber) {
                // The user's guess is larger than the secret number
                System.out.println("Too high!");
            } else {
                // The user's guess is exactly equal to the secret number
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            }
        }

        // Step 7: Close the Scanner because we are done with user input
        keyboardScanner.close();
    }
}