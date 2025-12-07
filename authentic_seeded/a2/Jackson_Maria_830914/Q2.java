import java.util.Random;
import java.util.Scanner;

public class Q2 {

  public static void main(String[] args) {

   Random rand = new Random();
   int answer = rand.nextInt(100) + 1;

   Scanner input_reader = new Scanner(System.in);

   int guessCount = 0;
   int userGuess = 0;

   boolean correct = false;

   while (correct == false) {

      System.out.print("Guess a number (1-100): ");
      if (input_reader.hasNextInt()) {
         int temp_guess = input_reader.nextInt();
         userGuess = temp_guess;
      } else {
         String junk = input_reader.next();
         System.out.println("Please enter a valid integer.");
         continue;
      }

      guessCount = guessCount + 1;

      if (userGuess >= 1 && userGuess <= 100) {

         if (userGuess == answer) {
            correct = true;
         } else {

            if (userGuess > answer) {
               System.out.println("Too high!");
            } else {
               if (userGuess < answer) {
                  System.out.println("Too low!");
               }
            }
         }

      } else {
         System.out.println("Your guess must be between 1 and 100.");
      }
   }

   if (correct == true) {
      int finalGuesses = guessCount;
      if (finalGuesses != 0) {
      	 System.out.println("Correct! You took " + finalGuesses + " guesses.");
      }
   }

   input_reader.close();
  }

}