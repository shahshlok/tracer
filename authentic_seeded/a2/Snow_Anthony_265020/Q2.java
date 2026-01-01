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

        // Step 5: Ask the user for the first guess and store it
        System.out.print("Guess a number (1-100): ");
        int userGuessNumber = keyboardScanner.nextInt();

        // Step 6: Use a loop that continues until the user guesses the correct number
        while (userGuessNumber != secretAnswerNumber) {
            // Step 7: Increase the guess counter because the user just made a guess
            totalNumberOfGuesses++;

            // Step 8: Check if the guess is too low
            if (userGuessNumber < secretAnswerNumber) {
                System.out.println("Too low!");
            }
            // Step 9: Check if the guess is too high
            else if (userGuessNumber > secretAnswerNumber) {
                System.out.println("Too high!");
            }
            // Step 10: The loop will run again and naturally check the condition
        }

        // Step 11: When we get here, the guess must be correct, so count the final guess
        totalNumberOfGuesses++;

        // Step 12: Tell the user they are correct and show how many guesses they took
        System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");

        // Step 13: Close the Scanner because we are done with input
        keyboardScanner.close();
    }
}