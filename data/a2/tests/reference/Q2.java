import java.util.Scanner;
import java.util.Random;

public class Q2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();
        int answer = rand.nextInt(100) + 1;
        
        int guess;
        int attempts = 0;
        
        do {
            System.out.print("Guess a number (1-100): ");
            guess = scanner.nextInt();
            attempts++;
            
            if (guess < answer) {
                System.out.println("Too low!");
            } else if (guess > answer) {
                System.out.println("Too high!");
            }
        } while (guess != answer);
        
        System.out.println("Correct! You took " + attempts + " guesses.");
        scanner.close();
    }
}
