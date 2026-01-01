import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Random rand = new Random();
        int answer = rand.nextInt(100) + 1;

        Scanner scanner = new Scanner(System.in);

        int guess = 0;
        int guessesCount = 0;
        boolean guessedCorrectly = false;

        System.out.print("Guess a number (1-100): ");
        if (scanner.hasNextInt()) {
            guess = scanner.nextInt();
        } else {
            String badInput = scanner.next();
            if (badInput != null) {
                System.out.println("Please enter a valid integer.");
            }
        }

        while (!guessedCorrectly) {
            if (guess == answer) {
                guessesCount = guessesCount + 1;
                guessedCorrectly = true;
                System.out.println("Correct! You took " + guessesCount + " guesses.");
            } else {
                guessesCount = guessesCount + 1;
                if (guess > answer) {
                    System.out.println("Too high!");
                } else {
                    if (guess < answer) {
                        System.out.println("Too low!");
                    }
                }
            }
        }

        scanner.close();
    }
}