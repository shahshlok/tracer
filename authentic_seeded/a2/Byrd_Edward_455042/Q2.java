import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Create a Random object to generate a random secret number
        Random randomNumberGenerator = new Random();

        // Generate the secret number between 1 and 100
        int secretNumberAnswer = randomNumberGenerator.nextInt(100) + 1;

        // Declare a variable to store the user's current guess
        int userGuessValue = 0;

        // Declare a variable to count how many guesses the user has made
        int totalNumberOfGuesses = 0;

        // Keep looping until the user guesses the correct secret number
        while (userGuessValue != secretNumberAnswer) {
            // Ask the user to guess a number between 1 and 100
            System.out.print("Guess a number (1-100): ");

            // Read the user's guess from the keyboard
            userGuessValue = userInputScanner.nextInt();

            // Each time the user enters a guess, increase the guess counter by 1
            totalNumberOfGuesses = totalNumberOfGuesses + 1;

            // Declare intermediate math variables to compare the guess and the answer
            int differenceBetweenGuessAndAnswer = userGuessValue - secretNumberAnswer;
            int signOfDifference;

            // Compute the sign of the difference: positive, negative, or zero
            if (differenceBetweenGuessAndAnswer > 0) {
                signOfDifference = 1;
            } else if (differenceBetweenGuessAndAnswer < 0) {
                signOfDifference = -1;
            } else {
                signOfDifference = 0;
            }

            // If signOfDifference is 0, the guess is exactly correct
            if (signOfDifference == 0) {
                // The user guessed correctly, so we tell them and show how many guesses it took
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            } else if (signOfDifference > 0) {
                // If signOfDifference is positive, the user's guess is greater than the answer
                System.out.println("Too high!");
            } else {
                // If signOfDifference is negative, the user's guess is less than the answer
                System.out.println("Too low!");
            }
        }

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}