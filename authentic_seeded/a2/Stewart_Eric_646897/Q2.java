import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Random rand = new Random();
        int answer = rand.nextInt(100) + 1;

        Scanner scanner = new Scanner(System.in);

        int guess = 0;
        int guesses = 0;
        boolean correct = false;

        System.out.print("Guess a number (1-100): ");
        if (scanner.hasNextInt()) {
            guess = scanner.nextInt();
            guesses = guesses + 1;
        } else {
            String badInput = scanner.next();
            if (badInput != null) {
                System.out.println("Please enter an integer.");
            }
        }

        while (!correct && guesses != 0) {
            if (guess == answer) {
                correct = true;
            } else {
                if (guess > answer) {
                    System.out.println("Too high!");
                } else {
                    System.out.println("Too low!");
                }
            }
        }

        if (guesses != 0 && correct) {
            System.out.println("Correct! You took " + guesses + " guesses.");
        }

        scanner.close();
    }
}