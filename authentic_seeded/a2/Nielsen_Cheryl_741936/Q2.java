import java.util.Scanner;
import java.util.Random;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Create a Random object to generate a random secret number
        Random randomNumberGenerator = new Random();

        // Generate the secret answer between 1 and 100 inclusive
        int secretAnswerValue = randomNumberGenerator.nextInt(100) + 1;

        // Initialize a counter to keep track of how many guesses the user takes
        int totalNumberOfGuesses = 0;

        // Initialize a variable to store the user's current guess
        int userGuessValue = 0;

        // We will loop until the user guesses the correct secret answer
        // The condition for continuing the loop is that userGuessValue is not equal to secretAnswerValue
        while (userGuessValue != secretAnswerValue) {

            // Prompt the user to guess a number between 1 and 100
            System.out.print("Guess a number (1-100): ");

            // Read the user's guess from the keyboard
            userGuessValue = userInputScanner.nextInt();

            // Each time the user makes a guess, we increase the totalNumberOfGuesses by 1
            int a = totalNumberOfGuesses;
            int b = 1;
            int c = a + b; // c is the new total number of guesses
            totalNumberOfGuesses = c;

            // Now we compare the user's guess to the secret answer value
            if (userGuessValue < secretAnswerValue) {
                // If the user's guess is less than the secret answer, it is too low
                System.out.println("Too low!");
            } else if (userGuessValue > secretAnswerValue) {
                // If the user's guess is greater than the secret answer, it is too high
                System.out.println("Too high!");
            } else {
                // If the guess is not less and not greater, it must be equal, so it is correct
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            }
        }

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}