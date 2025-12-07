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
         if (sc.hasNextInt()) {
         	  int user_guess = sc.nextInt();
            
            if (user_guess >= 1 && user_guess <= 100) {
            	  guess_count = guess_count + 1;
            	  int temp_answer = answer;
            	  int temp_guess = user_guess;

            	  if (temp_guess == temp_answer) {
            	  	  done = true;
            	  	  System.out.println("Correct! You took " + guess_count + " guesses.");
            	  } else {
            	  	  if (temp_guess > temp_answer) {
            	  	  	  System.out.println("Too high!");
            	  	  } else {
            	  	  	  System.out.println("Too low!");
            	  	  }
            	  }
            } else {
            	  // outside range
            	  if (user_guess < 1) {
            	  	  System.out.println("Please enter a number between 1 and 100.");
            	  } else if (user_guess > 100) {
            	  	  System.out.println("Please enter a number between 1 and 100.");
            	  }
            }

         } else {
         	  String bad_input = sc.next();
         	  if (bad_input != null) {
         	  	  System.out.println("Please enter an integer between 1 and 100.");
         	  }
         }

         if (answer < 1 || answer > 100) {
         	  // this should never happen, but I'm checking anyway
         	  answer = 1;
         }
      }

      sc.close();
   }
}