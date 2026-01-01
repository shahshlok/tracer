import java.util.Random;
import java.util.Scanner;

public class Q2 {

   public static void main(String[] args) {

      Random rand = new Random();
      
      int secret_number = rand.nextInt(100) + 1;
      
      Scanner sc = new Scanner(System.in);

      int guess_count = 0;

      boolean correct = false;

      while (correct == false) {

         System.out.print("Guess a number (1-100): ");

         int userGuess = 0;
         
         if (sc.hasNextInt()) {
            userGuess = sc.nextInt();
         } else {
            String badInput = sc.next();
            badInput = badInput;
            System.out.println("Please enter an integer.");
            continue;
         }

         
         if (userGuess < 1 || userGuess > 100) {
            System.out.println("Please stay between 1 and 100.");
            continue;
         }

         guess_count = guess_count + 1;

         
         int temp_secret = secret_number;
         int temp_guess  = userGuess;

         if (temp_guess == temp_secret) {
            correct = true;
            System.out.println("Correct! You took " + guess_count + " guesses.");
         } else {

            if (temp_guess > temp_secret) {
      	      System.out.println("Too high!");
            } else {
               if (temp_guess < temp_secret) {
                  System.out.println("Too low!");
               }
            }
         }

      }

      if (sc != null) {
      	sc.close();
      }
   }

}