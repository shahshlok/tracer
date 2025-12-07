import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner keyboardInputScanner = new Scanner(System.in);

        // Create a Random object to generate a random number
        Random randomNumberGenerator = new Random();

        // Generate the secret number between 1 and 100 using the formula:
        // secretNumber = random integer from 0 to 99 + 1
        int randomBaseValue = randomNumberGenerator.nextInt(100); // values 0 to 99
        int shiftValue = 1; // shift by 1 to move range from 0-99 to 1-100
        int secretAnswerNumber = randomBaseValue + shiftValue;

        // Initialize the number of guesses the user has taken
        int numberOfGuessesTaken = 0;

        // Initialize the user's guess variable
        int userGuessNumber = 0;

        // Use a loop that continues until the userGuessNumber equals secretAnswerNumber
        while (userGuessNumber != secretAnswerNumber) {
            // Prompt the user to guess a number between 1 and 100
            System.out.print("Guess a number (1-100): ");

            // Read the user's guess from the keyboard
            userGuessNumber = keyboardInputScanner.nextInt();

            // Increase the number of guesses taken by 1 each time the user guesses
            int incrementGuessValue = 1;
            numberOfGuessesTaken = numberOfGuessesTaken + incrementGuessValue;

            // Compare the user's guess with the secret answer using simple math comparisons
            if (userGuessNumber > secretAnswerNumber) {
                // The user's guess is too high
                System.out.println("Too high!");
            } else if (userGuessNumber < secretAnswerNumber) {
                // The user's guess is too low
                System.out.println("Too low!");
            } else {
                // The user guessed the exact secret number
                System.out.println("Correct! You took " + numberOfGuessesTaken + " guesses.");
            }
        }

        // Close the Scanner to free system resources
        keyboardInputScanner.close();
    }
}