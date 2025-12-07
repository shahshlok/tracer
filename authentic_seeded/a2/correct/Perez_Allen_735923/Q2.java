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
        
        // Step 6: Keep asking the user to guess until they guess the secret number correctly
        while (userGuessNumber != secretAnswerNumber) {
            // Step 7: Prompt the user to enter a guess
            System.out.print("Guess a number (1-100): ");
            
            // Step 8: Read the user's guess
            userGuessNumber = userInputScanner.nextInt();
            
            // Step 9: Increase the guess counter because the user just made a guess
            totalNumberOfGuesses = totalNumberOfGuesses + 1;
            
            // Step 10: Check if the guess is too low, too high, or correct
            if (userGuessNumber < secretAnswerNumber) {
                // Step 11: Tell the user their guess was too low
                System.out.println("Too low!");
            } else if (userGuessNumber > secretAnswerNumber) {
                // Step 12: Tell the user their guess was too high
                System.out.println("Too high!");
            } else {
                // Step 13: The user guessed correctly, so tell them and show how many guesses it took
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            }
        }
        
        // Step 14: Close the Scanner because we are done reading input
        userInputScanner.close();
    }
}