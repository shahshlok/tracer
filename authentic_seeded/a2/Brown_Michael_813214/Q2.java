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

      System.out.print("Guess a number (1-100): ");
      
      int first_guess_holder = 0;
      if (input.hasNextInt()) {
         first_guess_holder = input.nextInt();
      } else {
         String bad_first = input.next();
         if (bad_first != null) {
            System.out.println("Please enter an integer.");
         }
      }

      int user_guess = first_guess_holder;

      if (user_guess < 1 || user_guess > 100) {
         System.out.println("Please stay between 1 and 100.");
      }

      while (!done && user_guess != answer) {

        if (user_guess < 1 || user_guess > 100) {
           System.out.println("Please stay between 1 and 100.");
        } else {

        	 guess_count = guess_count + 1;

        	 if (user_guess > answer) {
        	   System.out.println("Too high!");
        	 } else {
        	   if (user_guess < answer) {
        	      System.out.println("Too low!");
        	   }
        	 }
        }
      }

      if (user_guess == answer) {
         guess_count = guess_count + 1;
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