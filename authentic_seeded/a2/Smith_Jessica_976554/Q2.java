import java.util.Random;
import java.util.Scanner;

public class Q2 {

   public static void main(String[] args) {
      Random rand   = new Random();
      int temp_answer = rand.nextInt(100) + 1;
      
      int answer = temp_answer;
      
      		Scanner input = new Scanner(System.in);

      int guess_count = 0;
      int userGuess = 0;

      
      boolean correct = false;

      
      System.out.print("Guess a number (1-100): ");
      if (input.hasNextInt()) {
         int temp_guess = input.nextInt();
         userGuess = temp_guess;
      } else {
         String junk = input.next();
         if (junk != null) {
            System.out.println("Please enter an integer.");
         }
      }

      if (userGuess != 0) {
         guess_count = guess_count + 1;
      }

      while (correct == false && userGuess != 0) {
         
        if (userGuess == answer) {
           correct = true;
           System.out.println("Correct! You took " + guess_count + " guesses.");
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

      input.close();
   }
}