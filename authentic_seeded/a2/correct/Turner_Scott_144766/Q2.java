import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Random rand = new Random();
        int answer = rand.nextInt(100) + 1;

        Scanner scanner = new Scanner(System.in);

        int guess = 0;
        int guessCount = 0;
        boolean correct = false;

        while (!correct) {
            System.out.print("Guess a number (1-100): ");
            if (scanner.hasNextInt()) {
                guess = scanner.nextInt();
                guessCount = guessCount + 1;

                if (guess == answer) {
                    correct = true;
                    System.out.println("Correct! You took " + guessCount + " guesses.");
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
            }
        }

        scanner.close();
    }
}