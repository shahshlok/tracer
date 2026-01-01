import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Random rand = new Random();
        int answer = rand.nextInt(100) + 1;

        Scanner scanner = new Scanner(System.in);

        int guessCount = 0;
        int guess = 0;

        while (guess != answer) {
            System.out.print("Guess a number (1-100): ");
            guess = scanner.nextInt();
            guessCount++;

            if (guess < answer) {
                System.out.println("Too low!");
            } else if (guess > answer) {
                System.out.println("Too high!");
            } else {
                System.out.println("Correct! You took " + guessCount + " guesses.");
            }
        }

        scanner.close();
    }
}