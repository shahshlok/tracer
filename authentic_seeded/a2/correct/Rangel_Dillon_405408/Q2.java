import java.util.Scanner;
import java.util.Random;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read the user's guesses from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Create a Random object so we can generate a random secret number
        Random randomNumberGenerator = new Random();

        // Step 3: Generate the secret number between 1 and 100
        int secretAnswerNumber = randomNumberGenerator.nextInt(100) + 1;

        // Step 4: Create a variable to store the user's current guess
        int userGuessNumber = 0;

        // Step 5: Create a variable to count how many guesses the user has made
        int numberOfGuessesTaken = 0;

        // Step 6: Keep asking the user to guess until their guess matches the secret number
        while (userGuessNumber != secretAnswerNumber) {
            // Step 6a: Ask the user to enter a guess
            System.out.print("Guess a number (1-100): ");

            // Step 6b: Read the user's guess from the keyboard
            userGuessNumber = userInputScanner.nextInt();

            // Step 6c: Increase the number of guesses taken by 1
            numberOfGuessesTaken++;

            // Step 6d: Check if the guess is too low, too high, or correct
            if (userGuessNumber < secretAnswerNumber) {
                // The guess is smaller than the secret number
                System.out.println("Too low!");
            } else if (userGuessNumber > secretAnswerNumber) {
                // The guess is larger than the secret number
                System.out.println("Too high!");
            } else {
                // The guess matches the secret number
                System.out.println("Correct! You took " + numberOfGuessesTaken + " guesses.");
            }
        }

        // Step 7: Close the Scanner because we are done reading input
        userInputScanner.close();
    }
}