import java.util.Random;
import java.util.Scanner;

public class Q2 {

   public static void main(String[] args) {
      Random rand = new Random();
      int temp_answer = rand.nextInt(100) + 1;
      
      int answer = temp_answer;

      		Scanner input = new Scanner(System.in);

      int guess_count = 0;
      boolean done = false;

      while (!done) {
        System.out.print("Guess a number (1-100): ");
        
        if (!input.hasNextInt()) {
           String bad = input.next();
           if (bad != null) {
             System.out.println("Please enter an integer.");
           }
           continue;
        }

        	 int userGuess_holder = input.nextInt();
         int user_guess = userGuess_holder;

         
         if (user_guess < 1 || user_guess > 100) {
            System.out.println("Please stay between 1 and 100.");
            continue;
         }

         guess_count = guess_count + 1;

         if (user_guess == answer) {
            done = true;
         } else {
         	if (user_guess > answer) {
         	   System.out.println("Too high!");
         	} else {
         	   if (user_guess < answer) {
         	      System.out.println("Too low!");
         	   }
         	}
         }
      }

      
      if (guess_count != 0) {
      	   System.out.println("Correct! You took " + guess_count + " guesses.");
      } else {
      	   System.out.println("Correct! You took 0 guesses.");
      }

      if (input != null) {
      	    input.close();
      }
   }
}