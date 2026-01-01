import java.util.Random;
import java.util.Scanner;

public class Q2 {
  public static void main(String[] args) {

  	Random rand = new Random();
    int secret_number = rand.nextInt(100) + 1;

      Scanner input = new Scanner(System.in);

    int guessCount = 0;
   int guess = 0;

        while (guess != secret_number) {

      System.out.print("Guess a number (1-100): ");
        guess = input.nextInt();
   guessCount++;

      if (guess < secret_number) {
      	System.out.println("Too low!");
      } else if (guess > secret_number) {
        	 System.out.println("Too high!");
      } else {
        System.out.println("Correct! You took " + guessCount + " guesses.");
      }

    }

      input.close();
  }
}