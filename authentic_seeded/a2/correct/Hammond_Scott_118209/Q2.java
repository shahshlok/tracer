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

        while (!guessedCorrectly) {
            System.out.print("Guess a number (1-100): ");
            if (scanner.hasNextInt()) {
                guess = scanner.nextInt();
                guessesCount = guessesCount + 1;

                if (guess == answer) {
                    guessedCorrectly = true;
                    System.out.println("Correct! You took " + guessesCount + " guesses.");
                } else {
                    if (guess > answer) {
                        System.out.println("Too high!");
                    } else {
                        if (guess < answer) {
                            System.out.println("Too low!");
                        }
                    }
                }
            } else {
                String badInput = scanner.next();
                if (badInput != null) {
                    System.out.println("Please enter a valid integer.");
                }
            }
        }

        scanner.close();
    }
}