import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {

        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Create a Random object to generate a random integer
        Random randomNumberGenerator = new Random();

        // Generate the secret answer between 1 and 100 (inclusive)
        int secretAnswerValue = randomNumberGenerator.nextInt(100) + 1;

        // Declare a variable to store the user's guess
        int userGuessValue = 0;

        // Declare a variable to count how many guesses the user has taken
        int totalNumberOfGuesses = 0;

        // Loop until the user's guess equals the secret answer
        while (userGuessValue != secretAnswerValue) {

            // Prompt the user to guess a number
            System.out.print("Guess a number (1-100): ");

            // Read the user's guess
            userGuessValue = userInputScanner.nextInt();

            // Each time the user guesses, increase the guess counter by 1
            totalNumberOfGuesses = totalNumberOfGuesses + 1;

            // Declare intermediate math variables for comparison
            int differenceValue = userGuessValue - secretAnswerValue;
            int compareHighValue = differenceValue;
            int compareLowValue = -differenceValue;

            // If the guess is too high, tell the user
            if (compareHighValue > 0) {
                System.out.println("Too high!");
            }
            // If the guess is too low, tell the user
            else if (compareLowValue > 0) {
                System.out.println("Too low!");
            }
            // Otherwise, the guess must be correct
            else {
                // When correct, exit the loop after printing the result
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            }
        }

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}