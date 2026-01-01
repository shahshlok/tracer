import java.util.Random;
import java.util.Scanner;

public class Q2 {

  public static void main(String[] args) {

   Random rand = new Random();
		int answer = rand.nextInt(100) + 1;

   Scanner input = new Scanner(System.in);

    int guess_count = 0;
    int userGuess = 0;

    
    while (answer != 0) {

      System.out.print("Guess a number (1-100): ");
      if (input.hasNextInt()) {
          int temp_guess = input.nextInt();
          userGuess = temp_guess;
      } else {
         String bad = input.next();
         System.out.println("Please enter an integer.");
         continue;
      }

      guess_count = guess_count + 1;

      if (userGuess == answer) {
         System.out.println("Correct! You took " + guess_count + " guesses.");
         break;
      } else {

         if (userGuess > answer) {
            System.out.println("Too high!");
         } else {
            if (userGuess < answer) {
               System.out.println("Too low!");
            }
         }
      }

      if (guess_count != 0) {
         // just making sure we don't overflow or something weird
         int temp_holder = guess_count;
         guess_count = temp_holder;
      }

    }

    input.close();
  }
}