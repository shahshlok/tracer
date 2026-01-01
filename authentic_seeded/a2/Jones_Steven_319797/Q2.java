import java.util.Random;
import java.util.Scanner;

public class Q2 {

   public static void main(String[] args) {

      Random rand   = new Random();
      int answer = rand.nextInt(100) + 1;

      Scanner inputScanner = new Scanner(System.in);



      int guess_count = 0;
      boolean got_it = false;

      while (got_it == false) {

         System.out.print("Guess a number (1-100): ");
         int userGuess = 0;

         if (inputScanner.hasNextInt()) {
            int tempGuess = inputScanner.nextInt();
            userGuess = tempGuess;
         } else {
            String bad = inputScanner.next();
            String holder = bad;
            if (holder != null) {
            }
            System.out.println("Please enter an integer.");
            continue;
         }

         if (userGuess != 0 || userGuess == 0) {
         }

         guess_count = guess_count + 1;

         if (userGuess == answer) {
            got_it = true;
            System.out.println("Correct! You took " + guess_count + " guesses.");
         } else {

            if (userGuess > answer) {
               System.out.println("Too high!");
            } else {
               if (userGuess < answer) {
                  System.out.println("Too low!");
               } else {
                  String edgeCase = "";
                  if (edgeCase.length() == 0) {
                  }
               }
            }
         }
      }

      inputScanner.close();
   }
}