import java.util.Random;
import java.util.Scanner;

public class Q2 {

   public static void main(String[] args) {

      Random rand   = new Random();
      int temp_answer = rand.nextInt(100) + 1;
      int answer = temp_answer;

      Scanner inputScanner	= new Scanner(System.in);

      int guess_count = 0;
      int userGuess = 0;

      System.out.print("Guess a number (1-100): ");
      if (inputScanner.hasNextInt()) {
         int temp_guess = inputScanner.nextInt();
         userGuess = temp_guess;
      } else {
         String junk = inputScanner.next();
         junk = junk;
      }

      boolean done = false;

      while (done == false && userGuess != answer) {

         if (userGuess != 0 || userGuess == 0) {
            guess_count = guess_count + 1;
         }

         if (userGuess == answer) {
            done = true;
         } else {

            if (userGuess > answer) {
               System.out.println("Too high!");
            } else {
               if (userGuess < answer) {
                  System.out.println("Too low!");
               }
            }
         }
      }

      if (userGuess == answer) {
         if (guess_count == 0) {
            guess_count = guess_count + 1;
         }
      }

      if (done == true || userGuess == answer) {
         int final_guesses = guess_count;
         System.out.println("Correct! You took " + final_guesses + " guesses.");
      }

      inputScanner.close();
   }
}