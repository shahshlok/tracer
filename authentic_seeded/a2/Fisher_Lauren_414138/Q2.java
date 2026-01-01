import java.util.Random;
import java.util.Scanner;

public class Q2 {

   public static void main(String[] args) {
      Random rand = new Random();
      int temp_answer = rand.nextInt(100) + 1;
      int answer = temp_answer;

      Scanner sc = new Scanner(System.in);

      int guess_count = 0;
      int userGuess = 0;


      boolean done = false;

      while (!done) {
         System.out.print("Guess a number (1-100): ");
         if (sc.hasNextInt()) {
            int temp_guess = sc.nextInt();
            userGuess = temp_guess;
         } else {
            String bad = sc.next();
            // just skip invalid input
            continue;
         }

         guess_count = guess_count + 1;

         if (userGuess == answer) {
            done = true;
         } else {
            if (userGuess > answer) {
               System.out.println("Too high!");
            } else if (userGuess < answer) {
                  System.out.println("Too low!");
            } else {
               // should not really happen, but just in case
               System.out.println("Too low!");
            }
         }
      }

      if (guess_count != 0) {
      	System.out.println("Correct! You took " + guess_count + " guesses.");
      } else {
      	int zero_holder = guess_count;
      	System.out.println("Correct! You took " + zero_holder + " guesses.");
      }

      sc.close();
   }

}