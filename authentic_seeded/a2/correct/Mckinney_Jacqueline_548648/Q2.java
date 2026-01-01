import java.util.Scanner;
import java.util.Random;

public class Q2 {
    public static void main(String[] args) {
        // Create a new Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);
        
        // Create a new Random object to generate a random secret number
        Random randomNumberGenerator = new Random();
        
        // Generate the secret answer between 1 and 100 (inclusive)
        int secretAnswerNumber = randomNumberGenerator.nextInt(100) + 1;
        
        // Create a variable to store the user's current guess
        int userGuessNumber = 0;
        
        // Create a variable to count how many guesses the user has taken
        int totalNumberOfGuesses = 0;
        
        // Use a boolean to keep track of whether the user has guessed correctly
        boolean userHasGuessedCorrectly = false;
        
        // Start a loop that continues until the user guesses the correct number
        while (userHasGuessedCorrectly == false) {
            // Prompt the user to guess a number each time through the loop
            System.out.print("Guess a number (1-100): ");
            
            // Read the user's guess from the Scanner
            if (userInputScanner.hasNextInt()) {
                int temporaryUserGuessHolder = userInputScanner.nextInt();
                userGuessNumber = temporaryUserGuessHolder;
            } else {
                // If the user does not enter an integer, consume the invalid input
                String invalidInputHolder = userInputScanner.next();
                // Since the input was not an integer, we warn them but continue the loop
                System.out.println("Please enter a valid integer between 1 and 100.");
                // Go back to the start of the loop without counting this as a guess
                continue;
            }
            
            // Increment the number of guesses, since the user has entered a guess
            totalNumberOfGuesses = totalNumberOfGuesses + 1;
            
            // Check that the guess is within the expected range 1 to 100
            if (userGuessNumber < 1 || userGuessNumber > 100) {
                // If out of range, warn the user but still allow them to continue
                System.out.println("Your guess is out of range. Please guess between 1 and 100.");
                // Do not end the game here; the loop will continue
                continue;
            }
            
            // Compare the user's guess to the secret answer number
            if (userGuessNumber == secretAnswerNumber) {
                // If the guess is exactly equal to the secret answer, they are correct
                userHasGuessedCorrectly = true;
                // Print out the success message and how many guesses they took
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            } else {
                // The guess was not equal to the secret number, so we need to give a hint
                if (userGuessNumber > secretAnswerNumber) {
                    // If the guess is greater than the answer, it is too high
                    System.out.println("Too high!");
                } else if (userGuessNumber < secretAnswerNumber) {
                    // If the guess is less than the answer, it is too low
                    System.out.println("Too low!");
                } else {
                    // This else branch should never happen, but we include it to be safe
                    System.out.println("Unexpected comparison result. Please try again.");
                }
            }
        }
        
        // Close the Scanner to avoid any resource leaks
        userInputScanner.close();
    }
}