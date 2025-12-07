import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read input from the user
        Scanner keyboardScanner = new Scanner(System.in);

        // Step 2: Create a Random object so the computer can pick a random number
        Random randomNumberGenerator = new Random();

        // Step 3: Generate the secret number between 1 and 100
        int secretAnswerNumber = randomNumberGenerator.nextInt(100) + 1;

        // Step 4: Create variables to store the user's guess and the number of guesses
        int userGuessNumber = 0;
        int totalNumberOfGuesses = 0;

        // Step 5: Keep asking the user to guess until they guess the correct number
        while (userGuessNumber != secretAnswerNumber) {
            // Step 6: Prompt the user to enter a guess
            System.out.print("Guess a number (1-100): ");

            // Step 7: Read the user's guess from the keyboard
            userGuessNumber = keyboardScanner.nextInt();

            // Step 8: Increase the guess counter by 1 because the user just made a guess
            totalNumberOfGuesses = totalNumberOfGuesses + 1;

            // Step 9: Check if the guess is too low, too high, or correct
            if (userGuessNumber < secretAnswerNumber) {
                // The guess is smaller than the secret number
                System.out.println("Too low!");
            } else if (userGuessNumber > secretAnswerNumber) {
                // The guess is larger than the secret number
                System.out.println("Too high!");
            } else {
                // The guess is exactly the secret number
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            }
        }

        // Step 10: Close the Scanner because we are done using it
        keyboardScanner.close();
    }
}