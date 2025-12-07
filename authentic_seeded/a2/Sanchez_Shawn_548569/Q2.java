import java.util.Scanner;
import java.util.Random;

public class Q2 {
    public static void main(String[] args) {

        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Create a Random object to generate random numbers
        Random randomNumberGenerator = new Random();

        // Generate the secret answer between 1 and 100 (inclusive)
        int secretAnswerNumber = randomNumberGenerator.nextInt(100) + 1;

        // Create a variable to store the user's current guess
        int currentUserGuessNumber = 0;

        // Create a variable to count how many guesses the user has taken
        int totalNumberOfGuessesTaken = 0;

        // We will use a boolean to track if the user has guessed correctly
        boolean hasUserGuessedCorrectly = false;

        // Start a loop that continues until the user guesses the correct number
        while (hasUserGuessedCorrectly == false) {

            // Prompt the user to guess a number in the correct range
            System.out.print("Guess a number (1-100): ");

            // Read the user's guess as an integer
            if (userInputScanner.hasNextInt()) {
                int temporaryUserGuessHolder = userInputScanner.nextInt();
                currentUserGuessNumber = temporaryUserGuessHolder;
            } else {
                // If the user does not enter an integer, consume the invalid input
                String invalidUserInputString = userInputScanner.next();
                // Warn the user and continue to the next loop iteration
                System.out.println("Please enter a valid integer between 1 and 100.");
                // Since we did not get a valid guess, we skip the rest of the loop
                continue;
            }

            // Increase the guess counter because the user has made a guess
            totalNumberOfGuessesTaken = totalNumberOfGuessesTaken + 1;

            // Check that the guess is within the valid range 1-100
            if (currentUserGuessNumber < 1 || currentUserGuessNumber > 100) {
                // Tell the user the guess is out of range
                System.out.println("Your guess must be between 1 and 100.");
                // We do not compare this guess to the secret number
                // Move on to the next loop iteration
                continue;
            }

            // Compare the user's guess to the secret answer
            if (currentUserGuessNumber == secretAnswerNumber) {
                // If the guess matches the secret answer, the user is correct
                hasUserGuessedCorrectly = true;

                // Store the final number of guesses in a temporary holder
                int finalNumberOfGuessesHolder = totalNumberOfGuessesTaken;

                // Print the success message with the total number of guesses
                System.out.println("Correct! You took " + finalNumberOfGuessesHolder + " guesses.");
            } else {
                // If the guess does not match, check if it is too high or too low

                if (currentUserGuessNumber > secretAnswerNumber) {
                    // The guess is greater than the secret answer
                    System.out.println("Too high!");
                } else {
                    // The guess is less than the secret answer
                    System.out.println("Too low!");
                }

                // The user has not guessed correctly yet, so the loop will continue
            }
        }

        // Close the Scanner to avoid resource leaks
        userInputScanner.close();
    }
}