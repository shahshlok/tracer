import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Create a Random object so we can generate a random secret number
        Random randomNumberGenerator = new Random();

        // Generate the secret number between 1 and 100 (inclusive)
        // rand.nextInt(100) gives 0 to 99, so we add 1 to shift to 1 to 100
        int secretNumberAnswer = randomNumberGenerator.nextInt(100) + 1;

        // Variable to keep track of how many guesses the user has taken
        int totalNumberOfGuesses = 0;

        // Variable to store the current user guess
        int currentUserGuessValue = 0;

        // We keep looping until the currentUserGuessValue equals the secretNumberAnswer
        while (currentUserGuessValue != secretNumberAnswer) {
            // Prompt the user to guess a number
            System.out.print("Guess a number (1-100): ");

            // Read the user's guess from the keyboard
            currentUserGuessValue = userInputScanner.nextInt();

            // Increase the total number of guesses by 1 each time the user guesses
            totalNumberOfGuesses = totalNumberOfGuesses + 1;

            // We will compare the guess and the answer using mathematical comparison
            int differenceBetweenGuessAndAnswer = currentUserGuessValue - secretNumberAnswer;

            // If the guess is less than the answer, then difference is negative, so it is too low
            if (differenceBetweenGuessAndAnswer < 0) {
                System.out.println("Too low!");
            }
            // If the guess is greater than the answer, then difference is positive, so it is too high
            else if (differenceBetweenGuessAndAnswer > 0) {
                System.out.println("Too high!");
            }
            // Otherwise the difference must be zero, so the guess is correct
            else {
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            }
        }

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}