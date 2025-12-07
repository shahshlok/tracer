import java.util.Random;
import java.util.Scanner;

public class Q2 {

   public static void main(String[] args) {

      Scanner input = new Scanner(System.in);
      Random rand = new Random();

      int a = 100;
   	int b = 1;
      int answer = rand.nextInt(a) + b;

      int guess_count = 0;
      int userGuess = 0;



      while (userGuess != answer) {

         System.out.print("Guess a number (1-100): ");
      userGuess = input.nextInt();
      	guess_count = guess_count + 1;

      	int c = userGuess - answer;

         if (c == 0) {
            // correct
         } else if (c > 0) {
            System.out.println("Too high!");
         } else {
         	  System.out.println("Too low!");
         }
      }

      System.out.println("Correct! You took " + guess_count + " guesses.");

      input.close();
   }

}