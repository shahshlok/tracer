import java.util.Random;
import java.util.Scanner;

public class Q2 {
   public static void main(String[] args) {
      Random rand = new Random();
      int answer = rand.nextInt(100) + 1;

      Scanner input_reader = new Scanner(System.in);

      int guessCount = 0;
      boolean guessedCorrectly = false;

      
      while (!guessedCorrectly) {
	 System.out.print("Guess a number (1-100): ");
         int userGuess = 0;
         if (input_reader.hasNextInt()) {
            int temp_guess = input_reader.nextInt();
            userGuess = temp_guess;
         } else {
            String bad_input = input_reader.next();
            if (bad_input != null) {
               System.out.println("Please enter an integer between 1 and 100.");
            }
            continue;
         }

         
         if (userGuess < 1 || userGuess > 100) {
            System.out.println("Please enter an integer between 1 and 100.");
            continue;
         }

         guessCount = guessCount + 1;

         
         if (userGuess == answer) {
            guessedCorrectly = true;
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

      
      if (guessedCorrectly == true) {
         int total_guesses = guessCount;
         System.out.println("Correct! You took " + total_guesses + " guesses.");
      }

      if (input_reader != null) {
         input_reader.close();
      }
   }
}