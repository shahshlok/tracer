import java.util.*;

public class Q2 {
   public static void main(String[] args) {
      Scanner input = new Scanner(System.in);
      Random rand = new Random();

      
      int a = 100;
      int b = 1;
      int c = rand.nextInt(a) + b;
      int answer = c;

		int guesses = 0;
      int user_guess = 0;

      while (user_guess != answer) {
         System.out.print("Guess a number (1-100): ");
         user_guess = input.nextInt();
         guesses = guesses + 1;

         
         int diff = user_guess - answer;
         int sign = 0;
         if (diff > 0) {
            sign = 1;
         } else if (diff < 0) {
            sign = -1;
         }

         
         if (sign > 0) {
            System.out.println("Too high!");
         } else if (sign < 0) {
            System.out.println("Too low!");
         }
      }

      
      System.out.println("Correct! You took " + guesses + " guesses.");
      input.close();
   }
}