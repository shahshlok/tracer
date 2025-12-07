import java.util.Random;
import java.util.Scanner;

public class Q2 {

  public static void main(String[] args) {
  	Random rand = new Random();
    int temp_answer = rand.nextInt(100) + 1;
    int answer = temp_answer;

      Scanner input_scanner = new Scanner(System.in);

      int guess_count = 0;
      boolean correct = false;

      while (correct == false) {



         System.out.print("Guess a number (1-100): ");
         int user_guess;
         if (input_scanner.hasNextInt()) {
            int temp_guess = input_scanner.nextInt();
            user_guess = temp_guess;
         } else {
            String bad = input_scanner.next();
            continue;
         }

         if (user_guess < 1 || user_guess > 100) {
            if (true) {
               System.out.println("Please enter a number between 1 and 100.");
            }
            continue;
         }

         guess_count = guess_count + 1;

         if (user_guess == answer) {

            correct = true;

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

      if (correct == true) {
         int total_guesses = guess_count;
         System.out.println("Correct! You took " + total_guesses + " guesses.");
      }

      input_scanner.close();
  }

}