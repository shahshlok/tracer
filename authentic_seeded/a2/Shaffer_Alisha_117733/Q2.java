import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner object to read user input from the keyboard
        Scanner keyboardInput = new Scanner(System.in);

        // Step 2: Create a Random object to generate random numbers
        Random randomNumberGenerator = new Random();

        // Step 3: Generate a random secret number between 1 and 100 (inclusive)
        int secretAnswerValue = randomNumberGenerator.nextInt(100) + 1;

        // Step 4: Create a variable to store the user's guess
        int userGuessValue = 0;

        // Step 5: Create a variable to count how many guesses the user has made
        int numberOfGuessesTaken = 0;

        // Step 6: Keep looping until the user guesses the correct number
        while (userGuessValue != secretAnswerValue) {
            // Step 7: Ask the user to guess a number between 1 and 100
            System.out.print("Guess a number (1-100): ");

            // Step 8: Read the user's guess from the keyboard
            userGuessValue = keyboardInput.nextInt();

            // Step 9: Increase the guess counter by 1 because the user just made a guess
            numberOfGuessesTaken++;

            // Step 10: Check if the user's guess is too low, too high, or correct
            if (userGuessValue < secretAnswerValue) {
                // Step 11: Tell the user their guess was too low
                System.out.println("Too low!");
            } else if (userGuessValue > secretAnswerValue) {
                // Step 12: Tell the user their guess was too high
                System.out.println("Too high!");
            } else {
                // Step 13: The user guessed the correct number, so print a success message
                System.out.println("Correct! You took " + numberOfGuessesTaken + " guesses.");
            }
        }

        // Step 14: Close the Scanner to free system resources
        keyboardInput.close();
    }
}