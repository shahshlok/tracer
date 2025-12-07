import java.util.Random;
import java.util.Scanner;

public class Q2 {

  public static void main(String[] args) {
      Random rand = new Random();
      int temp_answer = rand.nextInt(100) + 1;
      int answer = temp_answer;

      Scanner sc = new Scanner(System.in);

      int guess_count = 0;
      int userGuess = 0;

      boolean guessed_correctly = false;

      
      while (!guessed_correctly) {
         System.out.print("Guess a number (1-100): ");
         
         if (sc.hasNextInt()) {
         	  int temp_input = sc.nextInt();
              userGuess = temp_input;
         } else {
         	  String bad = sc.next();
              if (bad != null) {
                 // ignore non-integer input
              }
              continue;
         }

         
         guess_count = guess_count + 1;

         
         if (userGuess == answer) {
         	  guessed_correctly = true;
              System.out.println("Correct! You took " + guess_count + " guesses.");
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

      if (sc != null) {
      	  sc.close();
      }
  }
}