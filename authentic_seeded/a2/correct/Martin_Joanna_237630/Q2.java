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
         int userGuess = 0;

         if (sc.hasNextInt()) {
            int tempGuess = sc.nextInt();
            userGuess = tempGuess;
         } else {
            String bad = sc.next();
            if (bad != null) {
               System.out.println("Please enter an integer.");
            }
            continue;
         }

         if (userGuess < 1 || userGuess > 100) {
            System.out.println("Please guess between 1 and 100.");
            continue;
         }

         guess_count = guess_count + 1;

         int diff = userGuess - answer;

         if (diff == 0) {
            System.out.println("Correct! You took " + guess_count + " guesses.");
            done = true;
         } else {
            if (diff > 0) {
               System.out.println("Too high!");
            } else {
               if (diff < 0) {
                  System.out.println("Too low!");
               }
            }
         }

      }

      if (sc != null) {
      	sc.close();
      }

   }

}