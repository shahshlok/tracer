import java.util.Scanner;
import java.util.Random;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Create a Random object to generate a random number
        Random randomNumberGenerator = new Random();

        // Step 3: Generate the secret number between 1 and 100
        int secretAnswerNumber = randomNumberGenerator.nextInt(100) + 1;

        // Step 4: Create a variable to store the user's guess
        int userGuessNumber = 0;

        // Step 5: Create a variable to count how many guesses the user takes
        int numberOfGuessesTaken = 0;

        // Step 6: Keep asking the user for guesses until they guess correctly
        while (userGuessNumber != secretAnswerNumber) {
            // Step 6a: Prompt the user to guess a number
            System.out.print("Guess a number (1-100): ");

            // Step 6b: Read the user's guess
            userGuessNumber = userInputScanner.nextInt();

            // Step 6c: Increase the guess counter by 1
            numberOfGuessesTaken = numberOfGuessesTaken + 1;

            // Step 6d: Check if the guess is too low, too high, or correct
            if (userGuessNumber < secretAnswerNumber) {
                // The guess is too low
                System.out.println("Too low!");
            } else if (userGuessNumber > secretAnswerNumber) {
                // The guess is too high
                System.out.println("Too high!");
            } else {
                // The guess is correct
                System.out.println("Correct! You took " + numberOfGuessesTaken + " guesses.");
            }
        }

        // Step 7: Close the Scanner
        userInputScanner.close();
    }
}