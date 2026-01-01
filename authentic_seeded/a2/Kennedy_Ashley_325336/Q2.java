import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {

        // Create a Random object to generate the secret number
        Random randomNumberGenerator = new Random();

        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Generate the secret number between 1 and 100 (inclusive)
        int secretRandomNumber = randomNumberGenerator.nextInt(100) + 1;

        // Initialize a counter for how many guesses the user takes
        int totalNumberOfGuesses = 0;

        // Initialize a variable to store the user's current guess
        int currentUserGuessValue = 0;

        // We will loop until the user guesses the correct secretRandomNumber
        while (currentUserGuessValue != secretRandomNumber) {

            // Ask the user to guess a number between 1 and 100
            System.out.print("Guess a number (1-100): ");

            // Read the user's guess
            currentUserGuessValue = userInputScanner.nextInt();

            // Each time the user enters a guess, increase the guess counter by 1
            int previousTotalNumberOfGuesses = totalNumberOfGuesses;
            int incrementValueForGuessCount = 1;
            totalNumberOfGuesses = previousTotalNumberOfGuesses + incrementValueForGuessCount;

            // Compare the guess to the secret number using math-style variables
            int differenceBetweenGuessAndSecret = currentUserGuessValue - secretRandomNumber;

            // If the guess is too low, tell the user
            if (differenceBetweenGuessAndSecret < 0) {
                System.out.println("Too low!");
            }
            // If the guess is too high, tell the user
            else if (differenceBetweenGuessAndSecret > 0) {
                System.out.println("Too high!");
            }
            // If the difference is zero, the guess is correct
            else {
                // User guessed the correct number, break out after printing result
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            }
        }

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}