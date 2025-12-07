import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Create a Random object to generate the secret number
        Random randomNumberGenerator = new Random();

        // Generate a random number between 1 and 100 inclusive
        int secretAnswerValue = randomNumberGenerator.nextInt(100) + 1;

        // Declare a variable to store the user's current guess
        int userGuessValue = 0;

        // Declare a variable to count how many guesses the user has taken
        int totalNumberOfGuesses = 0;

        // Loop until the user guesses the secret number correctly
        while (userGuessValue != secretAnswerValue) {
            // Ask the user to guess a number between 1 and 100
            System.out.print("Guess a number (1-100): ");

            // Read the user's guess from the keyboard
            userGuessValue = userInputScanner.nextInt();

            // Each time the user enters a guess, increase the guess counter by 1
            totalNumberOfGuesses = totalNumberOfGuesses + 1;

            // Declare intermediate variables to compare the guess and the answer
            int differenceBetweenGuessAndAnswer = userGuessValue - secretAnswerValue;
            int comparisonIndicator = 0;

            // If difference is positive, guess is too high; if negative, guess is too low; if zero, correct
            if (differenceBetweenGuessAndAnswer > 0) {
                comparisonIndicator = 1; // This means guess is greater than answer
            } else if (differenceBetweenGuessAndAnswer < 0) {
                comparisonIndicator = -1; // This means guess is less than answer
            } else {
                comparisonIndicator = 0; // This means guess equals answer
            }

            // Use the comparisonIndicator to decide what message to print
            if (comparisonIndicator > 0) {
                // Guess is too high
                System.out.println("Too high!");
            } else if (comparisonIndicator < 0) {
                // Guess is too low
                System.out.println("Too low!");
            } else {
                // Guess is correct
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            }
        }

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}