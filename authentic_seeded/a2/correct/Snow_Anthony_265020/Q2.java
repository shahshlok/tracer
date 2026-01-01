import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read user input from the keyboard
        Scanner keyboardScanner = new Scanner(System.in);

        // Step 2: Create a Random object to generate the secret number
        Random randomNumberGenerator = new Random();

        // Step 3: Generate a random number between 1 and 100 (inclusive)
        int secretAnswerNumber = randomNumberGenerator.nextInt(100) + 1;

        // Step 4: Create a variable to count how many guesses the user takes
        int totalNumberOfGuesses = 0;

        // Step 5: Create a variable to store the user's current guess
        int userGuessNumber = 0;

        // Step 6: Use a loop that continues until the user guesses the correct number
        while (userGuessNumber != secretAnswerNumber) {
            // Step 7: Ask the user to guess a number between 1 and 100
            System.out.print("Guess a number (1-100): ");

            // Step 8: Read the user's guess from the keyboard
            userGuessNumber = keyboardScanner.nextInt();

            // Step 9: Increase the guess counter because the user just made a guess
            totalNumberOfGuesses++;

            // Step 10: Check if the guess is too low
            if (userGuessNumber < secretAnswerNumber) {
                System.out.println("Too low!");
            }
            // Step 11: Check if the guess is too high
            else if (userGuessNumber > secretAnswerNumber) {
                System.out.println("Too high!");
            }
            // Step 12: If the guess is not too low and not too high, it must be correct
            else {
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            }
        }

        // Step 13: Close the Scanner because we are done with input
        keyboardScanner.close();
    }
}