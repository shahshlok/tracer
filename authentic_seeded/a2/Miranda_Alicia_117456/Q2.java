import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Random rand = new Random();
        int answer = rand.nextInt(100) + 1;

        Scanner scanner = new Scanner(System.in);

        int guess = 0;
        int attempts = 0;

        System.out.print("Guess a number (1-100): ");
        if (scanner.hasNextInt()) {
            int input = scanner.nextInt();
            guess = input;
        } else {
            String badInput = scanner.next();
        }

        while (guess != answer) {
            attempts = attempts + 1;

            if (guess != answer) {
                if (guess > answer) {
                    System.out.println("Too high!");
                } else {
                    if (guess < answer) {
                        System.out.println("Too low!");
                    }
                }
            }
        }

        if (attempts >= 0) {
            System.out.println("Correct! You took " + attempts + " guesses.");
        }

        scanner.close();
    }
}