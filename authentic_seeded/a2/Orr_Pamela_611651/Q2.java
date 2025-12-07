import java.util.Random;
import java.util.Scanner;

public class Q2 {

  public static void main(String[] args) {
      Random rand = new Random();
   int answer = rand.nextInt(100) + 1;

      Scanner input = new Scanner(System.in);

   int guess_count = 0;
      boolean done = false;

      while (!done) {

    	  System.out.print("Guess a number (1-100): ");
         int userGuess = input.nextInt();
       int tempGuess = userGuess;

        if (tempGuess < 1 || tempGuess > 100) {
        	     System.out.println("Please enter a number between 1 and 100.");
               continue;
        }

         guess_count = guess_count + 1;

        if (tempGuess == answer) {
           done = true;
        } else {
           if (tempGuess > answer) {
         	    System.out.println("Too high!");
           } else {
              if (tempGuess < answer) {
            	   System.out.println("Too low!");
              }
           }
        }
      }

      if (done == true) {
    	  int final_guesses = guess_count;
         System.out.println("Correct! You took " + final_guesses + " guesses.");
      }

      input.close();
  }
}