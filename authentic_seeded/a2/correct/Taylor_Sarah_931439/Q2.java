import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();

        int a = 100;
        int answer = rand.nextInt(a) + 1;

        int guess = 0;
        int guesses = 0;

        while (guess != answer) {
            System.out.print("Guess a number (1-100): ");
            guess = sc.nextInt();
            guesses++;

            if (guess < answer) {
                System.out.println("Too low!");
            } else if (guess > answer) {
                System.out.println("Too high!");
            }
        }

        System.out.println("Correct! You took " + guesses + " guesses.");
        sc.close();
    }
}