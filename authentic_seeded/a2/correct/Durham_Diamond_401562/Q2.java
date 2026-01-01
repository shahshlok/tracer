import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Random rand = new Random();
        Scanner in = new Scanner(System.in);

        int a = 100;
        int b = 1;
        int answer = rand.nextInt(a) + b;

        int guesses = 0;
        int guess = 0;

        while (guess != answer) {
            System.out.print("Guess a number (1-100): ");
            guess = in.nextInt();
            guesses++;

            if (guess < answer) {
                System.out.println("Too low!");
            } else if (guess > answer) {
                System.out.println("Too high!");
            }
        }

        System.out.println("Correct! You took " + guesses + " guesses.");
        in.close();
    }
}