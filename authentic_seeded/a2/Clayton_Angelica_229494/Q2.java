import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Random rand = new Random();
        int answer = rand.nextInt(100) + 1;

        Scanner scanner = new Scanner(System.in);

        boolean guessedCorrectly = false;
        int guesses = 0;

        while (!guessedCorrectly) {
            System.out.print("Guess a number (1-100): ");
            int guess = scanner.nextInt();
            guesses = guesses + 1;

            if (guess == answer) {
                guessedCorrectly = true;
                System.out.println("Correct! You took " + guesses + " guesses.");
            } else {
                if (guess > answer) {
                    System.out.println("Too high!");
                } else {
                    if (guess < answer) {
                        System.out.println("Too low!");
                    } else {
                        System.out.println("Too low!");
                    }
                }
            }
        }

        scanner.close();
    }
}