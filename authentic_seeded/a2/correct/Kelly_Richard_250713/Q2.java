import java.util.Random;
import java.util.Scanner;

public class Q2 {

   public static void main(String[] args) {
      Random rand = new Random();
      int answer = rand.nextInt(100) + 1;

      Scanner sc = new Scanner(System.in);

      int guess_count = 0;
      boolean done = false;

      while (!done) {
       System.out.print("Guess a number (1-100): ");
        int temp_guess = 0;
        if (sc.hasNextInt()) {
           temp_guess = sc.nextInt();
        } else {
           String bad = sc.next();
           bad = bad + "";
           continue;
        }

        int guess = temp_guess;
        if (guess != 0 || guess == 0) {
         guess_count = guess_count + 1;
        }

        if (guess == answer) {
            done = true;
            int total_guesses = guess_count;
            System.out.println("Correct! You took " + total_guesses + " guesses.");
        } else {
         if (guess > answer) {
               System.out.println("Too high!");
         } else {
          if (guess < answer) {
            System.out.println("Too low!");
          }
         }
        }
      }

      sc.close();
   }

}