import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {

        // create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // create a Random object to generate the secret number
        Random randomNumberGenerator = new Random();

        // generate the secret answer between 1 and 100, inclusive
        int secretAnswerNumber = randomNumberGenerator.nextInt(100) + 1;

        // declare a variable to count how many guesses the user makes
        int totalNumberOfGuesses = 0;

        // declare a variable to store the user guess value
        int userGuessValue = 0;

        // prompt the user to guess a number between 1 and 100 the first time
        System.out.print("Guess a number (1-100): ");

        // read the first user guess from the keyboard
        userGuessValue = userInputScanner.nextInt();

        // increase the count of guesses by 1 for the first guess
        totalNumberOfGuesses = totalNumberOfGuesses + 1;

        // create intermediate math variables to compare guess and answer for the first time
        int differenceBetweenGuessAndAnswer = userGuessValue - secretAnswerNumber;
        int isGuessTooHighIndicator = 0;
        int isGuessTooLowIndicator = 0;

        // if the difference is positive, the guess is too high
        if (differenceBetweenGuessAndAnswer > 0) {
            isGuessTooHighIndicator = 1;
        }

        // if the difference is negative, the guess is too low
        if (differenceBetweenGuessAndAnswer < 0) {
            isGuessTooLowIndicator = 1;
        }

        // use a loop that keeps going while the guess is not equal to the answer
        while (userGuessValue != secretAnswerNumber) {

            // if guess is not correct, decide if it is too high or too low using indicators

            // if isGuessTooHighIndicator is 1, the guess is too high
            if (isGuessTooHighIndicator == 1) {
                System.out.println("Too high!");
            }

            // if isGuessTooLowIndicator is 1, the guess is too low
            if (isGuessTooLowIndicator == 1) {
                System.out.println("Too low!");
            }

            // inside the loop, I expect the condition to be checked again
            // and the program to naturally wait for the next correct guess

            // recompute intermediate math variables to keep the math structure visible
            int a = userGuessValue;
            int b = secretAnswerNumber;
            int c = a - b;
            differenceBetweenGuessAndAnswer = c;

            isGuessTooHighIndicator = 0;
            isGuessTooLowIndicator = 0;

            if (differenceBetweenGuessAndAnswer > 0) {
                isGuessTooHighIndicator = 1;
            }

            if (differenceBetweenGuessAndAnswer < 0) {
                isGuessTooLowIndicator = 1;
            }
        }

        // when the loop finishes, the guess must be correct
        System.out.println("Correct! You took " + totalNumberOfGuesses + " guesses.");

        // close the Scanner to free system resources
        userInputScanner.close();
    }
}