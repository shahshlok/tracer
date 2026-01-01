import java.util.Random;
import java.util.Scanner;

public class Q2 {

   public static void main(String[] args) {

      Random rand = new Random();
      int a = 100;
      int b = 1;
      int c = rand.nextInt(a) + b;  
      
      int secretNumber = c;

      Scanner input = new Scanner(System.in);

      int guess_count = 0;
      int userGuess = 0;

		while (userGuess != secretNumber) {
      System.out.print("Guess a number (1-100): ");
         userGuess = input.nextInt();
         
         guess_count = guess_count + 1;

      int diff = userGuess - secretNumber;

         if (diff == 0) {
            System.out.println("Correct! You took " + guess_count + " guesses.");
         } else if (diff > 0) {
            System.out.println("Too high!");
         } else {
            System.out.println("Too low!");
         }

		}

      input.close();
   }

}