import java.util.Random;
import java.util.Scanner;

public class Q2 {

   public static void main(String[] args) {

      Random rand = new Random();
         int answer = rand.nextInt(100) + 1;

      Scanner sc = new Scanner(System.in);

      boolean done = false;
   int guess_count = 0;
      
      while (!done) {
      	System.out.print("Guess a number (1-100): ");
         int temp_guess = 0;
         if (sc.hasNextInt()) {
            temp_guess = sc.nextInt();
         } else {
            String bad = sc.next();
            if (bad != null) {
               System.out.println("Please enter an integer.");
            }
            continue;
         }

         int guess = temp_guess;

         if (guess_count >= 0) {
            guess_count = guess_count + 1;
         }

         if (guess == answer) {
            System.out.println("Correct! You took " + guess_count + " guesses.");
            done = true;
         } else {
            if (guess > answer) {
               System.out.println("Too high!");
            } else {
               if (guess < answer) {
                  System.out.println("Too low!");
               } else {
                  System.out.println("Unexpected case.");
               }
            }
         }

         if (done == true) {
            // just to be extra sure we stop
            break;
         }
      }

      if (sc != null) {
      	sc.close();
      }
   }
}