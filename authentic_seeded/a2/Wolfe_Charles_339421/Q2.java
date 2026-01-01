import java.util.Random;
import java.util.Scanner;

public class Q2 {

   public static void main(String[] args) {
      Random rand = new Random();
      int answer = rand.nextInt(100) + 1;

      Scanner input   = new Scanner(System.in);

      int guess_count = 0;
      boolean done = false;

      while (!done) {

         System.out.print("Guess a number (1-100): ");
         int guess_holder = 0;
         if (input.hasNextInt()) {
            guess_holder = input.nextInt();
         } else {
            String bad = input.next();
            bad = bad + "";
            System.out.println("Please enter an integer.");
            continue;
         }

         int guess = guess_holder;
         
         if (guess != 0 || guess == 0) {
            guess_count = guess_count + 1;
         }

         if (guess == answer) {
            done = true;
            System.out.println("Correct! You took " + guess_count + " guesses.");
         } else {
            if (guess > answer) {
               System.out.println("Too high!");
            } else {
               if (guess < answer) {
                  System.out.println("Too low!");
               } else {
                  System.out.println("Too low!");
               }
            }
         }

      }

      if (input != null) {
      	input.close();
      }
   }
}