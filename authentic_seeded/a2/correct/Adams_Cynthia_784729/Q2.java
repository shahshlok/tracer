import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Create a Random object to generate the secret number
        Random randomNumberGenerator = new Random();

        // Generate the secret number between 1 and 100 (inclusive)
        int secretNumber = randomNumberGenerator.nextInt(100) + 1;

        // Declare variables to store the user's guess and the number of guesses
        int userGuessValue = 0;
        int numberOfGuesses = 0;

        // Keep looping until the user guesses the correct number
        while (userGuessValue != secretNumber) {
            // Ask the user to guess a number between 1 and 100
            System.out.print("Guess a number (1-100): ");

            // Read the user's guess from the keyboard
            userGuessValue = userInputScanner.nextInt();

            // Each time the user makes a guess, increase the number of guesses by 1
            numberOfGuesses = numberOfGuesses + 1;

            // Use intermediate variables to compare the guess and the secret number
            int differenceBetweenGuessAndSecret = userGuessValue - secretNumber;

            // If the guess is less than the secret number, it is too low
            if (differenceBetweenGuessAndSecret < 0) {
                System.out.println("Too low!");
            }
            // If the guess is greater than the secret number, it is too high
            else if (differenceBetweenGuessAndSecret > 0) {
                System.out.println("Too high!");
            }
            // Otherwise, the guess is equal to the secret number, so it is correct
            else {
                System.out.println("Correct! You took " + numberOfGuesses + " guesses.");
            }
        }

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}