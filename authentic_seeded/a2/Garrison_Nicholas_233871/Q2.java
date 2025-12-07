import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Random object to generate a random integer
        Random randomNumberGenerator = new Random();
        
        // Generate the secret number between 1 and 100 using the formula:
        // secretNumber = randomIntegerFrom0To99 + 1
        int randomBaseValue = randomNumberGenerator.nextInt(100);
        int secretNumberAnswer = randomBaseValue + 1;
        
        // Create a Scanner to read user input from the keyboard
        Scanner keyboardInputScanner = new Scanner(System.in);
        
        // Variable to store the user's current guess
        int userGuessValue = 0;
        
        // Variable to count how many guesses the user has taken
        int totalNumberOfGuesses = 0;
        
        // Loop until the user guesses the correct secret number
        while (userGuessValue != secretNumberAnswer) {
            // Prompt the user for a guess
            System.out.print("Guess a number (1-100): ");
            
            // Read the user's guess from input
            userGuessValue = keyboardInputScanner.nextInt();
            
            // Each time the user enters a guess, increase the guess counter by 1
            int previousTotalNumberOfGuesses = totalNumberOfGuesses;
            int incrementValue = 1;
            totalNumberOfGuesses = previousTotalNumberOfGuesses + incrementValue;
            
            // Compare the user's guess to the secret number
            
            // Compute the difference to think about the relationship mathematically
            int differenceBetweenGuessAndAnswer = userGuessValue - secretNumberAnswer;
            
            // If the guess is less than the secret number, the guess is too low
            if (differenceBetweenGuessAndAnswer < 0) {
                System.out.println("Too low!");
            }
            // If the guess is greater than the secret number, the guess is too high
            else if (differenceBetweenGuessAndAnswer > 0) {
                System.out.println("Too high!");
            }
            // Otherwise, the guess is exactly equal to the secret number
            else {
                System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");
            }
        }
        
        // Close the Scanner to free system resources
        keyboardInputScanner.close();
    }
}