import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {

        // Step 1: Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Create a Random object to generate a random secret number
        Random randomNumberGenerator = new Random();

        // Step 3: Generate a random secret number between 1 and 100 (inclusive)
        int secretAnswerValue = randomNumberGenerator.nextInt(100) + 1;

        // Step 4: Make a variable to store the user's current guess
        int userGuessValue = 0;

        // Step 5: Make a variable to count how many guesses the user has taken
        int numberOfGuessesTaken = 0;

        // Step 6: Repeat asking the user for a guess until they guess correctly
        while (userGuessValue != secretAnswerValue) {

            // Step 7: Prompt the user to guess a number between 1 and 100
            System.out.print("Guess a number (1-100): ");

            // Step 8: Read the user's guess from the keyboard
            userGuessValue = userInputScanner.nextInt();

            // Step 9: Increase the guess counter because the user just made a guess
            numberOfGuessesTaken++;

            // Step 10: Check if the user's guess is too low, too high, or correct
            if (userGuessValue < secretAnswerValue) {
                // The guess is smaller than the secret number
                System.out.println("Too low!");
            } else if (userGuessValue > secretAnswerValue) {
                // The guess is larger than the secret number
                System.out.println("Too high!");
            } else {
                // The guess is equal to the secret number, so it is correct
                System.out.println("Correct! You took " + numberOfGuessesTaken + " guesses.");
            }
        }

        // Step 11: Close the Scanner because we are done using it
        userInputScanner.close();
    }
}