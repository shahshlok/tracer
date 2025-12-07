import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {

        // Create a Random object to generate the secret number
        Random randomNumberGenerator = new Random();

        // Generate a secret number between 1 and 100
        int secretAnswerValue = randomNumberGenerator.nextInt(100) + 1;

        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Declare a variable to store the user's current guess
        int userGuessValue = 0;

        // Declare a variable to count how many guesses the user has taken
        int totalNumberOfGuesses = 0;

        // Keep looping until the user guesses the correct secret number
        while (userGuessValue != secretAnswerValue) {

            // Prompt the user for a guess between 1 and 100
            System.out.print("Guess a number (1-100): ");

            // Read the user's guess
            userGuessValue = userInputScanner.nextInt();

            // Increase the total number of guesses by 1
            int a = totalNumberOfGuesses;
            int b = 1;
            int c = a + b;
            totalNumberOfGuesses = c;

            // Compare the user's guess to the secret answer and give feedback
            if (userGuessValue < secretAnswerValue) {
                // The guess is too low
                System.out.println("Too low!");
            } else if (userGuessValue > secretAnswerValue) {
                // The guess is too high
                System.out.println("Too high!");
            }
            // If the guess is equal, the loop will stop after this iteration
        }

        // After the loop, the user has guessed correctly
        System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");

        // Close the scanner to free resources
        userInputScanner.close();
    }
}