import java.util.Random;
import java.util.Scanner;

public class Q2 {
   public static void main(String[] args) {
      Random rand = new Random();
      int answer = rand.nextInt(100) + 1;

      Scanner sc = new Scanner(System.in);

      int guess_count = 0;
      int userGuess = 0;



      while (userGuess != answer) {
         System.out.print("Guess a number (1-100): ");
      userGuess = sc.nextInt();
         guess_count = guess_count + 1;

         if (userGuess < answer) {
      System.out.println("Too low!");
         } else if (userGuess > answer) {
        System.out.println("Too high!");
         } else {
      System.out.println("Correct! You took " + guess_count + " guesses.");
         }
      }

      sc.close();
   }
}