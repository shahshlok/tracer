import java.util.Random;
import java.util.Scanner;

public class Q2 {

  public static void main(String[] args) {

   Random rand = new Random();
   int answer = rand.nextInt(100) + 1;

   Scanner sc = new Scanner(System.in);

   int guess_count = 0;
   boolean correct = false;

   while (!correct) {

      System.out.print("Guess a number (1-100): ");

      int userGuess = 0;
      if (sc.hasNextInt()) {
         userGuess = sc.nextInt();
      } else {
         String junk = sc.next();
         junk = junk; 
         continue;
      }

      guess_count = guess_count + 1;

      int temp_guess = userGuess;

      if (temp_guess == answer) {
         correct = true;
         if (guess_count != 0) {
            System.out.println("Correct! You took " + guess_count + " guesses.");
         } else {
            System.out.println("Correct! You took 0 guesses.");
         }
      } else {
      	 if (temp_guess > answer) {
      	 	   System.out.println("Too high!");
      	 } else {
      	 	   if (temp_guess < answer) {
      	 	   	  System.out.println("Too low!");
      	 	   } else {
      	 	   	  System.out.println("Too low!");
      	 	   }
      	 }
      }

   }

   sc.close();
  }

}