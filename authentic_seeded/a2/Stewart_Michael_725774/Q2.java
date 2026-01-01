import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Create a Random object to generate random numbers
        Random randomNumberGenerator = new Random();

        // Generate the secret number between 1 and 100
        int secretAnswerValue = randomNumberGenerator.nextInt(100) + 1;

        // Declare a variable to store the user's guess
        int userGuessValue = 0;

        // Declare a variable to count how many guesses the user has taken
        int totalNumberOfGuesses = 0;

        // Keep asking the user for guesses until they guess the correct number
        while (userGuessValue != secretAnswerValue) {
            // Prompt the user to guess a number between 1 and 100
            System.out.print("Guess a number (1-100): ");

            // Read the user's guess from input
            userGuessValue = userInputScanner.nextInt();

            // Increase the number of guesses by 1 each time the user guesses
            totalNumberOfGuesses = totalNumberOfGuesses + 1;

            // Declare intermediate math variables to compare the guess and the answer
            int differenceValue = userGuessValue - secretAnswerValue;
            int comparisonIndicatorValue;

            // Use a formula-like approach to determine if guess is high, low, or equal
            if (differenceValue > 0) {
                // If the difference is positive, the guess is too high
                comparisonIndicatorValue = 1;
            } else if (differenceValue < 0) {
                // If the difference is negative, the guess is too low
                comparisonIndicatorValue = -1;
            } else {
                // If the difference is zero, the guess is correct
                comparisonIndicatorValue = 0;
            }

            // Now use the comparisonIndicatorValue to print the correct hint
            if (comparisonIndicatorValue > 0) {
                // User's guess is greater than the answer
                System.out.println("Too high!");
            } else if (comparisonIndicatorValue < 0) {
                // User's guess is less than the answer
                System.out.println("Too low!");
            } else {
                // User's guess equals the answer, so they are correct
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            }
        }

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}