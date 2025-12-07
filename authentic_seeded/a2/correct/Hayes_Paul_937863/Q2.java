import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Random object to generate the secret number
        Random randomNumberGenerator = new Random();

        // Generate a random integer between 1 and 100 inclusive
        int minimumPossibleValue = 1;
        int maximumPossibleValue = 100;
        int rangeOfNumbers = maximumPossibleValue - minimumPossibleValue + 1;
        int secretAnswerNumber = randomNumberGenerator.nextInt(rangeOfNumbers) + minimumPossibleValue;

        // Create a Scanner to read user input from the keyboard
        Scanner keyboardInputScanner = new Scanner(System.in);

        // Keep track of how many guesses the user has taken
        int numberOfGuessesTaken = 0;

        // Variable to store the user's current guess
        int userGuessValue = 0;

        // Loop until the user guesses the correct secret answer
        while (userGuessValue != secretAnswerNumber) {
            // Prompt the user to guess a number between 1 and 100
            System.out.print("Guess a number (1-100): ");

            // Read the user's guess from the keyboard
            userGuessValue = keyboardInputScanner.nextInt();

            // Increase the number of guesses by 1 because the user just made a guess
            numberOfGuessesTaken = numberOfGuessesTaken + 1;

            // Compare the user's guess to the secret answer number
            int differenceBetweenGuessAndAnswer = userGuessValue - secretAnswerNumber;

            // If the difference is positive, the guess is too high
            if (differenceBetweenGuessAndAnswer > 0) {
                System.out.println("Too high!");
            }
            // If the difference is negative, the guess is too low
            else if (differenceBetweenGuessAndAnswer < 0) {
                System.out.println("Too low!");
            }
            // If the difference is zero, the user guessed correctly
            else {
                System.out.println("Correct! You took " + numberOfGuessesTaken + " guesses.");
            }
        }

        // Close the scanner to free system resources
        keyboardInputScanner.close();
    }
}