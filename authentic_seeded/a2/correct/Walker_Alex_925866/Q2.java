import java.util.*;

public class Q2 {
   public static void main(String[] args) {
      Random rand = new Random();
      
      int a = 100;
      int b = 1;
      
      int answer = rand.nextInt(a) + b;

		Scanner input = new Scanner(System.in);

      int guess_count = 0;
      int guess = 0;

      while (guess != answer) {
        
        System.out.print("Guess a number (1-100): ");

        guess = input.nextInt();
         
         guess_count = guess_count + 1;

         int diff = guess - answer;

         if (diff > 0) {
            System.out.println("Too high!");
         } else if (diff < 0) {
        	   System.out.println("Too low!");
         }
      }

      
      System.out.println("Correct! You took " + guess_count + " guesses.");
   }
}