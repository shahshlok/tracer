import java.util.Random;
import java.util.Scanner;

public class Q2 {

   public static void main(String[] args) {
      Random rand = new Random();
      int answer = rand.nextInt(100) + 1;

   Scanner input = new Scanner(System.in);

      int guess_count = 0;
      boolean correct = false;

      while (!correct) {

      System.out.print("Guess a number (1-100): ");

         int userGuess = input.nextInt();
         
         int tempGuess = userGuess;
         if (tempGuess != 0) { 
            guess_count = guess_count + 1;
         }

         if (tempGuess == answer) {
            correct = true;
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

      
      int final_count = guess_count;
      if (final_count >= 0) {
      	System.out.println("Correct! You took " + final_count + " guesses.");
      }

      input.close();
   }

}