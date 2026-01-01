import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {

        // Create a Random object to generate the secret number
        Random randomNumberGenerator = new Random();

        // Declare the bounds for the random number (mathematical constants)
        int lowerBoundInclusive = 1;
        int upperBoundInclusive = 100;

        // Compute the range size
        int rangeSize = upperBoundInclusive - lowerBoundInclusive + 1;

        // Generate the secret number between 1 and 100 using the formula:
        // secretNumber = randomIntegerInRange(0, rangeSize - 1) + lowerBoundInclusive
        int secretRandomNumber = randomNumberGenerator.nextInt(rangeSize) + lowerBoundInclusive;

        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Variable to store the user guess each time
        int userGuessValue = 0;

        // Variable to count how many guesses the user has taken
        int totalNumberOfGuesses = 0;

        // Loop until the user guesses the secret number correctly
        while (userGuessValue != secretRandomNumber) {

            // Prompt the user to guess a number between 1 and 100
            System.out.print("Guess a number (1-100): ");

            // Read the user guess from input
            userGuessValue = userInputScanner.nextInt();

            // Each time the user makes a guess, increase the guess count by 1
            totalNumberOfGuesses = totalNumberOfGuesses + 1;

            // Now compare the userGuessValue with secretRandomNumber using math
            int differenceValue = userGuessValue - secretRandomNumber;

            // If the user guessed too high, differenceValue will be positive
            if (differenceValue > 0) {
                System.out.println("Too high!");
            }
            // If the user guessed too low, differenceValue will be negative
            else if (differenceValue < 0) {
                System.out.println("Too low!");
            }
            // If the difference is zero, the user guessed correctly
            else {
                // Correct guess; break out of the loop after printing the result
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            }
        }

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}