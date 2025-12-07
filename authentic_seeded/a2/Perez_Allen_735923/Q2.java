import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Random object to generate the secret number
        Random randomNumberGenerator = new Random();
        
        // Step 2: Generate a random number between 1 and 100
        int secretAnswerNumber = randomNumberGenerator.nextInt(100) + 1;
        
        // Step 3: Create a Scanner to read the user's guesses from the keyboard
        Scanner userInputScanner = new Scanner(System.in);
        
        // Step 4: Create a variable to store the user's current guess
        int userGuessNumber = 0;
        
        // Step 5: Create a variable to count how many guesses the user has taken
        int totalNumberOfGuesses = 0;

        // Step 6: Ask the user for their first guess before the loop
        System.out.print("Guess a number (1-100): ");
        userGuessNumber = userInputScanner.nextInt();
        totalNumberOfGuesses = totalNumberOfGuesses + 1;
        
        // Step 7: Keep checking the guess until they guess the secret number correctly
        while (userGuessNumber != secretAnswerNumber) {
            // Step 8: Check if the guess is too low, too high, or correct
            if (userGuessNumber < secretAnswerNumber) {
                // Step 9: Tell the user their guess was too low
                System.out.println("Too low!");
            } else if (userGuessNumber > secretAnswerNumber) {
                // Step 10: Tell the user their guess was too high
                System.out.println("Too high!");
            }
            
            // Step 11: The loop will naturally check again, so it will wait for the user to guess again
            // (No need to read another guess here, the while condition will handle it)
        }
        
        // Step 12: When the loop is done, the user must have guessed correctly
        System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
        
        // Step 13: Close the Scanner because we are done reading input
        userInputScanner.close();
    }
}