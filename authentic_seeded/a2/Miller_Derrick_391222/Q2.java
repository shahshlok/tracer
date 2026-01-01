import java.util.Random;
import java.util.Scanner;

public class Q2 {

   public static void main(String[] args) {
      Scanner in = new Scanner(System.in);
   	Random rand = new Random();

      int a = 1;
      int b = 100;
      int c = b - a + 1;

      int secret_number = rand.nextInt(c) + a;

      int guessCount = 0;
      int guess = 0;

      while (guess != secret_number) {
      	System.out.print("Guess a number (1-100): ");
         guess = in.nextInt();
         
         guessCount = guessCount + 1;

         if (guess < secret_number) {
            System.out.println("Too low!");
         } else if (guess > secret_number) {
         	System.out.println("Too high!");
         } else {
            System.out.println("Correct! You took " + guessCount + " guesses.");
         }
      }

      in.close();
   }
}