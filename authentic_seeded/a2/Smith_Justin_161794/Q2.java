import java.util.Scanner;
import java.util.Random;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Create a Random object to generate the secret number
        Random randomNumberGenerator = new Random();

        // Generate the secret number between 1 and 100 (inclusive)
        int secretAnswerNumber = randomNumberGenerator.nextInt(100) + 1;

        // Declare a variable to store the user's current guess
        int currentUserGuessNumber = 0;

        // Declare a variable to count how many guesses the user has made
        int totalNumberOfGuesses = 0;

        // Loop until the user correctly guesses the secret number
        while (currentUserGuessNumber != secretAnswerNumber) {
            // Prompt the user to enter a guess between 1 and 100
            System.out.print("Guess a number (1-100): ");

            // Read the user's guess from the keyboard
            currentUserGuessNumber = userInputScanner.nextInt();

            // Each time the user makes a guess, increase the guess counter by 1
            totalNumberOfGuesses = totalNumberOfGuesses + 1;

            // Declare intermediate math variables to compare the guess and the answer
            int comparisonDifference = currentUserGuessNumber - secretAnswerNumber;

            // If the guess is less than the answer, the difference is negative (too low)
            if (comparisonDifference < 0) {
                System.out.println("Too low!");
            }
            // If the guess is greater than the answer, the difference is positive (too high)
            else if (comparisonDifference > 0) {
                System.out.println("Too high!");
            }
            // If the difference is zero, the guess is correct
            else {
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            }
        }

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}