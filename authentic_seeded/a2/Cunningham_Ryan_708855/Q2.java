import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Random rand = new Random();
        int answer = rand.nextInt(100) + 1;

        Scanner scanner = new Scanner(System.in);
        int guess;
        int guesses = 0;

        System.out.print("Guess a number (1-100): ");
        guess = scanner.nextInt();
        guesses++;

        while (guess != answer) {
            if (guess < answer) {
                System.out.println("Too low!");
            } else if (guess > answer) {
                System.out.println("Too high!");
            }
            guesses++;
        }

        System.out.println("Correct! You took " + guesses + " guesses.");
        scanner.close();
    }
}