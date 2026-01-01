import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Create a Random object to generate the secret number
        Random randomNumberGenerator = new Random();

        // Generate a random integer between 1 and 100 (inclusive)
        int randomNumberBase = randomNumberGenerator.nextInt(100);
        int randomNumberOffset = 1;
        int secretAnswerNumber = randomNumberBase + randomNumberOffset;

        // Variable to keep track of how many guesses the user has made
        int totalNumberOfGuesses = 0;

        // Variable to store the current user guess
        int currentUserGuessNumber = 0;

        // Loop until the user guesses the correct number
        while (currentUserGuessNumber != secretAnswerNumber) {
            // Prompt the user for a guess
            System.out.print("Guess a number (1-100): ");

            // Read the user's guess from the keyboard
            currentUserGuessNumber = userInputScanner.nextInt();

            // Increase the total number of guesses by 1
            int incrementAmount = 1;
            totalNumberOfGuesses = totalNumberOfGuesses + incrementAmount;

            // Compare the user's guess with the secret answer
            int differenceBetweenGuessAndAnswer = currentUserGuessNumber - secretAnswerNumber;

            // If the difference is positive, the guess is too high
            if (differenceBetweenGuessAndAnswer > 0) {
                System.out.println("Too high!");
            }
            // If the difference is negative, the guess is too low
            else if (differenceBetweenGuessAndAnswer < 0) {
                System.out.println("Too low!");
            }
            // If the difference is zero, the guess is correct
            else {
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            }
        }

        // Close the Scanner to free resources
        userInputScanner.close();
    }
}