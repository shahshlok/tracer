import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {

        // Create a Scanner object to read user input from the keyboard
        Scanner keyboardScanner = new Scanner(System.in);

        // Create a Random object to generate a random secret number
        Random randomNumberGenerator = new Random();

        // Generate the secret number between 1 and 100 (inclusive)
        int secretAnswerNumber = randomNumberGenerator.nextInt(100) + 1;

        // Declare a variable to store the user's current guess
        int userGuessNumber = 0;

        // Declare a variable to count how many guesses the user has taken
        int totalNumberOfGuesses = 0;

        // Prompt the user to enter a guess between 1 and 100
        System.out.print("Guess a number (1-100): ");

        // Read the user's guess from the keyboard once at the beginning
        userGuessNumber = keyboardScanner.nextInt();

        // Increase the guess counter because the user just made a guess
        totalNumberOfGuesses = totalNumberOfGuesses + 1;

        // Keep looping until the user guesses the correct number
        while (userGuessNumber != secretAnswerNumber) {

            // Compare the user's guess to the secret answer

            // First, compute the difference between guess and answer
            int differenceBetweenGuessAndAnswer = userGuessNumber - secretAnswerNumber;

            // If the user's guess is lower than the answer, the difference is negative
            if (differenceBetweenGuessAndAnswer < 0) {
                System.out.println("Too low!");
            }
            // If the user's guess is higher than the answer, the difference is positive
            else if (differenceBetweenGuessAndAnswer > 0) {
                System.out.println("Too high!");
            }
            // If the difference is zero, the guess is exactly correct
            else {
                // The user guessed correctly, so print a success message with number of guesses
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            }
        }

        // If the loop condition is false at the beginning (guess correct right away),
        // then we still need to print the success message
        if (userGuessNumber == secretAnswerNumber) {
            System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
        }

        // Close the Scanner to free system resources
        keyboardScanner.close();
    }
}