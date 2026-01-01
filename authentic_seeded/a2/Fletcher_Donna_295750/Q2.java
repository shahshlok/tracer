import java.util.Scanner;
import java.util.Random;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Create a Random object to generate a random secret number
        Random randomNumberGenerator = new Random();

        // Step 3: Generate the secret number between 1 and 100 (inclusive)
        int secretAnswerNumber = randomNumberGenerator.nextInt(100) + 1;

        // Step 4: Create a variable to keep track of how many guesses the user has made
        int numberOfGuessesTaken = 0;

        // Step 5: Create a variable to store the user's current guess
        int userCurrentGuess = 0;

        // Step 6: Repeat guessing until the user guesses the correct number
        while (userCurrentGuess != secretAnswerNumber) {
            // Step 6a: Ask the user to guess a number between 1 and 100
            System.out.print("Guess a number (1-100): ");

            // Step 6b: Read the user's guess from the keyboard
            userCurrentGuess = userInputScanner.nextInt();

            // Step 6c: Increase the number of guesses taken by 1
            numberOfGuessesTaken = numberOfGuessesTaken + 1;

            // Step 6d: Check if the guess is too low, too high, or correct
            if (userCurrentGuess < secretAnswerNumber) {
                // The user's guess is smaller than the secret number
                System.out.println("Too low!");
            } else if (userCurrentGuess > secretAnswerNumber) {
                // The user's guess is larger than the secret number
                System.out.println("Too high!");
            } else {
                // The user guessed the correct number
                System.out.println("Correct! You took " + numberOfGuessesTaken + " guesses.");
            }
        }

        // Step 7: Close the Scanner because we are done with input
        userInputScanner.close();
    }
}