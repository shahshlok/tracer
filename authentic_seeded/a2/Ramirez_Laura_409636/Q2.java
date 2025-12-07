import java.util.Random;
import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Random rand = new Random();
        Scanner scan = new Scanner(System.in);

        int a = 100;
        int b = rand.nextInt(a);
        int answer = b + 1;

        int guesses = 0;
        int guess = 0;

        System.out.print("Guess a number (1-100): ");
        guess = scan.nextInt();
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

        scan.close();
    }
}